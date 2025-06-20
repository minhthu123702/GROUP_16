package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminManagerProvidersActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private ImageButton btnback;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_providers_admin);

        recyclerView = findViewById(R.id.rcvUser);
        btnback = findViewById(R.id.btnBack1);
        edtSearch = findViewById(R.id.edtSearch);

        // Danh sách user mẫu
        userList = new ArrayList<>();
        userList.add(new User("Nguyễn Văn A", "123456", R.drawable.avatar1, true));
        userList.add(new User("Trần Thị B", "654321", R.drawable.avatar1, false));
        userList.add(new User("Lê Văn C", "999888", R.drawable.avatar1, true));
        userList.add(new User("Lê Văn D", "999888", R.drawable.avatar1, true));
        userList.add(new User("Vũ Minh E", "445566", R.drawable.avatar1, true));
        userList.add(new User("Hoàng Văn F", "224466", R.drawable.avatar1, false));
        userList.add(new User("Lý Thị G", "101112", R.drawable.avatar1, true));
        userList.add(new User("Phan Anh H", "334455", R.drawable.avatar1, false));
        userList.add(new User("Bùi Thị I", "556677", R.drawable.avatar1, true));
        userList.add(new User("Đặng Văn K", "778899", R.drawable.avatar1, false));
        userList.add(new User("Bùi Thị X", "556677", R.drawable.avatar1, true));
        userList.add(new User("Đặng Văn Z", "778899", R.drawable.avatar1, false));

        // Adapter và sự kiện click/xóa
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Log.d("DEBUG", "Click user: " + user.getName());
                Intent intent = new Intent(AdminManagerProvidersActivity.this, CustomerProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(User user, int position) {
                adapter.removeUser(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Tìm kiếm theo mã khách hàng
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsersByCode(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // Nút quay về
        btnback.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManagerProvidersActivity.this, MainAdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    // Lọc user theo mã
    private void filterUsersByCode(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getCode().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminManagerProvidersActivity.this, MainAdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
