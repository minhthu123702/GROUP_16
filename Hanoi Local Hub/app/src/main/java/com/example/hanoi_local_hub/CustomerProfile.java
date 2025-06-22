package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerProfile extends AppCompatActivity {

    // Các trường thông tin
    private EditText edtUserId, edtCCCD, edtEmail, edtPhone, edtBirthday;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;
    private TextView txtName;

    // Các nút chức năng
    private LinearLayout actionContainer;        // Nhóm Đăng xuất & Quản lý dịch vụ
    private LinearLayout btnLogout, btnManageService;
    private ImageButton btnEdit;
    private Button btnCancel, btnSave;
    private LinearLayout editButtonContainer;    // Nhóm Bỏ qua & Cập nhật

    // Biến lưu lại dữ liệu ban đầu (phục hồi nếu bỏ qua)
    private String originalEmail, originalPhone, originalBirthday;
    private boolean originalIsMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // Ánh xạ view
        edtUserId = findViewById(R.id.edtUserId);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthday = findViewById(R.id.edtBirthday);
        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        txtName = findViewById(R.id.txtName);

        actionContainer = findViewById(R.id.action_container);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageService = findViewById(R.id.btnManageService);
        btnEdit = findViewById(R.id.btnEdit);

        editButtonContainer = findViewById(R.id.edit_button_container);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        // Gán dữ liệu mẫu (hoặc lấy từ server/Firebase)
        txtName.setText("Trần Đăng Dương");
        edtUserId.setText("111111");
        edtCCCD.setText("001200356741");
        edtEmail.setText("customer@gmail.com");
        edtPhone.setText("0123456789");
        edtBirthday.setText("19/12/2000");
        radioMale.setChecked(true);

        // Chỉ xem lúc đầu
        setEditable(false);
        editButtonContainer.setVisibility(View.GONE);

        // Sự kiện nút Sửa
        btnEdit.setOnClickListener(v -> {
            saveOriginalValues();
            setEditable(true);
            actionContainer.setVisibility(View.GONE);
            editButtonContainer.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        });

        // Sự kiện Bỏ qua
        btnCancel.setOnClickListener(v -> {
            restoreOriginalValues();
            setEditable(false);
            actionContainer.setVisibility(View.VISIBLE);
            editButtonContainer.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        });

        // Sự kiện Cập nhật
        btnSave.setOnClickListener(v -> {
            // TODO: Validate dữ liệu, cập nhật lên server nếu cần
            setEditable(false);
            actionContainer.setVisibility(View.VISIBLE);
            editButtonContainer.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
            // Cập nhật lại tên lớn trên cùng (nếu muốn)
            txtName.setText(edtEmail.getText().toString());
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> {
            // TODO: Xóa session, về màn hình đăng nhập
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            // finish();
        });

        // Sự kiện Quản lý dịch vụ
        btnManageService.setOnClickListener(v -> {
            // TODO: Chuyển sang activity quản lý dịch vụ
            Toast.makeText(this, "Chuyển sang quản lý dịch vụ!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, ManageServiceActivity.class));
        });

        // Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // startActivity(new Intent(this, MainCustomer.class));
                // overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_notification) {
                // startActivity(new Intent(this, CustomerNotification.class));
                // overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    // Bật/tắt chế độ chỉnh sửa
    private void setEditable(boolean editable) {
        edt.setEnabled(editable);
        edtEmail.setEnabled(editable);
        edtPhone.setEnabled(editable);
        edtBirthday.setEnabled(editable);
        radioMale.setEnabled(editable);
        radioFemale.setEnabled(editable);

        // Không bao giờ cho sửa mã người dùng, CCCD
        edtUserId.setEnabled(false);
        edtCCCD.setEnabled(false);
    }

    // Lưu giá trị gốc khi bắt đầu sửa (để phục hồi nếu bấm Bỏ qua)
    private void saveOriginalValues() {
        originalEmail = edtEmail.getText().toString();
        originalPhone = edtPhone.getText().toString();
        originalBirthday = edtBirthday.getText().toString();
        originalIsMale = radioMale.isChecked();
    }

    // Phục hồi giá trị nếu bấm Bỏ qua
    private void restoreOriginalValues() {
        edtEmail.setText(originalEmail);
        edtPhone.setText(originalPhone);
        edtBirthday.setText(originalBirthday);
        if (originalIsMale) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
    }
}
