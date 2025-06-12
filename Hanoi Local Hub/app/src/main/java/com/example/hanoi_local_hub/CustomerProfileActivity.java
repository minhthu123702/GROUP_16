package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerProfileActivity extends AppCompatActivity {

    TextView tvFullName, tvBirth, tvHome, tvEmail, tvPhone, tvCode, tvDate;
    RadioGroup rgGender;
    RadioButton radioMale, radioFemale;
    ImageView btnBack;
    Button btnHide, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Ánh xạ view
        tvFullName = findViewById(R.id.tvFullName);
        tvBirth = findViewById(R.id.tvBirth);
        tvHome = findViewById(R.id.tvHome);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvCode = findViewById(R.id.tvCode);
        tvDate = findViewById(R.id.tvDate);

        rgGender = findViewById(R.id.rgGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        btnBack = findViewById(R.id.btnBack);
        btnHide = findViewById(R.id.btnHide);
        btnDelete = findViewById(R.id.btnDelete);

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

            if ("Nam".equalsIgnoreCase(user.getGender())) {
                radioMale.setChecked(true);
            } else {
                radioFemale.setChecked(true);
            }
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút Ẩn
        btnHide.setOnClickListener(v -> tvPhone.setVisibility(View.GONE));

        // Xử lý nút Xoá
        btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("deleted", true);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
