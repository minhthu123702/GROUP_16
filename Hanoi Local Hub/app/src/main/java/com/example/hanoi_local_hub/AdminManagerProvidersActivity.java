package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast; // Thêm thư viện Toast để thông báo lỗi

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Thêm các thư viện Firebase
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminManagerProvidersActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private ImageButton btnback;
    private EditText edtSearch;

    // Khai báo biến cho Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_providers_admin);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.rcvUser);
        btnback = findViewById(R.id.btnBack1);
        edtSearch = findViewById(R.id.edtSearch);

        // Khởi tạo danh sách user rỗng
        userList = new ArrayList<>();

        // Adapter và sự kiện click/xóa. Intent sẽ điều hướng đến ProviderProfileActivity
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Log.d("DEBUG", "Click user: " + user.getName());
                Intent intent = new Intent(AdminManagerProvidersActivity.this, ProviderProfileActivity.class); // Giữ nguyên điều hướng này
                intent.putExtra("user", user);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(User user, int position) {
                // (Tùy chọn) Thêm logic xóa provider khỏi Firestore tại đây
                adapter.removeUser(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu các nhà cung cấp từ Firestore
        fetchProvidersFromFirestore();

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

    // Phương thức mới để lấy dữ liệu nhà cung cấp từ Firestore
    private void fetchProvidersFromFirestore() {
        db.collection("users")
                .whereEqualTo("role", "user")       // Điều kiện 1: role là "user"
                .whereEqualTo("providerStatus", true)
// Điều kiện 2: isProvider là true
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear(); // Xóa dữ liệu cũ
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy dữ liệu từ document
                            String name = document.getString("name");
                            String code = document.getString("code");
                            Boolean isOnline = document.getBoolean("isOnline");

                            // Tạm thời dùng avatar mặc định.
                            // Để dùng avatar thật, bạn cần sửa lớp User và UserAdapter
                            // để xử lý URL ảnh từ Firestore.
                            int avatar = R.drawable.avatar1;

                            if (name != null && code != null && isOnline != null) {
                                userList.add(new User(name, code, avatar, isOnline));
                            }
                        }
                        // Cập nhật giao diện
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("FIRESTORE_ERROR", "Error getting documents.", task.getException());
                        Toast.makeText(AdminManagerProvidersActivity.this, "Lỗi khi tải dữ liệu nhà cung cấp.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // Lọc user theo mã
    private void filterUsersByCode(String query) {
        List<User> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            // Nếu query trống, hiển thị lại toàn bộ danh sách gốc
            filteredList.addAll(userList);
        } else {
            for (User user : userList) {
                if (user.getCode().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
        }
        // Giả sử adapter của bạn có phương thức setFilteredList
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