package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }
}
