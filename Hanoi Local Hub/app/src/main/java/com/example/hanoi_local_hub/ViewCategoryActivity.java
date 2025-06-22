package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewCategoryActivity extends AppCompatActivity {

    private TextView tvCategoryName, tvCategoryDesc, tvCreateDate;
    private ImageView imgCategory;
    private ImageButton btnBack;

    private FirebaseFirestore db;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category); // Đúng tên file xml của bạn

        // Ánh xạ View
        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvCategoryDesc = findViewById(R.id.tvCategoryDesc);
        tvCreateDate = findViewById(R.id.tvCreateDate);
        imgCategory = findViewById(R.id.imgCategory);
        btnBack = findViewById(R.id.btnBack);

        db = FirebaseFirestore.getInstance();

        // Lấy ID danh mục từ Intent
        categoryId = getIntent().getStringExtra("CATEGORY_ID");
        if (categoryId == null || categoryId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy ID danh mục!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu từ Firestore
        loadCategoryDetail();
    }

    private void loadCategoryDetail() {
        db.collection("categories").document(categoryId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String desc = documentSnapshot.getString("description");
                        String imageUrl = documentSnapshot.getString("imageUrl");
                        String createDate = documentSnapshot.getString("creationDate");

                        tvCategoryName.setText(name != null ? name : "Tên danh mục");
                        tvCategoryDesc.setText(desc != null ? desc : "Chưa có mô tả");
                        tvCreateDate.setText("Ngày tạo: " + (createDate != null ? createDate : "Không rõ"));

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this).load(imageUrl).into(imgCategory);
                        } else {
                            imgCategory.setImageResource(R.drawable.ic_clean);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
