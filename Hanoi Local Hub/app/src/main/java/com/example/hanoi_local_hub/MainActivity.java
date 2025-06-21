package com.example.hanoi_local_hub; // Hãy đảm bảo đây là package của bạn

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hanoi_local_hub.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Khai báo biến cho Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //
        // --- Code để xử lý các chức năng chính của MainActivity của bạn sẽ nằm ở đây ---
        // Ví dụ: setup RecyclerView, lấy dữ liệu dịch vụ, v.v.
        //
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Kiểm tra xem người dùng hiện tại đã đăng nhập hay chưa khi Activity bắt đầu
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Nếu người dùng chưa đăng nhập, chuyển họ đến màn hình Login
            sendUserToLoginActivity();
        }
        // Nếu đã đăng nhập thì không làm gì cả, người dùng sẽ ở lại màn hình này.
    }

    /**
     * Hàm để tạo Intent và chuyển người dùng đến LoginActivity
     */
    private void sendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        // Cờ này sẽ xóa hết các activity trước đó khỏi stack, tạo một task mới.
        // Ngăn người dùng nhấn back để quay lại màn hình chính khi chưa đăng nhập.
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        // Đóng MainActivity hiện tại
        finish();
    }
}