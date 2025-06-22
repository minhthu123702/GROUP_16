package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerProfile extends AppCompatActivity {

    private EditText edtUserId, edtCCCD, edtEmail, edtPhone, edtBirthday;
    private TextView txtName;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private TextView btnLogout, btnManageService;
    private ImageButton btnEdit;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // Khởi tạo view
        txtName = findViewById(R.id.txtName);
        edtUserId = findViewById(R.id.edtUserId);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthday = findViewById(R.id.edtBirthday);
        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageService = findViewById(R.id.btnManageService);
        btnEdit = findViewById(R.id.btnEdit);
        imgAvatar = findViewById(R.id.imgAvatar);

        // Gán dữ liệu ví dụ (sau này lấy từ Firebase/UserProfile)
        txtName.setText("Trần Đăng Dương");
        edtUserId.setText("111111");
        edtCCCD.setText("001200356741");
        edtEmail.setText("customer@gmail.com");
        edtPhone.setText("0123456789");
        edtBirthday.setText("19/12/2000");
        radioMale.setChecked(true);

        // Đặt EditText chỉ đọc
        setEditable(false);

        // Sửa thông tin
        btnEdit.setOnClickListener(v -> {
            boolean editable = !edtUserId.isEnabled(); // Toggle
            setEditable(editable);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            // TODO: Xóa session, quay về màn hình đăng nhập nếu cần
            finish();
        });

        // Quản lý dịch vụ
        btnManageService.setOnClickListener(v -> {
            // TODO: Chuyển sang activity quản lý dịch vụ
        });

        // Thanh điều hướng dưới cùng
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainCustomer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notification) {
                startActivity(new Intent(this, CustomerNotification.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    // Bật/tắt chỉnh sửa các trường
    private void setEditable(boolean editable) {
        edtUserId.setEnabled(editable);
        edtCCCD.setEnabled(editable);
        edtEmail.setEnabled(editable);
        edtPhone.setEnabled(editable);
        edtBirthday.setEnabled(editable);
        radioMale.setEnabled(editable);
        radioFemale.setEnabled(editable);
        // Nếu muốn đổi màu viền có thể set background ở đây
    }
}
