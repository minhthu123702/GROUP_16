// Tạo một file mới tên là AddCategoryActivity.java

package com.example.hanoi_local_hub; // Gói của bạn

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private ImageButton btnBack, btnAddImage, btnPickDate;
    private EditText edtCategoryName, edtCategoryDesc, edtCreateDate;
    private Button btnAddCategory;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri imageUri; // Biến để lưu đường dẫn ảnh được chọn

    // Trình xử lý kết quả trả về khi chọn ảnh
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    btnAddImage.setImageURI(imageUri); // Hiển thị ảnh đã chọn lên ImageButton
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Ánh xạ View
        btnBack = findViewById(R.id.btnBack);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnPickDate = findViewById(R.id.btnPickDate);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDesc = findViewById(R.id.edtCategoryDesc);
        edtCreateDate = findViewById(R.id.edtCreateDate);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        // Cài đặt sự kiện click
        setupClickListeners();

        // Tự động điền ngày hiện tại
        setTodayDate();
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish()); // Đóng màn hình hiện tại

        btnAddImage.setOnClickListener(v -> selectImage());

        btnPickDate.setOnClickListener(v -> openDatePicker());

        btnAddCategory.setOnClickListener(v -> uploadCategory());
    }

    private void setTodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        edtCreateDate.setText(today);
    }

    private void selectImage() {
        // Mở thư viện ảnh để người dùng chọn
        mGetContent.launch("image/*");
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    edtCreateDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void uploadCategory() {
        String name = edtCategoryName.getText().toString().trim();
        String description = edtCategoryDesc.getText().toString().trim();
        String creationDate = edtCreateDate.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị dialog chờ
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang lưu...");
        progressDialog.show();

        // THAY ĐỔI: Kiểm tra xem người dùng có chọn ảnh không
        if (imageUri != null) {
            // --- TRƯỜNG HỢP 1: CÓ CHỌN ẢNH ---
            // Tải ảnh lên Storage trước
            StorageReference fileRef = storageReference.child("category_images/" + System.currentTimeMillis() + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Lấy URL của ảnh vừa tải lên
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            // Lưu thông tin (bao gồm cả URL ảnh) vào Firestore
                            saveCategoryToFirestore(name, description, creationDate, imageUrl, progressDialog);
                        });
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Lỗi tải ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // --- TRƯỜNG HỢP 2: KHÔNG CHỌN ẢNH ---
            // Bỏ qua bước tải ảnh, lưu thẳng thông tin vào Firestore với imageUrl là rỗng
            saveCategoryToFirestore(name, description, creationDate, "", progressDialog);
        }
    }

    private void saveCategoryToFirestore(String name, String description, String creationDate, String imageUrl, ProgressDialog progressDialog) {
        // Tạo một đối tượng Map để lưu dữ liệu
        Map<String, Object> category = new HashMap<>();
        category.put("name", name);
        category.put("description", description);

        // THAY ĐỔI: Chỉ thêm imageUrl vào Map nếu nó không rỗng
        if (imageUrl != null && !imageUrl.isEmpty()) {
            category.put("imageUrl", imageUrl);
        }

        // Thêm các trường khác nếu cần
        // category.put("creationDate", creationDate);

        // Thêm document mới vào collection "categories"
        db.collection("categories").add(category)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình và quay lại danh sách
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi thêm dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}