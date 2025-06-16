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

import java.util.ArrayList;
import java.util.List;

public class MainCustomer extends AppCompatActivity {

    private List<ServiceItem> allServices;   // Không cần khởi tạo mới
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

        // Lấy danh sách dịch vụ từ ServiceDataHolder
        allServices = ServiceDataHolder.allServices;

        // Danh sách danh mục
        String[] categories = {"Tất cả", "Gia sư", "Thiết kế", "Sửa chữa", "Chụp Ảnh", "Khác"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Tạo adapter có click
        adapter = new ServiceAdapter(this, new ArrayList<>(allServices), service -> {
            Intent intent = new Intent(MainCustomer.this, ServiceDetailActivity.class);
            intent.putExtra("imageResId", service.getImageResId());
            intent.putExtra("title", service.getTitle());
            intent.putExtra("desc", service.getDesc());
            intent.putExtra("category", service.getCategory());
            intent.putExtra("area", service.getArea());
            intent.putExtra("price", service.getPrice());
            intent.putExtra("time", service.getWorkTime());
            intent.putExtra("contact", service.getContact());
            intent.putExtra("rating", service.getRating());
            intent.putExtra("count", service.getReviewCount());
            intent.putExtra("phone", service.getPhone());
            intent.putExtra("email", service.getEmail());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Lọc theo spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                if (selectedCategory.equals("Tất cả")) {
                    adapter.updateData(new ArrayList<>(allServices));
                } else {
                    List<ServiceItem> filtered = new ArrayList<>();
                    for (ServiceItem item : allServices) {
                        if (item.getCategory().equals(selectedCategory)) {
                            filtered.add(item);
                        }
                    }
                    adapter.updateData(filtered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Sự kiện tìm kiếm
        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(MainCustomer.this, SearchActivity.class));
        });

    }
}
