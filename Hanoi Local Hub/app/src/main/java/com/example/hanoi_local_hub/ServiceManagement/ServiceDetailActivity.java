package com.example.hanoi_local_hub.ServiceManagement;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.hanoi_local_hub.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity {

    private ImageView imgServiceDetail, imgBack;
    private TextView tvServiceName, tvServiceCategory, tvServiceDescription, tvServicePricing, tvServiceHours, tvServiceArea, tvContactPhone, tvContactEmail, tvRatingScore, tvReviewCount;

    private FirebaseFirestore db;
    private String serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_services);

        db = FirebaseFirestore.getInstance();

        initViews();
        setupToolbar();

        serviceId = getIntent().getStringExtra("SERVICE_ID");

        if (serviceId != null && !serviceId.isEmpty()) {
            loadServiceDetails();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin dịch vụ.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        // Ánh xạ tất cả các view đã được đặt ID trong XML
        imgBack = findViewById(R.id.img_detail_back);
        imgServiceDetail = findViewById(R.id.img_detail_service_image);
        tvServiceName = findViewById(R.id.tv_detail_service_name);
        tvServiceCategory = findViewById(R.id.tv_detail_category);
        tvServiceDescription = findViewById(R.id.tv_detail_description);
        tvServicePricing = findViewById(R.id.tv_detail_pricing);
        tvServiceHours = findViewById(R.id.tv_detail_hours);
        tvServiceArea = findViewById(R.id.tv_detail_area);
        tvContactPhone = findViewById(R.id.tv_detail_phone);
        tvContactEmail = findViewById(R.id.tv_detail_email);
        tvRatingScore = findViewById(R.id.tv_detail_rating_score);
        tvReviewCount = findViewById(R.id.tv_detail_review_count);
    }

    private void setupToolbar() {
        // Xử lý nút back
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadServiceDetails() {
        db.collection("services").document(serviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ServiceModel service = documentSnapshot.toObject(ServiceModel.class);
                        if (service != null) {
                            populateUI(service);
                        }
                    } else {
                        Toast.makeText(this, "Dịch vụ không còn tồn tại.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải chi tiết dịch vụ.", Toast.LENGTH_SHORT).show();
                });
    }

    private void populateUI(ServiceModel service) {
        // Hiển thị dữ liệu lên các View
        tvServiceName.setText(service.getTitle());
        tvServiceCategory.setText(service.getCategoryName());
        tvServiceDescription.setText(service.getDescription());
        tvServicePricing.setText(service.getPricingInfo());
        tvServiceHours.setText(service.getOperatingHours());

        // Format lại khu vực hiển thị cho đẹp
        List<String> areaList = service.getServiceArea();
        if (areaList != null && !areaList.isEmpty()) {
            tvServiceArea.setText(String.join(", ", areaList));
        }

        // TODO: Bạn cần thêm các trường contactPhone và contactEmail vào ServiceModel
        // tvContactPhone.setText(service.getContactPhone());
        // tvContactEmail.setText(service.getContactEmail());

        // Hiển thị đánh giá
        tvRatingScore.setText(String.format(Locale.US, "%.1f", service.getAverageRating()));
        tvReviewCount.setText(String.format(Locale.US, "%d người đánh giá", service.getReviewCount()));

        // Dùng Glide để tải ảnh

    }
}