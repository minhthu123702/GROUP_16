package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerProfileActivity extends AppCompatActivity {

    private EditText tvFullName, tvBirth, tvHome, tvEmail, tvPhone, tvCode, tvDate;
    private RadioButton rbMale, rbFemale;
    private ImageView btnBack, imgAvatar;
    private TextView tvName;
    private Button btnHide, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Ánh xạ các thành phần giao diện
        tvFullName = findViewById(R.id.tvFullName);
        tvBirth = findViewById(R.id.tvBirth);
        tvHome = findViewById(R.id.tvHome);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvCode = findViewById(R.id.tvCode);
        tvDate = findViewById(R.id.tvDate);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
//        btnHide = findViewById(R.id.btnHide);
//        btnDelete = findViewById(R.id.btnDelete);

        // Nhận dữ liệu User từ Intent
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        if (user != null) {
            tvFullName.setText(user.getName());
            tvBirth.setText(user.getBirth());
            tvHome.setText(user.getAddress());
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
            tvCode.setText(user.getCode());
            tvDate.setText(user.getRegisterDate());
            tvName.setText(user.getName());
            imgAvatar.setImageResource(user.getAvatarResId());

            if ("Nam".equalsIgnoreCase(user.getGender())) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút Ẩn (ẩn số điện thoại)
        btnHide.setOnClickListener(v -> {
            if (tvPhone.getVisibility() == View.VISIBLE) {
                tvPhone.setVisibility(View.GONE);
                btnHide.setText("Hiện");
            } else {
                tvPhone.setVisibility(View.VISIBLE);
                btnHide.setText("Ẩn");
            }
        });

        // Xử lý nút Xoá (trả kết quả xoá về activity trước)
        btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("deleted", true);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
