package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserGroupActivity extends AppCompatActivity {

    Button btnManagerCustomers, btnManagerSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        btnManagerCustomers = findViewById(R.id.btnManagerCustomers);
        btnManagerSupplier = findViewById(R.id.btnManagerSupplier);

        // Mở trang quản lý khách hàng
        btnManagerCustomers.setOnClickListener(v -> {
            Intent intent = new Intent(UserGroupActivity.this, AdminManagerCustomersActivity.class);
            startActivity(intent);
        });

        // Mở trang quản lý nhà cung cấp (bạn thêm activity sau)
        btnManagerSupplier.setOnClickListener(v -> {
            Intent intent = new Intent(UserGroupActivity.this, AdminManagerProvidersActivity.class);
            startActivity(intent);
        });
    }
}
