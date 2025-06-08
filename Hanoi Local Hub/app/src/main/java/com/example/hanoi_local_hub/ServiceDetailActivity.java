package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        ImageView imageService = findViewById(R.id.imageService);
        TextView textTitle = findViewById(R.id.textTitle);
        TextView textDesc = findViewById(R.id.textDesc);
        TextView textCategory = findViewById(R.id.textCategory);
        TextView textArea = findViewById(R.id.textArea);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textTime = findViewById(R.id.textTime);
        TextView textContact = findViewById(R.id.textContact);
        TextView textRating = findViewById(R.id.textRating);
        TextView textCount = findViewById(R.id.textCount);

        // Lấy dữ liệu gửi từ MainCustomer (cần gửi đủ key khi intent)
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.image33);
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        String category = getIntent().getStringExtra("category");
        String area = getIntent().getStringExtra("area");
        String price = getIntent().getStringExtra("price");
        String time = getIntent().getStringExtra("time");
        String contact = getIntent().getStringExtra("contact");
        String rating = getIntent().getStringExtra("rating");
        String count = getIntent().getStringExtra("count");

        imageService.setImageResource(imageResId);
        textTitle.setText(title);
        textDesc.setText(desc);
        textCategory.setText(category);
        textArea.setText(area);
        textPrice.setText(price);
        textTime.setText(time);
        textContact.setText("Đã có " + (contact != null ? contact : "0") + " người liên hệ");
        textRating.setText(rating + " ★");
        textCount.setText(count + " người đánh giá");
    }
}
