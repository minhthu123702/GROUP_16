// AdminManagerCustomersActivity.java
package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminManagerCustomersActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private ImageButton btnback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_customers_admin);

        recyclerView = findViewById(R.id.rcvUser);
        btnback = findViewById(R.id.btnBack1);
        // Khởi tạo danh sách user mẫu
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

        // Khởi tạo adapter với sự kiện hiển thị và xóa
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Log.d("DEBUG", "Click user: " + user.getName());
                Intent intent = new Intent(AdminManagerCustomersActivity.this, CustomerProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            @Override
            public void onDeleteClick(User user, int position) {
                adapter.removeUser(position);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnback.setOnClickListener(v -> {
            Intent intent1 = new Intent(AdminManagerCustomersActivity.this, MainMenuActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent1);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminManagerCustomersActivity.this, MainMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
