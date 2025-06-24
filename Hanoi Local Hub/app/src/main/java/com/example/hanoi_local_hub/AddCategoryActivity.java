package com.example.hanoi_local_hub;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private ImageButton btnBack, btnAddImage;
    private EditText edtCategoryName, edtCategoryDesc;
    private Button btnAddCategory;

    private FirebaseFirestore db;
    private Uri imageUri; // Lưu đường dẫn ảnh được chọn

    // Chọn ảnh từ thư viện, chỉ để hiển thị trên app, không lưu lên Firebase
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    btnAddImage.setImageURI(imageUri); // Chỉ hiển thị ảnh lên nút
                    Log.d("AddCategory", "Chọn ảnh thành công, imageUri=" + imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        db = FirebaseFirestore.getInstance();

        btnBack = findViewById(R.id.btnBack);
        btnAddImage = findViewById(R.id.btnAddImage);
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDesc = findViewById(R.id.edtCategoryDesc);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        btnBack.setOnClickListener(v -> finish());
        btnAddImage.setOnClickListener(v -> checkPermissionAndSelectImage());
        btnAddCategory.setOnClickListener(v -> uploadCategory());
    }

    // Xin quyền truy cập ảnh cho Android 13+ (API 33+)
    private void checkPermissionAndSelectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                return;
            }
        }
        selectImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            Toast.makeText(this, "Cần cấp quyền truy cập ảnh để chọn ảnh!", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectImage() {
        mGetContent.launch("image/*");
    }

    private void uploadCategory() {
        String name = edtCategoryName.getText().toString().trim();
        String description = edtCategoryDesc.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang lưu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Không lưu ảnh, chỉ lưu 2 trường vào Firestore
        saveCategoryToFirestore(name, description, progressDialog);
    }

    private void saveCategoryToFirestore(String name, String description, ProgressDialog progressDialog) {
        Map<String, Object> category = new HashMap<>();
        category.put("name", name);
        category.put("description", description);

        db.collection("categories").add(category)
                .addOnSuccessListener(documentReference -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Thêm danh mục thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi thêm dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
