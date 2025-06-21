// Đảm bảo bạn đang sử dụng đúng package của dự án
package com.example.hanoi_local_hub.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.MainMenuActivity;
import com.example.hanoi_local_hub.R;
import com.example.hanoi_local_hub.MainCustomer; // Chú ý: Đảm bảo tên class này chính xác

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    // Khai báo các đối tượng Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View từ file XML
        editTextEmail = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextNumberPassword);
        buttonLogin = findViewById(R.id.button);
        textViewRegister = findViewById(R.id.register);

        // Bắt sự kiện click cho nút Đăng nhập
        buttonLogin.setOnClickListener(v -> loginUser());

        // Bắt sự kiện click cho TextView Đăng ký
        textViewRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    // KHÔNG CÒN onStart() ĐỂ TỰ ĐỘNG ĐĂNG NHẬP NỮA

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Kiểm tra đầu vào
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()) {
            if (email.isEmpty()) editTextEmail.setError("Email không được để trống");
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) editTextEmail.setError("Vui lòng nhập email hợp lệ.");
            if (password.isEmpty()) editTextPassword.setError("Mật khẩu không được để trống.");
            return;
        }

        Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();

        // Bước 1: Xác thực
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Bước 2: Phân quyền nếu xác thực thành công
                            checkUserRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String role = document.getString("role");
                    if ("admin".equals(role)) {
                        Toast.makeText(this, "Chào mừng Admin!", Toast.LENGTH_SHORT).show();
                        navigateTo(MainMenuActivity.class);
                    } else {
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        navigateTo(MainCustomer.class);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi: Không tìm thấy dữ liệu người dùng.", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(LoginActivity.this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}