package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserGroupActivity extends AppCompatActivity {

    Button btnQuanLyKhachHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);

        btnQuanLyKhachHang = findViewById(R.id.btnManagerCustomers);

        btnQuanLyKhachHang.setOnClickListener(v -> {
            Intent intent = new Intent(UserGroupActivity.this, AdminManagerCustomersActivity.class);
            startActivity(intent);
        });
    }
}