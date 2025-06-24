package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class ServiceDetailActivity extends AppCompatActivity {

    private String providerPhone = "";
    private String providerEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        ImageView imageService = findViewById(R.id.imageService);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textDesc = findViewById(R.id.textDesc);
        TextView textCategory = findViewById(R.id.textCategory);
        TextView textArea = findViewById(R.id.textArea);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textTime = findViewById(R.id.textTime);
        TextView textContact = findViewById(R.id.textContact);
        TextView textRating = findViewById(R.id.textRating);
        TextView textCount = findViewById(R.id.textCount);

        String serviceId = getIntent().getStringExtra("serviceId");
        if (serviceId == null) {
            Toast.makeText(this, "Không tìm thấy dịch vụ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn dịch vụ theo serviceId
        db.collection("services").document(serviceId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) {
                        Toast.makeText(this, "Dịch vụ không tồn tại!", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    ServiceItem service = doc.toObject(ServiceItem.class);
                    if (service == null) return;

                    textTitle.setText(service.getTitle());
                    textDesc.setText(service.getDescription());
                    textCategory.setText(service.getCategoryName());
                    // Khu vực: nối các phần tử serviceArea thành chuỗi
                    textArea.setText(service.getServiceArea() != null ? String.join(", ", service.getServiceArea()) : "");
                    textPrice.setText(service.getPricingInfo());
                    textTime.setText(service.getOperatingHours());
                    textContact.setText(""); // Sẽ hiển thị dưới dạng số lượt đánh giá, hoặc bạn tuỳ ý
                    textRating.setText(service.getAverageRating() + " ★");
                    textCount.setText(service.getReviewCount() + " người đánh giá");

                    // Ảnh: Lấy ảnh đầu tiên của portfolioImages
                    if (service.getPortfolioImages() != null && !service.getPortfolioImages().isEmpty()) {
                        Glide.with(this)
                                .load(service.getPortfolioImages().get(0))
                                .placeholder(R.drawable.image)
                                .into(imageService);
                    } else {
                        imageService.setImageResource(R.drawable.image);
                    }

                    // Lấy thông tin liên hệ từ providerId → truy vấn bảng users
                    if (service.getProviderId() != null && !service.getProviderId().isEmpty()) {
                        db.collection("users").document(service.getProviderId())
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    if (userDoc.exists()) {
                                        providerPhone = userDoc.getString("phone");
                                        providerEmail = userDoc.getString("email");
                                    }
                                });
                    }
                });

        // Back button
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nút hiện dialog liên hệ
        Button btnContact = findViewById(R.id.btnContact);
        btnContact.setOnClickListener(v -> showContactDialog());
    }

    private void showContactDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_contact_info, null);
        TextView txtContactDetail = dialogView.findViewById(R.id.txtContactDetail);

        String contactInfo = "• Thông tin liên hệ:\n";
        contactInfo += "Số điện thoại: " + (providerPhone != null ? providerPhone : "Chưa cập nhật") + "\n";
        contactInfo += "Email: " + (providerEmail != null ? providerEmail : "Chưa cập nhật");

        txtContactDetail.setText(contactInfo);

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .show();
    }
}
