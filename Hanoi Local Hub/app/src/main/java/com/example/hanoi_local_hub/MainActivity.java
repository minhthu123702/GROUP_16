package com.example.hanoi_local_hub;

import com.example.hanoi_local_hub.ServiceItem;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.customer_home);

        // Ánh xạ layout gốc (ConstraintLayout) phải có id là "main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewServices), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Spinner (nếu có trong layout)
        Spinner spinner = findViewById(R.id.spinnerCategory);
        if (spinner != null) {
            String[] categories = {"Gia sư", "Thiết kế", "Sửa chữa", "Chụp Ảnh"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<ServiceItem> services = new ArrayList<>();
        services.add(new ServiceItem(R.drawable.image33, "Gia sư Tiếng Anh", "150.000đ", "4.5", "102"));
        services.add(new ServiceItem(R.drawable.image35, "Sửa chữa tủ lạnh", "Liên hệ", "4.3", "58"));
        services.add(new ServiceItem(R.drawable.image33, "Thiết kế", "Liên hệ", "4.7", "50"));
        services.add(new ServiceItem(R.drawable.image33, "Chụp ảnh", "300.000đ", "4.4", "364"));
        services.add(new ServiceItem(R.drawable.image33, "Dọn dẹp", "200.000đ", "4.2", "114"));
        services.add(new ServiceItem(R.drawable.image33, "Makeup", "Liên hệ", "4.8", "207"));
        services.add(new ServiceItem(R.drawable.image33, "Sửa chữa điều hòa", "Liên hệ", "4.8", "9"));
        services.add(new ServiceItem(R.drawable.image33, "Gia sư Toán", "130.000đ", "4.3", "271"));


        ServiceAdapter adapter = new ServiceAdapter(this, services);
        recyclerView.setAdapter(adapter);

    }
}
