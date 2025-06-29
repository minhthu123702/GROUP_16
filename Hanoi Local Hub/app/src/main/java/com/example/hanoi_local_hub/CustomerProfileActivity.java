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
    private Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        // Ánh xạ giao diện
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
        btnDelete = findViewById(R.id.btnDelete);

        // Nhận dữ liệu từ Intent
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
            } else if ("Nữ".equalsIgnoreCase(user.getGender())) {
                rbFemale.setChecked(true);
            } else {
                rbMale.setChecked(false);
                rbFemale.setChecked(false);
            }
        }

        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(CustomerProfileActivity.this, AdminManagerCustomersActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
            finish();
        });

        // Nút xoá người dùng
        btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("deleted", true);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
