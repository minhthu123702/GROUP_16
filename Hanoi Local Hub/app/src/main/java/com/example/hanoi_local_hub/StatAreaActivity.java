package com.example.hanoi_local_hub; // <-- THAY BẰNG PACKAGE CỦA BẠN

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatAreaActivity extends AppCompatActivity {

    private static final String TAG = "StatAreaActivity";

    // Khai báo View
    private ImageView btnBack;
    private BarChart barChart;
    private ProgressBar progressBar;

    // Khai báo Firestore
    private FirebaseFirestore db;

    // Danh sách CỨNG các khu vực địa lý của Hà Nội
    private final List<String> hanoiAreas = new ArrayList<>(Arrays.asList(
            "Ba Đình", "Bắc Từ Liêm", "Cầu Giấy", "Đống Đa", "Hà Đông", "Hai Bà Trưng",
            "Hoàn Kiếm", "Hoàng Mai", "Long Biên", "Nam Từ Liêm", "Tây Hồ", "Thanh Xuân"
    ));

    // Thêm các loại hình đặc biệt cần thống kê riêng
    private final List<String> specialQueries = new ArrayList<>(Arrays.asList(
            "Làm việc online"
    ));


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_area);

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
        barChart.setFitBars(true);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-60); // Tăng góc xoay cho dễ đọc
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0f);
    }

    /**
     * Hàm chính để tải dữ liệu, xử lý logic "Toàn bộ Hà Nội"
     */
    private void loadDataFromFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);

        // --- BƯỚC A: Lấy số lượng của "Toàn bộ Hà Nội" trước ---
        db.collection("services")
                .whereEqualTo("status", "approved")
                .whereArrayContains("serviceArea", "Toàn bộ Hà Nội")
                .get()
                .addOnSuccessListener(toanBoHaNoiSnapshot -> {
                    // Lưu lại số lượng này
                    final int toanBoHaNoiCount = toanBoHaNoiSnapshot.size();
                    Log.d(TAG, "Số lượng 'Toàn bộ Hà Nội': " + toanBoHaNoiCount);

                    // --- BƯỚC B: Tiếp tục lấy dữ liệu cho các khu vực còn lại ---
                    loadIndividualAreaData(toanBoHaNoiCount);

                }).addOnFailureListener(e -> {
                    // Xử lý lỗi nếu không lấy được dữ liệu "Toàn bộ Hà Nội"
                    Log.e(TAG, "Lỗi khi tải dữ liệu 'Toàn bộ Hà Nội': ", e);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });
    }

    /**
     * Tải dữ liệu cho từng khu vực riêng lẻ và "Làm việc online"
     * @param toanBoHaNoiCount Số lượng dịch vụ "Toàn bộ Hà Nội" để cộng dồn
     */
    private void loadIndividualAreaData(final int toanBoHaNoiCount) {
        // Gộp danh sách các khu vực địa lý và các loại hình đặc biệt
        List<String> allQueries = new ArrayList<>(hanoiAreas);
        allQueries.addAll(specialQueries);

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (String queryItem : allQueries) {
            Task<QuerySnapshot> task = db.collection("services")
                    .whereEqualTo("status", "approved")
                    .whereArrayContains("serviceArea", queryItem)
                    .get();
            tasks.add(task);
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
            // --- BƯỚC C: Tổng hợp kết quả ---
            Map<String, Integer> areaCounts = new LinkedHashMap<>();

            for (int i = 0; i < results.size(); i++) {
                QuerySnapshot snapshot = (QuerySnapshot) results.get(i);
                String queryItemName = allQueries.get(i);
                int specificCount = snapshot.size();
                int finalCount;

                // Nếu là khu vực địa lý, cộng thêm số lượng "Toàn bộ Hà Nội"
                if (hanoiAreas.contains(queryItemName)) {
                    finalCount = specificCount + toanBoHaNoiCount;
                } else {
                    // Nếu là "Làm việc online" hoặc loại khác, giữ nguyên số lượng
                    finalCount = specificCount;
                }

                areaCounts.put(queryItemName, finalCount);
                Log.d(TAG, "Khu vực: " + queryItemName + " - Số lượng riêng: " + specificCount + " - Số lượng cuối cùng: " + finalCount);
            }

            // --- BƯỚC D: Vẽ biểu đồ ---
            drawChart(areaCounts);
            progressBar.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);

        }).addOnFailureListener(e -> {
            Log.e(TAG, "Lỗi khi tải dữ liệu các khu vực riêng lẻ: ", e);
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });
    }


    private void drawChart(Map<String, Integer> areaCounts) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entry : areaCounts.entrySet()) {
            if (entry.getValue() > 0) {
                entries.add(new BarEntry(index, entry.getValue()));
                labels.add(entry.getKey());
                index++;
            }
        }

        if (entries.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy dữ liệu dịch vụ nào.", Toast.LENGTH_LONG).show();
            barChart.clear();
            barChart.invalidate();
            return;
        }

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setLabelCount(labels.size());

        BarDataSet dataSet = new BarDataSet(entries, "Số lượng dịch vụ");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);

        barChart.setData(barData);
        // Tăng giới hạn hiển thị của trục X để không bị cắt chữ ở cột cuối
        barChart.getXAxis().setAxisMaximum(labels.size());
        barChart.animateY(1000);
        barChart.invalidate();
    }
}