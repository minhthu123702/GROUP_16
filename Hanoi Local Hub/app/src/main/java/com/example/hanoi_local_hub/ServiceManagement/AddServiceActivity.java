package com.example.hanoi_local_hub.ServiceManagement; // Thay bằng package của bạn

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.R;
import com.example.hanoi_local_hub.ServiceManagement.ServiceModel; // Đảm bảo bạn có file Model này
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity {

    private static final String TAG = "AddServiceActivity";

    // --- Biến cho Chế độ Sửa ---
    private String editingServiceId = null;
    private ServiceModel currentService;

    // --- Khai báo các thành phần UI ---
    private TextInputEditText edtServiceTitle, edtServiceDescription, edtServicePricing, edtServiceHours, edtServiceCertifications;
    private AutoCompleteTextView actCategory;
    private Button btnAddService, btnCancel;
    private ImageView imgBack;
    private TextView tvHeaderTitle;
    private AutoCompleteTextView actDistrict, actWard;
    private TextInputLayout layoutWard;

    // --- Khai báo các đối tượng Firebase ---
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_service);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupViews();
        setupCategorySpinner();
        setupLocationDropdowns();
        setupListeners();

        // Kiểm tra chế độ Thêm mới hay Chỉnh sửa
        editingServiceId = getIntent().getStringExtra("SERVICE_ID_TO_EDIT");
        if (editingServiceId != null && !editingServiceId.isEmpty()) {
            prepareEditMode();
        } else {
            tvHeaderTitle.setText("Thêm mới dịch vụ");
            btnAddService.setText("Thêm mới");
        }
    }

    private void setupViews() {
        edtServiceTitle = findViewById(R.id.edt_service_title);
        edtServiceDescription = findViewById(R.id.edt_service_description);
        actCategory = findViewById(R.id.act_category);
        edtServicePricing = findViewById(R.id.edt_service_pricing);
        edtServiceHours = findViewById(R.id.edt_service_hours);
        edtServiceCertifications = findViewById(R.id.edt_service_certifications);
        btnAddService = findViewById(R.id.btn_add_service);
        btnCancel = findViewById(R.id.btn_cancel);
        imgBack = findViewById(R.id.img_back_add);
        tvHeaderTitle = findViewById(R.id.tv_header_title);
        actDistrict = findViewById(R.id.act_district);
        actWard = findViewById(R.id.act_ward);
        layoutWard = findViewById(R.id.layout_ward); // TextInputLayout cho Phường/Xã
    }

    private void setupCategorySpinner() {
        // Trong thực tế, bạn nên lấy danh sách này từ Firestore
        String[] categories = new String[]{"Sửa chữa Gia dụng", "Gia sư & Đào tạo", "Thiết kế & Sáng tạo", "Ăn uống & Thực phẩm", "Nhiếp ảnh & Quay phim", "Dịch thuật & Văn phòng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actCategory.setAdapter(adapter);
    }

    private void setupListeners() {
        imgBack.setOnClickListener(v -> finish());
        btnAddService.setOnClickListener(v -> {
            if (validateInput()) {
                saveServiceData();
            }
        });
        btnCancel.setOnClickListener(v -> finish());
    }

    private void prepareEditMode() {
        tvHeaderTitle.setText("Chỉnh sửa dịch vụ");
        btnAddService.setText("Lưu thay đổi");
        loadServiceData(editingServiceId);
    }

    private void loadServiceData(String serviceId) {
        Toast.makeText(this, "Đang tải dữ liệu...", Toast.LENGTH_SHORT).show();
        db.collection("services").document(serviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentService = documentSnapshot.toObject(ServiceModel.class);
                        if (currentService != null) {
                            populateForm(currentService);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy dịch vụ.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void populateForm(ServiceModel service) {
        // Điền dữ liệu cho các trường văn bản như cũ
        edtServiceTitle.setText(service.getTitle());
        edtServiceDescription.setText(service.getDescription());
        actCategory.setText(service.getCategoryName(), false);
        edtServicePricing.setText(service.getPricingInfo());
        edtServiceHours.setText(service.getOperatingHours());
        edtServiceCertifications.setText(service.getCertifications());

        // --- LOGIC MỚI ĐỂ ĐIỀN DỮ LIỆU KHU VỰC ---
        List<String> serviceArea = service.getServiceArea();
        if (serviceArea != null && !serviceArea.isEmpty()) {
            // 1. Lấy và điền tên Quận/Huyện
            String district = serviceArea.get(0);
            actDistrict.setText(district, false);

            // 2. Kích hoạt và điền danh sách Phường/Xã tương ứng
            updateWardDropdown(district);

            // 3. Nếu có dữ liệu Phường/Xã, điền nốt vào
            if (serviceArea.size() > 1) {
                String ward = serviceArea.get(1);
                actWard.setText(ward, false);
            }
        }
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
    // THAY THẾ TOÀN BỘ HÀM CŨ BẰNG HÀM MỚI NÀY
    private void setupLocationDropdowns() {
        List<String> districts = HanoiLocationData.getDistrictList();
        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, districts);
        actDistrict.setAdapter(districtAdapter);

        // Khi người dùng chọn một quận, chỉ cần gọi hàm updateWardDropdown
        actDistrict.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDistrict = (String) parent.getItemAtPosition(position);
            updateWardDropdown(selectedDistrict);
        });
    }
    private void updateWardDropdown(String selectedDistrict) {
        // Xóa lựa chọn phường cũ
        actWard.setText("", false);

        if (selectedDistrict.equals("Toàn bộ khu vực Hà Nội")) {
            // Nếu người dùng chọn "Toàn bộ", hãy vô hiệu hóa dropdown Phường/Xã
            layoutWard.setEnabled(false);
        } else {
            // Nếu người dùng chọn một quận cụ thể
            List<String> wards = HanoiLocationData.getWardList(selectedDistrict);
            ArrayAdapter<String> wardAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, wards);
            actWard.setAdapter(wardAdapter);
            // Kích hoạt lại dropdown Phường/Xã
            layoutWard.setEnabled(true);
        }
    }
    private void saveServiceData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đang xử lý...", Toast.LENGTH_SHORT).show();
        btnAddService.setEnabled(false);
        btnCancel.setEnabled(false);

        // Thu thập dữ liệu từ form
        String title = edtServiceTitle.getText().toString().trim();
        String description = edtServiceDescription.getText().toString().trim();
        String categoryName = actCategory.getText().toString().trim();
        String pricingInfo = edtServicePricing.getText().toString().trim();
        String operatingHours = edtServiceHours.getText().toString().trim();
        String certifications = edtServiceCertifications.getText().toString().trim();
        String district = actDistrict.getText().toString().trim();
        String ward = actWard.getText().toString().trim();

        // 2. Tạo một danh sách (List) để lưu khu vực
        List<String> serviceAreaList = new ArrayList<>();

        // 3. Thêm quận/huyện vào danh sách nếu người dùng có chọn
        if (!district.isEmpty()) {
            serviceAreaList.add(district);
        }

        // 4. Thêm phường/xã vào danh sách nếu người dùng có chọn
        // (Khi chọn "Toàn bộ khu vực Hà Nội", trường ward sẽ rỗng nên điều kiện này không được thực thi)
        if (!ward.isEmpty()) {
            serviceAreaList.add(ward);
        }
        // Nếu là chế độ Sửa, dùng lại object cũ. Nếu là Thêm, tạo object mới.
        ServiceModel serviceToSave = (editingServiceId != null) ? currentService : new ServiceModel();

        // Gán dữ liệu vào đối tượng
        serviceToSave.setTitle(title);
        serviceToSave.setDescription(description);
        serviceToSave.setCategoryName(categoryName);
        serviceToSave.setPricingInfo(pricingInfo);
        serviceToSave.setOperatingHours(operatingHours);
        serviceToSave.setCertifications(certifications);
        serviceToSave.setServiceArea(serviceAreaList);

        // Vì không có ảnh, chúng ta sẽ giữ nguyên danh sách ảnh cũ (nếu là chế độ sửa)
        // hoặc tạo một danh sách rỗng (nếu là chế độ thêm)
        if (editingServiceId == null) {
            serviceToSave.setPortfolioImages(new ArrayList<>());
        }

        if (editingServiceId != null) {
            // --- CHẾ ĐỘ UPDATE ---
            db.collection("services").document(editingServiceId).set(serviceToSave, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnAddService.setEnabled(true);
                        btnCancel.setEnabled(true);
                    });
        } else {
            // --- CHẾ ĐỘ THÊM MỚI ---
            serviceToSave.setProviderId(currentUser.getUid());
            serviceToSave.setStatus("pending");
            serviceToSave.setAverageRating(0.0);
            serviceToSave.setReviewCount(0);

            db.collection("services").add(serviceToSave)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Thêm dịch vụ thành công! Đang chờ duyệt.", Toast.LENGTH_LONG).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi thêm dịch vụ: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        btnAddService.setEnabled(true);
                        btnCancel.setEnabled(true);
                    });
        }
    }
}