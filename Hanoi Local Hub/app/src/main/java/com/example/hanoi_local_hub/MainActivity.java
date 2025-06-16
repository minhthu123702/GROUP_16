package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.auth.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Đây là layout bạn vừa gửi

        // Ánh xạ
        edtEmail = findViewById(R.id.editTextText);
        edtPassword = findViewById(R.id.editTextNumberPassword);
        btnLogin = findViewById(R.id.button);
        // Xử lý sự kiện click nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.equals("admin@gmail.com") && password.equals("123456")) {
                // Admin account
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            } else if (email.equals("user") && password.equals("123")) {
                Intent intent = new Intent(MainActivity.this, MainCustomer.class);
                startActivity(intent);
                finish();
            } {
                Toast.makeText(MainActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });
        TextView register = findViewById(R.id.register); // Sử dụng ID từ file XML của bạn

// 2. Gán sự kiện click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để mở RegisterActivity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class); // Thay LoginActivity.this bằng tên Activity hiện tại của bạn
                startActivity(intent);
            }
        });
    }

}
