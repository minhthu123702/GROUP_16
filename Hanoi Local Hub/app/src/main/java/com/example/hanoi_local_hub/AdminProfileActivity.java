package com.example.hanoi_local_hub;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AdminProfileActivity extends AppCompatActivity {
private ImageButton btnlogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        btnlogout=findViewById(R.id.btnlogout);
        // Xử lý nút back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Xử lý nút đăng xuất
        LinearLayout btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // Xóa session hoặc token nếu có

            // Quay về màn hình đăng nhập, xóa hết backstack
            Intent intent = new Intent(AdminProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // Có thể gọi finishAffinity() nếu muốn đóng hết activity
        });
        btnlogout.setOnClickListener(v -> {
            Intent intent1 = new Intent(AdminProfileActivity.this, AdminManagerCustomersActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
            finish();
        });
    }
}