package com.example.hanoi_local_hub.ServiceManagement; // Thay bằng package của bạn

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

// import com.bumptech.glide.Glide; // Tạm thời không cần
import com.example.hanoi_local_hub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
// import com.google.firebase.storage.FirebaseStorage; // Tạm thời không cần
// import com.google.firebase.storage.StorageReference; // Tạm thời không cần

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity {

    private static final String TAG = "AddServiceActivity";

    // Khai báo các thành phần UI
    private TextInputEditText edtServiceTitle, edtServiceDescription, edtServicePricing, edtServiceHours, edtServiceCertifications;
    private AutoCompleteTextView actCategory;
    private Button btnAddService, btnCancel;
    private ImageView imgBack;
    // private FloatingActionButton fabAddImage; // Tạm ẩn
    // private ImageView imagePreview1, imagePreview2; // Tạm ẩn


    // Khai báo các đối tượng Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    // private FirebaseStorage storage; // Tạm ẩn

    // Dữ liệu cho việc chọn ảnh
    // private List<Uri> selectedImageUris = new ArrayList<>(); // Tạm ẩn
    // private ActivityResultLauncher<String[]> imagePickerLauncher; // Tạm ẩn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // storage = FirebaseStorage.getInstance(); // Tạm ẩn

        setupViews();
        setupCategorySpinner();
        // setupImagePicker(); // Tạm ẩn
        setupListeners();
    }

    private void setupViews() {
        edtServiceTitle = findViewById(R.id.edt_service_title);
        edtServiceDescription = findViewById(R.id.edt_service_description);
        actCategory = findViewById(R.id.act_category);
        edtServicePricing = findViewById(R.id.edt_service_pricing);
        edtServiceHours = findViewById(R.id.edt_service_hours);
        edtServiceCertifications = findViewById(R.id.edt_service_certifications);

        // fabAddImage = findViewById(R.id.fab_add_image);       // Tạm ẩn
        // imagePreview1 = findViewById(R.id.imagePreview1); // Tạm ẩn
        // imagePreview2 = findViewById(R.id.imagePreview2); // Tạm ẩn

        btnAddService = findViewById(R.id.btn_add_service);
        btnCancel = findViewById(R.id.btn_cancel);
        imgBack = findViewById(R.id.img_back_add);
    }

    private void setupCategorySpinner() {
        String[] categories = new String[]{"Sửa chữa Gia dụng", "Gia sư & Đào tạo", "Thiết kế & Sáng tạo", "Ăn uống & Thực phẩm", "Nhiếp ảnh & Quay phim", "Dịch thuật & Văn phòng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actCategory.setAdapter(adapter);
    }

    /*
    private void setupImagePicker() { ... }
    private void updateImagePreviews() { ... }
    */

    private void setupListeners() {
        imgBack.setOnClickListener(v -> finish());
        // fabAddImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*")); // Tạm ẩn
        btnAddService.setOnClickListener(v -> {
            if (validateInput()) {
                uploadServiceData();
            }
        });
        btnCancel.setOnClickListener(v -> finish());
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(edtServiceTitle.getText())) {
            edtServiceTitle.setError("Tiêu đề không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(edtServiceDescription.getText())) {
            edtServiceDescription.setError("Mô tả không được để trống");
            return false;
        }
        if (TextUtils.isEmpty(actCategory.getText())) {
            actCategory.setError("Vui lòng chọn danh mục");
            return false;
        }
        return true;
    }

    private void uploadServiceData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang lưu dịch vụ...", Toast.LENGTH_LONG).show();
        btnAddService.setEnabled(false);
        btnCancel.setEnabled(false);

        // Luôn gọi trực tiếp hàm lưu với danh sách ảnh rỗng
        saveServiceToFirestore(new ArrayList<>());
    }

    /*
    private void uploadImagesAndThenSaveService() { ... }
    */

    private void saveServiceToFirestore(List<String> imageUrls) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String title = edtServiceTitle.getText().toString().trim();
        String description = edtServiceDescription.getText().toString().trim();
        String categoryName = actCategory.getText().toString().trim();
        String pricingInfo = edtServicePricing.getText().toString().trim();
        String operatingHours = edtServiceHours.getText().toString().trim();
        String certifications = edtServiceCertifications.getText().toString().trim();

        ServiceModel newService = new ServiceModel();
        newService.setTitle(title);
        newService.setDescription(description);
        newService.setCategoryName(categoryName);
        newService.setPricingInfo(pricingInfo);
        newService.setOperatingHours(operatingHours);
        newService.setCertifications(certifications);
        newService.setPortfolioImages(imageUrls);
        newService.setProviderId(currentUser.getUid());
        newService.setStatus("pending");
        newService.setAverageRating(0.0);
        newService.setReviewCount(0);

        db.collection("services").add(newService)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm dịch vụ thành công! Đang chờ duyệt.", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi thêm dịch vụ: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    btnAddService.setEnabled(true);
                    btnCancel.setEnabled(true);
                });
    }
}