package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button btnUserManagement, btnStatistics, btnServiceManagement;
    ImageView ivUserProfile, ivMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Ánh xạ các views theo đúng ID layout mới nhất
        btnUserManagement = findViewById(R.id.buttonUserManagement);
        btnStatistics = findViewById(R.id.buttonStatistics);
        btnServiceManagement = findViewById(R.id.buttonServiceManagement);
        ivUserProfile = findViewById(R.id.ivUser);
        ivMenu = findViewById(R.id.ivMenu);

        // Chuyển đến trang Quản lý người dùng
        btnUserManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, UserGroupActivity.class);
            startActivity(intent);
        });

        // Chuyển đến trang Báo cáo thống kê
        btnStatistics.setOnClickListener(v -> {
            // TODO: thêm logic chuyển sang trang Báo cáo thống kê
        });

        // Chuyển đến trang Quản lý danh mục dịch vụ
        btnServiceManagement.setOnClickListener(v -> {
            // TODO: thêm logic chuyển sang trang Quản lý danh mục dịch vụ
        });

        // Chuyển đến trang Admin profile khi ấn vào avatar người dùng
        ivUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });

        // Hiển thị menu (3 gạch)
        ivMenu.setOnClickListener(v -> {
            // TODO: thêm xử lý hiển thị navigation drawer hoặc menu
        });
    }
}
