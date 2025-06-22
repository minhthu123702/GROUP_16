package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hanoi_local_hub.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // Lấy info_container
        LinearLayout infoContainer = findViewById(R.id.info_container);

        // Lấy từng dòng (theo thứ tự include trong xml)
        setProfileRow(infoContainer.getChildAt(0), "Mã người dùng:", "111111");
        setProfileRow(infoContainer.getChildAt(1), "Tên đăng nhập:", "customer");
        setProfileRow(infoContainer.getChildAt(2), "CCCD:", "001200356741");
        setProfileRow(infoContainer.getChildAt(3), "Email:", "customer@gmail.com");
        setProfileRow(infoContainer.getChildAt(4), "Số điện thoại:", "0123456789");
        setProfileRow(infoContainer.getChildAt(5), "Ngày sinh:", "19/12/2000");
        EditText etBirthday = infoContainer.getChildAt(5).findViewById(R.id.etValue);
        etBirthday.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);

        // Tên người dùng lớn ở trên cùng
        TextView txtName = findViewById(R.id.txtName);
        txtName.setText("Trần Đăng Dương");

        // Giới tính
        LinearLayout genderRow = (LinearLayout) infoContainer.getChildAt(6);
        RadioGroup radioGender = genderRow.findViewById(R.id.radioGender);
        RadioButton radioMale = genderRow.findViewById(R.id.radioMale);
        RadioButton radioFemale = genderRow.findViewById(R.id.radioFemale);
        radioMale.setChecked(true);

        // Đăng xuất
        TextView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // TODO: Đăng xuất (ví dụ: xóa session, quay về login)
            finish();
        });

        // Quản lý dịch vụ
        TextView btnManageService = findViewById(R.id.btnManageService);
        btnManageService.setOnClickListener(v -> {
            // TODO: Chuyển sang activity quản lý dịch vụ nếu cần
        });

        // Thanh điều hướng
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, MainCustomer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notification) {
                startActivity(new Intent(this, CustomerNotification.class)); // Đúng class
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                // Đã ở trang này rồi
                return true;
            }
            return false;
        });
    }

    // Hàm gán label và value cho từng row
    private void setProfileRow(android.view.View row, String label, String value) {
        TextView tvLabel = row.findViewById(R.id.tvLabel);
        EditText etValue = row.findViewById(R.id.etValue);
        tvLabel.setText(label);
        etValue.setText(value);
    }
}
