package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerHistory, recyclerResults;
    private TextView txtSeeMore;
    private EditText edtKeyword;
    private SearchHistoryAdapter adapter;
    private SearchResultAdapter resultAdapter;
    private List<String> historyList;
    private List<ServiceItem> allServices = new ArrayList<>(); // Toàn bộ dịch vụ lấy từ Firebase
    private List<ServiceItem> resultList = new ArrayList<>();  // Kết quả tìm kiếm
    private boolean isShowAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerHistory = findViewById(R.id.recyclerHistory);
        recyclerResults = findViewById(R.id.recyclerResults);
        txtSeeMore = findViewById(R.id.txtSeeMore);
        edtKeyword = findViewById(R.id.edtKeyword);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Lịch sử tìm kiếm mẫu
        historyList = new ArrayList<>();
        historyList.add("Thiết kế logo");
        historyList.add("Chụp ảnh");
        historyList.add("Sửa chữa");

        adapter = new SearchHistoryAdapter(historyList, new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int pos) {
                historyList.remove(pos);
                adapter.notifyItemRemoved(pos);
                updateSeeMore();
            }
            @Override
            public void onKeywordClick(int pos) {
                String kw = historyList.get(pos);
                edtKeyword.setText(kw);
                edtKeyword.setSelection(kw.length());
                triggerSearch();
            }
        });
        recyclerHistory.setAdapter(adapter);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        recyclerResults.setLayoutManager(new GridLayoutManager(this, 2));
        resultAdapter = new SearchResultAdapter(resultList, item -> {
            Intent intent = new Intent(this, ServiceDetailActivity.class);
            intent.putExtra("serviceId", item.getId());
            startActivity(intent);
        });
        recyclerResults.setAdapter(resultAdapter);

        updateSeeMore();

        // Khi ấn enter trên ô tìm kiếm
        edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                triggerSearch();
                return true;
            }
            return false;
        });

        findViewById(R.id.btnFilter).setOnClickListener(v -> {
            CustomerFilterBottomSheetDialogFragment dialog = new CustomerFilterBottomSheetDialogFragment();
            dialog.setOnFilterApplyListener(option -> {
                applyCustomerFilter(option);
            });
            dialog.show(getSupportFragmentManager(), "CustomerFilter");
        });

        txtSeeMore.setOnClickListener(v -> {
            isShowAll = !isShowAll;
            adapter.setShowAll(isShowAll);
            txtSeeMore.setText(isShowAll ? "Thu gọn" : "Xem thêm");
        });

        // Load toàn bộ dịch vụ về local
        loadAllServicesFromFirebase();
    }

    private void loadAllServicesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .whereEqualTo("status", "approved")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allServices.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        ServiceItem item = doc.toObject(ServiceItem.class);
                        item.setId(doc.getId());
                        allServices.add(item);
                    }
                    // Cho phép tìm kiếm sau khi đã load xong data
                    if (!TextUtils.isEmpty(edtKeyword.getText().toString().trim())) {
                        triggerSearch();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thể tải dịch vụ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void triggerSearch() {
        String keyword = edtKeyword.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            addKeywordToHistory(keyword);
            searchService(keyword);
        } else {
            showHistory();
        }
    }

    private void addKeywordToHistory(String keyword) {
        historyList.remove(keyword);
        historyList.add(0, keyword);
        adapter.notifyDataSetChanged();
        updateSeeMore();
    }

    private void updateSeeMore() {
        txtSeeMore.setVisibility(historyList.size() > 3 ? TextView.VISIBLE : TextView.GONE);
        if (historyList.size() <= 3 && isShowAll) {
            isShowAll = false;
            adapter.setShowAll(false);
            txtSeeMore.setText("Xem thêm");
        }
    }

    // Tìm kiếm trên list local đã tải về
    private void searchService(String keyword) {
        resultList.clear();
        for (ServiceItem item : allServices) {
            if (containsIgnoreCase(item.getTitle(), keyword)
                    || containsIgnoreCase(item.getCategoryName(), keyword)
                    || (item.getServiceArea() != null && containsList(item.getServiceArea(), keyword))
                    || containsIgnoreCase(item.getDescription(), keyword)) {
                resultList.add(item);
            }
        }
        resultAdapter.notifyDataSetChanged();

        if (!resultList.isEmpty()) {
            recyclerResults.setVisibility(RecyclerView.VISIBLE);
            recyclerHistory.setVisibility(RecyclerView.GONE);
            txtSeeMore.setVisibility(TextView.GONE);
        } else {
            showHistory();
            Toast.makeText(this, "Không tìm thấy kết quả phù hợp!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean containsIgnoreCase(String str, String keyword) {
        return str != null && str.toLowerCase().contains(keyword.toLowerCase());
    }

    private boolean containsList(List<String> list, String keyword) {
        if (list == null) return false;
        for (String s : list) {
            if (s != null && s.toLowerCase().contains(keyword.toLowerCase())) return true;
        }
        return false;
    }

    private void showHistory() {
        recyclerResults.setVisibility(RecyclerView.GONE);
        recyclerHistory.setVisibility(RecyclerView.VISIBLE);
        updateSeeMore();
    }

    // Lọc nâng cao sau khi đã có resultList
    private void applyCustomerFilter(CustomerFilterOption option) {
        List<ServiceItem> filtered = new ArrayList<>();
        for (ServiceItem item : resultList) {
            boolean isMatch = true;

            // Lọc theo vị trí (location)
            if (option.location != null && item.getServiceArea() != null) {
                boolean found = false;
                for (String area : item.getServiceArea()) {
                    if (area.equalsIgnoreCase(option.location)) {
                        found = true;
                        break;
                    }
                }
                if (!found) isMatch = false;
            }

            // Lọc theo giá (pricingInfo kiểu String, bạn có thể tách số hoặc lưu kiểu số trên Firestore để dễ so sánh)
            try {
                int priceValue = Integer.parseInt(item.getPricingInfo().replaceAll("\\D+", ""));
                if (option.minPrice != null && priceValue < option.minPrice) isMatch = false;
                if (option.maxPrice != null && priceValue > option.maxPrice) isMatch = false;
            } catch (Exception e) {
                isMatch = false;
            }

            // Lọc theo rating
            float ratingValue = (float) item.getAverageRating();
            if (option.minRating != null && ratingValue < option.minRating) isMatch = false;

            if (isMatch) filtered.add(item);
        }
        resultList.clear();
        resultList.addAll(filtered);
        resultAdapter.notifyDataSetChanged();

        if (!resultList.isEmpty()) {
            recyclerResults.setVisibility(RecyclerView.VISIBLE);
            recyclerHistory.setVisibility(RecyclerView.GONE);
            txtSeeMore.setVisibility(TextView.GONE);
        } else {
            showHistory();
        }
    }
}
