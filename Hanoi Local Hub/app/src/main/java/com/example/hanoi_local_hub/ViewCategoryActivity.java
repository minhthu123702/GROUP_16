package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ViewCategoryActivity extends AppCompatActivity {

    private TextView tvCategoryName, tvCategoryDesc, tvCreateDate;
    private ImageView imgCategory;
    private ImageButton btnBack;

    private String categoryId, categoryName, categoryDesc, categoryDate, categoryImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        // Ánh xạ View
        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvCategoryDesc = findViewById(R.id.tvCategoryDesc);
        tvCreateDate   = findViewById(R.id.tvCreateDate);
        imgCategory    = findViewById(R.id.imgCategory);
        btnBack        = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ Intent
        loadDataFromIntent();

        // Sự kiện click nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDataFromIntent() {
        if (getIntent().getExtras() != null) {
            categoryId      = getIntent().getStringExtra("CATEGORY_ID");
            categoryName    = getIntent().getStringExtra("CATEGORY_NAME");
            categoryDesc    = getIntent().getStringExtra("CATEGORY_DESC");
            categoryDate    = getIntent().getStringExtra("CATEGORY_DATE");
            categoryImageUrl = getIntent().getStringExtra("CATEGORY_IMAGE_URL");

            // Hiển thị dữ liệu lên các trường TextView
            tvCategoryName.setText(categoryName != null ? categoryName : "Tên danh mục");
            tvCategoryDesc.setText(categoryDesc != null ? categoryDesc : "Chưa có mô tả");
            tvCreateDate.setText("Ngày tạo: " + (categoryDate != null ? categoryDate : "Không rõ"));

            // Hiển thị ảnh (nếu có truyền link ảnh)
            if (categoryImageUrl != null && !categoryImageUrl.isEmpty()) {
                Glide.with(this).load(categoryImageUrl).into(imgCategory);
            } else {
                imgCategory.setImageResource(R.drawable.ic_clean);
            }
        } else {
            Toast.makeText(this, "Lỗi: Không có dữ liệu danh mục.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
