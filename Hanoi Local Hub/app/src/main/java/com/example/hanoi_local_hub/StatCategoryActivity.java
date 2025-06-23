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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatCategoryActivity extends AppCompatActivity {

    private static final String TAG = "StatCategoryActivity";
    private ImageView btnBack;
    private PieChart pieChart;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_category);

        db = FirebaseFirestore.getInstance();

        setupViews();
        setupPieChart();
        loadDataFromFirestore();
    }

    private void setupViews() {
        btnBack = findViewById(R.id.btnBack);
        pieChart = findViewById(R.id.pieChart);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleRadius(58f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Tỷ lệ Danh mục");
        pieChart.setCenterTextSize(16f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(12f);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(2f);
        l.setYOffset(0f);
        l.setXOffset(0f); // Có thể thử -5f nếu muốn sát nữa

// Đẩy xuống chút cho đẹp

    }

    private void loadDataFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);

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
                            // categoryName = categoryName.toLowerCase(); // Nếu muốn không phân biệt hoa thường
                            int currentCount = categoryCounts.getOrDefault(categoryName, 0);
                            categoryCounts.put(categoryName, currentCount + 1);
                        }
                    }

                    Log.d(TAG, "Dữ liệu đếm được: " + categoryCounts.toString());
                    drawChart(categoryCounts);

                    progressBar.setVisibility(View.GONE);
                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.animateY(1000, Easing.EaseInOutCubic);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi tải dữ liệu từ Firestore: ", e);
                    Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void drawChart(Map<String, Integer> categoryCounts) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        // Gán màu thủ công cho các danh mục chính, còn lại auto
        Map<String, Integer> categoryColorMap = new HashMap<>();
        categoryColorMap.put("Sửa chữa", Color.parseColor("#95CFFA")); // Xanh lá
        categoryColorMap.put("Dịch thuật", Color.parseColor("#FF8800"));           // Cam
        categoryColorMap.put("Giặt là", Color.parseColor("#F44336"));              // Đỏ
        categoryColorMap.put("Dạy năng khiếu", Color.parseColor("#2196F3"));  // Xanh dương
        categoryColorMap.put("Thiết kế Đồ họa", Color.parseColor("#AB47BC"));      // Tím

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            String name = entry.getKey();
            entries.add(new PieEntry(entry.getValue().floatValue(), name));
            if (categoryColorMap.containsKey(name)) {
                colors.add(categoryColorMap.get(name));
            } else {
                // Auto dùng màu của thư viện cho danh mục mới/chưa định nghĩa
                colors.add(ColorTemplate.MATERIAL_COLORS[colors.size() % ColorTemplate.MATERIAL_COLORS.length]);
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(9f);
        data.setValueTextColor(Color.DKGRAY);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}
