package com.example.hanoi_local_hub;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button btnQuanLyNguoiDung, btnBaoCaoThongKe, btnQuanLyDichVu;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard); // Layout bạn gửi lúc đầu tiên

        // Ánh xạ các button từ layout
        btnQuanLyNguoiDung = findViewById(R.id.button8);
        btnBaoCaoThongKe = findViewById(R.id.button9);
        btnQuanLyDichVu = findViewById(R.id.button7);
        imgProfile = findViewById(R.id.imageView2);

        // Ấn Quản lý người dùng sang UserGroupActivity
        btnQuanLyNguoiDung.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, UserGroupActivity.class);
            startActivity(intent);
        });

        // Ấn Báo cáo thống kê (nếu bạn muốn xử lý sau)
        btnBaoCaoThongKe.setOnClickListener(v -> {
            // TODO: thêm xử lý màn hình báo cáo thống kê
        });

        // Ấn Quản lý danh mục dịch vụ (nếu bạn muốn xử lý sau)
        btnQuanLyDichVu.setOnClickListener(v -> {
            // TODO: thêm xử lý màn hình danh mục dịch vụ
        });

        // Ấn vào icon profile (avatar), sang AdminProfileActivity
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, AdminProfileActivity.class);
            startActivity(intent);
        });
    }
}
