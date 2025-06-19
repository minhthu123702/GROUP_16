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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private TextView backToLoginTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Thêm biến cho Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth và Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
                        // Đăng ký thành công, bây giờ lưu thông tin người dùng vào Firestore
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();

                            // Tạo một đối tượng Map để lưu thông tin
                            Map<String, Object> user = new HashMap<>();
                            user.put("uid", userId);
                            user.put("email", email);
                            // Bạn có thể thêm các trường mặc định khác ở đây
                            // user.put("name", "New User");
                            // user.put("createdAt", FieldValue.serverTimestamp());

                            // Lưu thông tin người dùng vào collection "users"
                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        // Lưu vào database thành công
                                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                        // Chuyển về màn hình đăng nhập
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Lưu vào database thất bại
                                        Toast.makeText(RegisterActivity.this, "Lỗi khi lưu thông tin: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        // Đăng ký thất bại, hiển thị thông báo lỗi
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thất bại: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
