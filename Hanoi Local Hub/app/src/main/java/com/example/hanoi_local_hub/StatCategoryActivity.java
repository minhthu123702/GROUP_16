package com.example.hanoi_local_hub;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatCategoryActivity extends AppCompatActivity {

    private static final String TAG = "StatCategoryActivity";
    private ImageView btnBack;
    private BarChart barChart;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_category);

        db = FirebaseFirestore.getInstance();

        setupViews();
        setupBarChart();
        loadDataFromFirestore();
    }

    private void setupViews() {
        btnBack = findViewById(R.id.btnBack);
        barChart = findViewById(R.id.barChart);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupBarChart() {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

    }

    private void loadDataFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);

        db.collection("services")
                .whereEqualTo("status", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Không có dữ liệu dịch vụ nào để thống kê.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    Map<String, Integer> categoryCounts = new HashMap<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String categoryName = document.getString("categoryName");
                        if (categoryName != null && !categoryName.isEmpty()) {
                            categoryName = categoryName.trim();
                            categoryName = categoryName.replaceAll("\\s+", " ");
                            int currentCount = categoryCounts.getOrDefault(categoryName, 0);
                            categoryCounts.put(categoryName, currentCount + 1);
                        }
                    }

                    Log.d(TAG, "Dữ liệu đếm được: " + categoryCounts.toString());
                    drawBarChart(categoryCounts);

                    progressBar.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    barChart.animateY(1000);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi tải dữ liệu từ Firestore: ", e);
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void drawBarChart(Map<String, Integer> categoryCounts) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Gán màu thủ công cho các danh mục chính, còn lại auto
        Map<String, Integer> categoryColorMap = new HashMap<>();
        categoryColorMap.put("Sửa chữa", Color.parseColor("#95CFFA")); // Xanh lá
        categoryColorMap.put("Dịch thuật", Color.parseColor("#FF8800"));           // Cam
        categoryColorMap.put("Giặt là", Color.parseColor("#F44336"));              // Đỏ
        categoryColorMap.put("Dạy năng khiếu", Color.parseColor("#2196F3"));  // Xanh dương
        categoryColorMap.put("Thiết kế Đồ họa", Color.parseColor("#AB47BC"));      // Tím

        int i = 0;
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            labels.add(entry.getKey());
            if (categoryColorMap.containsKey(entry.getKey())) {
                colors.add(categoryColorMap.get(entry.getKey()));
            } else {
                colors.add(ColorTemplate.MATERIAL_COLORS[colors.size() % ColorTemplate.MATERIAL_COLORS.length]);
            }
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Danh mục");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.DKGRAY);
        dataSet.setValueTextSize(14f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        barChart.setData(barData);
        barChart.setFitBars(true);

        // Hiển thị label dạng chữ dưới cột
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelRotationAngle(-25f); // Xoay chữ nếu bị dài
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setAxisMinimum(0f); // Đảm bảo luôn >= 0

        barChart.invalidate(); // Vẽ lại
    }
}
