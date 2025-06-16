package com.example.hanoi_local_hub.auth; // Thay thế bằng package của bạn

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText phoneEditText;
    private Button sendCodeButton;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneEditText = findViewById(R.id.phoneEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);
        Button backButton = findViewById(R.id.backButton);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        sendCodeButton.setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();

            // Kiểm tra xem số điện thoại có hợp lệ không
            // Thêm mã quốc gia (+84) nếu người dùng chưa nhập
            if (phoneNumber.isEmpty() || phoneNumber.length() < 9) {
                phoneEditText.setError("Số điện thoại không hợp lệ");
                phoneEditText.requestFocus();
                return;
            }
            if (!phoneNumber.startsWith("+84")) {
                phoneNumber = "+84" + phoneNumber.substring(phoneNumber.length() - 9);
            }

            // Bắt đầu quá trình xác thực số điện thoại
            sendVerificationCode(phoneNumber);
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void sendVerificationCode(String phoneNumber) {
        // Hiển thị loading, vô hiệu hóa nút bấm
        sendCodeButton.setEnabled(false);
        Toast.makeText(this, "Đang gửi mã xác thực...", Toast.LENGTH_SHORT).show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Số điện thoại cần xác thực
                        .setTimeout(60L, TimeUnit.SECONDS) // Thời gian chờ
                        .setActivity(this)                 // Activity hiện tại
                        .setCallbacks(mVerificationCallbacks) // Callback xử lý kết quả
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mVerificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // Callback này được gọi khi xác thực thành công tự động (hiếm gặp)
            // Hoặc sau khi người dùng nhập đúng mã OTP ở màn hình tiếp theo
            Log.d(TAG, "onVerificationCompleted:" + credential);
            // Bạn có thể đăng nhập người dùng tại đây
            // signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // Callback này được gọi khi có lỗi xảy ra
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(RegisterActivity.this, "Xác thực thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Kích hoạt lại nút bấm
            sendCodeButton.setEnabled(true);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // Callback này được gọi khi mã OTP đã được gửi thành công
            Log.d(TAG, "onCodeSent:" + verificationId);
            Toast.makeText(RegisterActivity.this, "Đã gửi mã xác thực.", Toast.LENGTH_SHORT).show();

            // Lưu lại verificationId và token để sử dụng ở màn hình xác thực OTP
            mVerificationId = verificationId;
            mResendToken = token;

            // Kích hoạt lại nút bấm
            sendCodeButton.setEnabled(true);

            // Chuyển sang màn hình nhập OTP
            // Bạn cần tạo một Activity mới (ví dụ: VerifyOtpActivity)
            Intent intent = new Intent(RegisterActivity.this, VerifyOtpActivity.class);
            intent.putExtra("verificationId", mVerificationId);
            startActivity(intent);
        }
    };
}
