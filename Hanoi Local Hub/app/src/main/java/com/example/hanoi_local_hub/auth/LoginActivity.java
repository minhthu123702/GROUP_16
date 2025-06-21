// Đảm bảo bạn đang sử dụng đúng package của dự án
package com.example.hanoi_local_hub.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.MainActivity; // Giả sử đây là MainCustomerActivity
import com.example.hanoi_local_hub.MainMenuActivity; // Activity cho Admin
import com.example.hanoi_local_hub.R;
import com.example.hanoi_local_hub.MainCustomer; // Activity cho User
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView register;
    private ProgressBar progressBar;

    // Khai báo các đối tượng Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo tên file layout của bạn là chính xác
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View từ file XML
        edtEmail = findViewById(R.id.editTextText);
        edtPassword = findViewById(R.id.editTextNumberPassword);
        btnLogin = findViewById(R.id.button);

        // Bắt sự kiện click cho nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            loginUser();
        });

        // Bắt sự kiện click cho chữ "Chưa có tài khoản? Đăng ký"
        TextView register = findViewById(R.id.register); // Sử dụng ID từ file XML của bạn

// 2. Gán sự kiện click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một Intent để mở RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); // Thay LoginActivity.this bằng tên Activity hiện tại của bạn
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // --- CHỨC NĂNG DUY TRÌ ĐĂNG NHẬP ---
        // Kiểm tra xem người dùng đã đăng nhập từ trước chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập, kiểm tra vai trò và chuyển thẳng đến màn hình chính
            checkUserRole(currentUser.getUid());
        }
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Kiểm tra đầu vào
        if (email.isEmpty()) {
            edtEmail.setError("Email không được để trống");
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Vui lòng nhập email hợp lệ.");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Mật khẩu không được để trống.");
            edtPassword.requestFocus();
            return;
        }

        // Hiển thị vòng xoay tiến trình

        // BƯỚC 1: XÁC THỰC BẰNG FIREBASE AUTHENTICATION
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, lấy UID
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // BƯỚC 2: KIỂM TRA VAI TRÒ TỪ FIRESTORE
                            checkUserRole(user.getUid());
                        }
                    } else {
                        // Đăng nhập thất bại
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserRole(String uid) {
        // Tham chiếu đến document của người dùng trong collection 'users'
        DocumentReference docRef = db.collection("users").document(uid);

        // Lấy dữ liệu document
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Lấy giá trị của trường 'role'
                    String role = document.getString("role");

                    // BƯỚC 3: ĐIỀU HƯỚNG DỰA TRÊN VAI TRÒ
                    if ("admin".equals(role)) {
                        // Nếu là admin, chuyển đến MainMenuActivity
                        Toast.makeText(this, "Chào mừng Admin!", Toast.LENGTH_SHORT).show();
                        navigateTo(MainMenuActivity.class);
                    } else {
                        // Mặc định là user, chuyển đến MainCustomerActivity
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        navigateTo(MainCustomer.class);
                    }
                } else {
                    // Trường hợp hiếm gặp: Tài khoản tồn tại trong Auth nhưng không có trong Firestore
                    Toast.makeText(LoginActivity.this, "Lỗi: Không tìm thấy dữ liệu người dùng.", Toast.LENGTH_LONG).show();
                    mAuth.signOut(); // Đăng xuất để tránh bị kẹt
                }
            } else {
                // Lỗi khi truy vấn Firestore (ví dụ: mất mạng)
                Toast.makeText(LoginActivity.this, "Lỗi khi lấy dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Hàm tiện ích để chuyển Activity và xóa back stack
     * @param cls Class của Activity muốn chuyển đến
     */
    private void navigateTo(Class<?> cls) {
        Intent intent = new Intent(LoginActivity.this, cls);
        // Các flag này giúp người dùng không thể nhấn nút Back để quay lại màn hình Login
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Đóng LoginActivity
    }
}