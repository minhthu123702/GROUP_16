package com.example.hanoi_local_hub.auth;// Đảm bảo package của bạn là đúng

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private TextView backToLoginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các thành phần giao diện
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        backToLoginTextView = findViewById(R.id.backToLoginTextView);

        registerButton.setOnClickListener(v -> {
            // Gọi hàm để xử lý đăng ký
            registerUser();
        });

        backToLoginTextView.setOnClickListener(v -> {
            // Đóng activity hiện tại để quay về màn hình đăng nhập
            finish();
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // --- Kiểm tra đầu vào ---
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email không được để trống.");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Vui lòng nhập email hợp lệ.");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Mật khẩu không được để trống.");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự.");
            passwordEditText.requestFocus();
            return;
        }

        // --- Bắt đầu tạo tài khoản với Firebase ---
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();

                        // Bạn có thể gửi email xác thực tại đây (tùy chọn)
                        // FirebaseUser user = mAuth.getCurrentUser();
                        // user.sendEmailVerification();

                        // Chuyển người dùng đến màn hình chính hoặc màn hình đăng nhập
                        // Ví dụ: chuyển về màn hình đăng nhập để họ đăng nhập lại
                        finish();

                    } else {
                        // Đăng ký thất bại, hiển thị thông báo lỗi
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
