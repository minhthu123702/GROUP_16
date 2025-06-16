package com.example.hanoi_local_hub.auth;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText otpEditText;
    private Button verifyButton;
    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);
        otpEditText.setOnClickListener(v -> {
            otpEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(otpEditText, InputMethodManager.SHOW_IMPLICIT);
        });
        mAuth = FirebaseAuth.getInstance();

        // Lấy verificationId từ Intent
        verificationId = getIntent().getStringExtra("verificationId");

        verifyButton.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();
            if (otp.isEmpty() || otp.length() < 6) {
                otpEditText.setError("Vui lòng nhập mã OTP gồm 6 chữ số");
                otpEditText.requestFocus();
                return;
            }
            if (verificationId != null) {
                // Tạo credential từ verificationId và mã OTP người dùng nhập
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                // Dùng credential để đăng nhập
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập / Đăng ký thành công
                        Toast.makeText(VerifyOtpActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                        // TODO: Chuyển người dùng đến màn hình chính (MainActivity) hoặc màn hình profile
                        // Intent intent = new Intent(VerifyOtpActivity.this, HomeActivity.class);
                        // startActivity(intent);
                        // finish();
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(VerifyOtpActivity.this, "Xác thực thất bại, mã OTP không đúng.", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
