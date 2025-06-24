package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainAdminActivity extends AppCompatActivity {

    Button btnUserManagement, btnStatistics, btnServiceManagement;
    ImageView ivUserProfile, ivMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        btnUserManagement = findViewById(R.id.buttonUserManagement);
        btnStatistics = findViewById(R.id.buttonStatistics);          // Chưa sử dụng
        btnServiceManagement = findViewById(R.id.buttonServiceManagement);  // Chưa sử dụng
        ivUserProfile = findViewById(R.id.ivUser);                    // Chưa sử dụng
        ivMenu = findViewById(R.id.ivMenu);

        // Mở menu khi bấm nút 3 gạch
        ivMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Xử lý chọn các mục trong menu trượt (chỉ xử lý quản lý người dùng)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers(); // Đóng menu sau khi chọn
                int id = item.getItemId();

                if (id == R.id.nav_user) {
                    startActivity(new Intent(MainAdminActivity.this, UserGroupActivity.class));
                    return true;
                }

                // Các mục này bạn sẽ xử lý sau khi tạo các Activity tương ứng
                else if (id == R.id.nav_services) {
                    startActivity(new Intent(MainAdminActivity.this, ServiceManagementActivity.class));
                    return true;
                } else if (id == R.id.nav_stats) {
                    startActivity(new Intent(MainAdminActivity.this, StatisticsActivity.class));
                    return true;
                }

                return false;
            }
        });

        // Button chức năng chính: Quản lý người dùng
        btnUserManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainAdminActivity.this, UserGroupActivity.class);
            startActivity(intent);
        });
        ivUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainAdminActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });

        // Các nút chưa dùng sẽ xử lý sau khi tạo Activity tương ứng
        btnStatistics.setOnClickListener(v -> {
            startActivity(new Intent(MainAdminActivity.this, StatisticsActivity.class));
        });

        btnServiceManagement.setOnClickListener(v -> {
            startActivity(new Intent(MainAdminActivity.this, ServiceManagementActivity.class));
        });

//        ivUserProfile.setOnClickListener(v -> {
//            Intent intent = new Intent(MainMenuActivity.this, AdminProfileActivity.class);
//            startActivity(intent);
//        });
    }
}
