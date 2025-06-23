package com.example.hanoi_local_hub.auth; // Đảm bảo đúng package

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private TextView backToLoginTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        backToLoginTextView = findViewById(R.id.backToLoginTextView);

        registerButton.setOnClickListener(v -> registerUser());
        backToLoginTextView.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // (Phần kiểm tra đầu vào giữ nguyên)
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.length() < 6) {
            if (TextUtils.isEmpty(email)) emailEditText.setError("Email không được để trống.");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) emailEditText.setError("Vui lòng nhập email hợp lệ.");
            if (password.length() < 6) passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Tạo một đối tượng Map để lưu thông tin người dùng
                            Map<String, Object> user = new HashMap<>();

                            // === THÊM TẤT CẢ CÁC TRƯỜNG VỚI GIÁ TRỊ MẶC ĐỊNH ===

                            // Các trường bắt buộc
                            user.put("uid", userId);
                            user.put("email", email);
                            user.put("role", "user");
                            user.put("providerStatus", false); // Mặc định là false theo yêu cầu

                            // Các trường sẽ được cập nhật sau, khởi tạo là null
                            user.put("name", null); // Có thể để tên mặc định hoặc null
                            user.put("avatarUrl", null);      // Đổi từ avatarResId sang avatarUrl và để là null
                            user.put("birthday", null);
                            user.put("cccd", null);
                            user.put("gender", null);
                            user.put("phone", null);
                            user.put("isOnline", true); // Trạng thái online mặc định là false

                            // =========================================================

                            // Sử dụng UID làm ID cho document để đồng bộ hóa
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                        finish(); // Quay về màn hình đăng nhập
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}