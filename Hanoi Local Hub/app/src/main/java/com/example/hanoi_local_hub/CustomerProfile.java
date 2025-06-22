package com.example.hanoi_local_hub;

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

    // --- Xem/Chỉnh sửa avatar/họ tên ---
    private LinearLayout profileHeaderView, profileEditHeader;

    private ImageView imgAvatarBig, imgAvatarSmall;
    private TextView txtName;
    private EditText edtFullName;

    // --- Các trường thông tin ---
    private EditText edtUserId, edtCCCD, edtEmail, edtPhone, edtBirthday;
    private RadioGroup radioGender;
    private RadioButton radioMale, radioFemale;

    // --- Nhóm nút dưới cùng ---
    private LinearLayout actionContainer, btnLogout, btnManageService;
    private LinearLayout editButtonContainer;
    private Button btnCancel, btnSave;
    private ImageButton btnEdit;

    // --- Lưu trạng thái ban đầu để "Bỏ qua" ---
    private String originalFullName, originalEmail, originalPhone, originalBirthday;
    private boolean originalIsMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_profile);

        // --- Ánh xạ view ---
        profileHeaderView = findViewById(R.id.profile_header_view);
        profileEditHeader = findViewById(R.id.profile_edit_header);

        imgAvatarBig = findViewById(R.id.imgAvatarBig);
        imgAvatarSmall = findViewById(R.id.imgAvatarSmall);

        txtName = findViewById(R.id.txtName);
        edtFullName = findViewById(R.id.edtFullName);

        edtUserId = findViewById(R.id.edtUserId);
        edtCCCD = findViewById(R.id.edtCCCD);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthday = findViewById(R.id.edtBirthday);
        radioGender = findViewById(R.id.radioGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        actionContainer = findViewById(R.id.action_container);
        btnLogout = findViewById(R.id.btnLogout);
        btnManageService = findViewById(R.id.btnManageService);

        editButtonContainer = findViewById(R.id.edit_button_container);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        btnEdit = findViewById(R.id.btnEdit);

        // --- Gán dữ liệu mẫu ---
        txtName.setText("Trần Đăng Dương");
        edtFullName.setText("Trần Đăng Dương");
        edtUserId.setText("111111");
        edtCCCD.setText("001200356741");
        edtEmail.setText("customer@gmail.com");
        edtPhone.setText("0123456789");
        edtBirthday.setText("19/12/2000");
        radioMale.setChecked(true);

        // --- Ban đầu là chế độ xem ---
        setEditable(false);
        showViewMode();

        // --- Sự kiện nút Sửa ---
        btnEdit.setOnClickListener(v -> {
            saveOriginalValues();
            setEditable(true);
            showEditMode();
        });

        // --- Sự kiện Bỏ qua ---
        btnCancel.setOnClickListener(v -> {
            restoreOriginalValues();
            setEditable(false);
            showViewMode();
        });

        // --- Sự kiện Cập nhật ---
        btnSave.setOnClickListener(v -> {
            // TODO: validate và cập nhật lên server/Firebase nếu muốn
            txtName.setText(edtFullName.getText().toString()); // Cập nhật tên lớn
            setEditable(false);
            showViewMode();
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        });

        // --- Sự kiện Đăng xuất ---
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            // TODO: Đăng xuất thật (xóa session, chuyển sang LoginActivity...)
        });

        // --- Sự kiện Quản lý dịch vụ ---
        btnManageService.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển sang quản lý dịch vụ!", Toast.LENGTH_SHORT).show();
            // TODO: startActivity(new Intent(this, ManageServiceActivity.class));
        });

        // --- Bottom navigation ---
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

    // Bật/tắt chế độ chỉnh sửa cho từng trường
    private void setEditable(boolean editable) {
        edtFullName.setEnabled(editable);
        edtEmail.setEnabled(editable);
        edtPhone.setEnabled(editable);
        edtBirthday.setEnabled(editable);
        radioMale.setEnabled(editable);
        radioFemale.setEnabled(editable);

        // Không cho sửa mã người dùng và CCCD
        edtUserId.setEnabled(false);
        edtCCCD.setEnabled(false);
    }

    // Chuyển sang chế độ xem (ẩn header chỉnh sửa, hiện header xem)
    private void showViewMode() {
        profileHeaderView.setVisibility(View.VISIBLE);
        profileEditHeader.setVisibility(View.GONE);
        actionContainer.setVisibility(View.VISIBLE);
        editButtonContainer.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
    }

    // Chuyển sang chế độ sửa (ẩn header xem, hiện header chỉnh sửa)
    private void showEditMode() {
        profileHeaderView.setVisibility(View.GONE);
        profileEditHeader.setVisibility(View.VISIBLE);
        actionContainer.setVisibility(View.GONE);
        editButtonContainer.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
    }

    // Lưu lại giá trị cũ khi bấm Sửa (phục hồi nếu bấm Bỏ qua)
    private void saveOriginalValues() {
        originalFullName = edtFullName.getText().toString();
        originalEmail = edtEmail.getText().toString();
        originalPhone = edtPhone.getText().toString();
        originalBirthday = edtBirthday.getText().toString();
        originalIsMale = radioMale.isChecked();
    }

    // Phục hồi giá trị khi bấm Bỏ qua
    private void restoreOriginalValues() {
        edtFullName.setText(originalFullName);
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
