package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class UserGroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        Button btnSupplier = findViewById(R.id.btnSupplier);
        Button btnCustomer = findViewById(R.id.btnCustomer);

        btnSupplier.setOnClickListener(v -> {
            // TODO: Chuyển sang activity quản lý nhà cung cấp
        });

        btnCustomer.setOnClickListener(v -> {
            // Chuyển sang activity quản lý khách hàng
            Intent intent = new Intent(UserGroupActivity.this, MainAdmin.class);
            startActivity(intent);
        });
    }
}
