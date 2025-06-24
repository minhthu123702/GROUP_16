package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle; // Thêm import này
import android.widget.Button;

import androidx.annotation.Nullable; // Thêm import này
import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {
    // Trong file MainAdminActivity.java

    // ...
    Button btnStatCategory, btnStatArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics); // Layout của bạn

        btnStatCategory = findViewById(R.id.btnStatCategory); // ID của nút thống kê danh mục
        btnStatArea = findViewById(R.id.btnStatArea);       // ID của nút thống kê khu vực

        // Sự kiện click cho nút THỐNG KÊ DANH MỤC
        btnStatCategory.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, StatCategoryActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho nút THỐNG KÊ KHU VỰC
        btnStatArea.setOnClickListener(v -> {
            Intent intent = new Intent(StatisticsActivity.this, StatAreaActivity.class);
            startActivity(intent);
        });

        // Bạn có thể xóa code cho btnStatistics cũ đi
    }
// ...
}