package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainCustomer extends AppCompatActivity {

    private List<ServiceItem> allServices = new ArrayList<>();
    private ServiceAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_home);

        recyclerView = findViewById(R.id.recyclerViewServices);
        spinner = findViewById(R.id.spinnerCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Danh sách danh mục
        String[] categories = {"Tất cả", "Thiết kế Đồ họa", "Gia sư", "Sửa chữa", "Giặt là", "Khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Adapter + click chuyển sang màn chi tiết, chỉ cần truyền serviceId
        adapter = new ServiceAdapter(this, new ArrayList<>(), service -> {
            Intent intent = new Intent(MainCustomer.this, ServiceDetailActivity.class);
            intent.putExtra("serviceId", service.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Load tất cả dịch vụ từ Firestore
        loadAllServicesFromFirebase();

        // Lọc theo danh mục trên spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                filterByCategory(selectedCategory);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Sự kiện tìm kiếm
        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(MainCustomer.this, SearchActivity.class));
        });

        // ===== BOTTOM NAVIGATION XỬ LÝ Ở ĐÂY =====
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int navId = item.getItemId();
            if (navId == R.id.nav_home) {
                return true;
            } else if (navId == R.id.nav_notification) {
                startActivity(new Intent(MainCustomer.this, CustomerNotification.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (navId == R.id.nav_profile) {
                startActivity(new Intent(MainCustomer.this, CustomerProfile.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    // Hàm load tất cả dịch vụ từ Firebase
    private void loadAllServicesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .whereEqualTo("status", "approved") // chỉ lấy dịch vụ đã duyệt
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allServices.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceItem item = doc.toObject(ServiceItem.class);
                        item.setId(doc.getId());
                        allServices.add(item);
                    }
                    adapter.updateData(new ArrayList<>(allServices));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi load data (nếu cần)
                });
    }

    // Hàm lọc theo danh mục
    private void filterByCategory(String selectedCategory) {
        if (selectedCategory.equals("Tất cả")) {
            adapter.updateData(new ArrayList<>(allServices));
        } else {
            List<ServiceItem> filtered = new ArrayList<>();
            for (ServiceItem item : allServices) {
                if (selectedCategory.equals(item.getCategoryName())) {
                    filtered.add(item);
                }
            }
            adapter.updateData(filtered);
        }
    }
}
