package com.example.hanoi_local_hub; // Gói của bạn

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText edtCategoryName, edtCategoryDesc;
    private Button btnUpdateCategory;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private String categoryId; // Biến để lưu ID của danh mục đang sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();

        // Ánh xạ Views
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDesc = findViewById(R.id.edtCategoryDesc);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);
        btnBack = findViewById(R.id.btnBack);

        // Lấy dữ liệu đã được gửi từ màn hình trước
        loadDataFromIntent();

        // Cài đặt sự kiện click
        btnBack.setOnClickListener(v -> finish());
        btnUpdateCategory.setOnClickListener(v -> updateCategory());
    }

    private void loadDataFromIntent() {
        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getStringExtra("CATEGORY_ID");
            String name = getIntent().getStringExtra("CATEGORY_NAME");
            String description = getIntent().getStringExtra("CATEGORY_DESC");

            // Hiển thị dữ liệu cũ lên các trường EditText
            edtCategoryName.setText(name);
            edtCategoryDesc.setText(description);
        } else {
            // Trường hợp không nhận được dữ liệu, thông báo lỗi và đóng màn hình
            Toast.makeText(this, "Lỗi: Không có dữ liệu danh mục.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateCategory() {
        String newName = edtCategoryName.getText().toString().trim();
        String newDescription = edtCategoryDesc.getText().toString().trim();

        // Kiểm tra người dùng đã nhập tên mới chưa
        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(this, "Vui lòng không để trống tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị dialog chờ
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang cập nhật...");
        progressDialog.show();

        // Chỉ cập nhật 2 trường name và description
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("description", newDescription);

        // Tìm đến document cần sửa bằng ID và cập nhật
        db.collection("categories").document(categoryId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình sửa và quay về danh sách
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}