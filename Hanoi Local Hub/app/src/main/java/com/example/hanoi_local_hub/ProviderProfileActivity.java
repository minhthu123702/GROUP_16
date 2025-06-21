package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProviderProfileActivity extends AppCompatActivity {

    private EditText edtFullName, edtBirth, edtHome, edtEmail, edtPhone, edtCode, edtService, edtApproveDate, edtDegree;
    private RadioButton rbMale, rbFemale;
    private ImageView btnBack, imgAvatar;
    private TextView tvName;
    private Button btnDelete, btnHide, btnCancelService;

    private boolean isPhoneVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        // Ánh xạ giao diện
        edtFullName = findViewById(R.id.tvFullName);
        edtBirth = findViewById(R.id.tvBirth);
        edtHome = findViewById(R.id.tvHome);
        edtEmail = findViewById(R.id.tvEmail);
        edtPhone = findViewById(R.id.tvPhone);
        edtCode = findViewById(R.id.tvCode);
        edtService = findViewById(R.id.tvService);
        edtApproveDate = findViewById(R.id.tvApproveDate);
        edtDegree = findViewById(R.id.edtDegree);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
        btnDelete = findViewById(R.id.btnDelete);
        btnHide = findViewById(R.id.btnHide);
        btnCancelService = findViewById(R.id.btnCancelService);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        if (user != null) {
            edtFullName.setText(user.getName());
            edtBirth.setText(user.getBirth());
            edtHome.setText(user.getAddress());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            edtCode.setText(user.getCode());
            edtService.setText(user.getService());
            edtApproveDate.setText(user.getApproveDate());
            edtDegree.setText(user.getDegreeInfo());
            tvName.setText(user.getName());
            imgAvatar.setImageResource(user.getAvatarResId());

            if ("Nam".equalsIgnoreCase(user.getGender())) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }

        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại trang trước (AdminManagerProvidersActivity)
        });

        // Nút ẩn/hiện số điện thoại
        btnHide.setOnClickListener(v -> {
            if (edtCode.getVisibility() == View.VISIBLE) {
                edtCode.setVisibility(View.GONE);
                btnHide.setText("Hiện");
            } else {
                edtCode.setVisibility(View.VISIBLE);
                btnHide.setText("Ẩn");
            }
        });

        // Nút xoá người dùng
        btnDelete.setOnClickListener(v -> {
            // Có thể gọi API xoá ở đây, hoặc gửi kết quả về activity trước
            Intent result = new Intent();
            result.putExtra("deleted", true);
            setResult(RESULT_OK, result);
            finish();
        });

        // Nút huỷ đăng kí dịch vụ
        btnCancelService.setOnClickListener(v -> {
            edtService.setText(""); // Xoá dịch vụ (tuỳ nghiệp vụ bạn có thể show dialog xác nhận)
        });
    }

    // Nếu muốn xử lý nút back cứng trên điện thoại
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Nếu cần chuyển về activity cụ thể, bỏ finish() ở btnBack và thêm ở đây:
        // Intent intent = new Intent(this, AdminManagerProvidersActivity.class);
        // startActivity(intent);
        // finish();
    }
}
