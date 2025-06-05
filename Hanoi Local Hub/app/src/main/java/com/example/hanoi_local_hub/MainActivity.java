package com.example.hanoi_local_hub;

import com.example.hanoi_local_hub.ServiceItem;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        String[] categories = {"Tất cả", "Gia sư", "Thiết kế", "Sửa chữa", "Chụp Ảnh"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Danh sách dịch vụ (đầy đủ)
        allServices.add(new ServiceItem(R.drawable.image33, "Gia sư Tiếng Anh", "150.000đ", "4.5", "102", "Gia sư"));
        allServices.add(new ServiceItem(R.drawable.image33, "Gia sư Toán", "130.000đ", "4.3", "271", "Gia sư"));
        allServices.add(new ServiceItem(R.drawable.image33, "Gia sư Văn", "130.000đ", "4.4", "67", "Gia sư"));
        allServices.add(new ServiceItem(R.drawable.image33, "Thiết kế", "Liên hệ", "4.7", "50", "Thiết kế"));
        allServices.add(new ServiceItem(R.drawable.image33, "Sửa chữa tủ lạnh", "Liên hệ", "4.3", "58", "Sửa chữa"));
        allServices.add(new ServiceItem(R.drawable.image33, "Chụp ảnh", "300.000đ", "4.4", "364", "Chụp Ảnh"));
        allServices.add(new ServiceItem(R.drawable.image33, "Makeup", "Liên hệ", "4.8", "207", "Khác"));
        allServices.add(new ServiceItem(R.drawable.image33, "Sửa điều hòa", "Liên hệ", "4.8", "9", "Sửa chữa"));

        // Gán tất cả dịch vụ ban đầu
        adapter = new ServiceAdapter(this, new ArrayList<>(allServices));
        recyclerView.setAdapter(adapter);

        // Xử lý chọn danh mục
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
    }
}


