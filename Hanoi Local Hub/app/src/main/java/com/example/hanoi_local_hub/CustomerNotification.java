package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class CustomerNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_notification);

        // Danh sách thông báo mẫu
        List<String> notiList = Arrays.asList(
                "Ứng dụng đang cần cập nhật",
                "Bạn vừa đánh giá dịch vụ Gia sư Tiếng Anh",
                "Ứng dụng đang cần cập nhật"
        );

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NotificationAdapter adapter = new NotificationAdapter(notiList);
        recyclerView.setAdapter(adapter);

        // Thanh điều hướng dưới
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_notification);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainCustomer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notification) {
                // Đang ở trang này rồi
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, CustomerProfile.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
