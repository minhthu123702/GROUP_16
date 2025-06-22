package com.example.hanoi_local_hub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerHistory, recyclerResults;
    private TextView txtSeeMore;
    private EditText edtKeyword;
    private SearchHistoryAdapter adapter;
    private SearchResultAdapter resultAdapter;
    private List<String> historyList;
    private List<ServiceItem> allServices;      // Lấy từ ServiceDataHolder
    private List<ServiceItem> resultList = new ArrayList<>();
    private boolean isShowAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerHistory = findViewById(R.id.recyclerHistory);
        recyclerResults = findViewById(R.id.recyclerResults);
        txtSeeMore = findViewById(R.id.txtSeeMore);
        edtKeyword = findViewById(R.id.edtKeyword);

        // Nút back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Lấy danh sách dịch vụ
        allServices = ServiceDataHolder.allServices;

        // Lịch sử tìm kiếm mẫu
        historyList = new ArrayList<>();
        historyList.add("Thiết kế logo");
        historyList.add("Chụp ảnh");
        historyList.add("Sửa chữa");

        // Adapter cho lịch sử tìm kiếm
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
                searchService(kw);
            }
        });
        recyclerHistory.setAdapter(adapter);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        // Adapter cho kết quả tìm kiếm (grid 2 cột)
        recyclerResults.setLayoutManager(new GridLayoutManager(this, 2));
        resultAdapter = new SearchResultAdapter(resultList, item -> {
            // Xử lý khi click vào kết quả (chuyển sang ServiceDetailActivity)
            Intent intent = new Intent(this, ServiceDetailActivity.class);
            intent.putExtra("imageResId", item.getImageResId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("desc", item.getDesc());
            intent.putExtra("category", item.getCategory());
            intent.putExtra("area", item.getArea());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("time", item.getWorkTime());
            intent.putExtra("contact", item.getContact());
            intent.putExtra("rating", item.getRating());
            intent.putExtra("count", item.getReviewCount());
            intent.putExtra("phone", item.getPhone());
            intent.putExtra("email", item.getEmail());
            startActivity(intent);
        });
        recyclerResults.setAdapter(resultAdapter);

        updateSeeMore();

        // Sự kiện tìm kiếm khi nhấn enter hoặc click nút tìm
        edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                triggerSearch();
                return true;
            }
            return false;
        });

        // Sự kiện lọc (nếu cần)
        findViewById(R.id.btnFilter).setOnClickListener(v -> {
            // TODO: Hiển thị dialog lọc nâng cao
        });

        // Sự kiện "Xem thêm"
        txtSeeMore.setOnClickListener(v -> {
            isShowAll = !isShowAll;
            adapter.setShowAll(isShowAll);
            txtSeeMore.setText(isShowAll ? "Thu gọn" : "Xem thêm");
        });
    }

    // Gọi khi muốn tìm kiếm theo keyword hiện tại
    private void triggerSearch() {
        String keyword = edtKeyword.getText().toString().trim();
        if (!TextUtils.isEmpty(keyword)) {
            addKeywordToHistory(keyword);
            searchService(keyword);
        } else {
            // Nếu bỏ trống thì show lại lịch sử
            showHistory();
        }
    }

    // Thêm từ khóa mới vào đầu lịch sử, không trùng nhau
    private void addKeywordToHistory(String keyword) {
        historyList.remove(keyword);
        historyList.add(0, keyword);
        adapter.notifyDataSetChanged();
        updateSeeMore();
    }

    // Ẩn/hiện "Xem thêm" tùy độ dài list (chuẩn là > 3)
    private void updateSeeMore() {
        txtSeeMore.setVisibility(historyList.size() > 3 ? TextView.VISIBLE : TextView.GONE);
        if (historyList.size() <= 3 && isShowAll) {
            isShowAll = false;
            adapter.setShowAll(false);
            txtSeeMore.setText("Xem thêm");
        }
    }

    // Lọc & hiển thị danh sách kết quả tìm kiếm
    private void searchService(String keyword) {
        resultList.clear();
        for (ServiceItem item : allServices) {
            // Lọc theo tiêu đề, category, khu vực, mô tả,...
            if (item.getTitle().toLowerCase().contains(keyword.toLowerCase())
                    || item.getCategory().toLowerCase().contains(keyword.toLowerCase())
                    || item.getDesc().toLowerCase().contains(keyword.toLowerCase())
                    || item.getArea().toLowerCase().contains(keyword.toLowerCase())) {
                resultList.add(item);
            }
        }
        resultAdapter.notifyDataSetChanged();

        // Hiện kết quả, ẩn lịch sử nếu có kết quả
        if (!resultList.isEmpty()) {
            recyclerResults.setVisibility(RecyclerView.VISIBLE);
            recyclerHistory.setVisibility(RecyclerView.GONE);
            txtSeeMore.setVisibility(TextView.GONE);
        } else {
            // Nếu không có kết quả, hiện lại lịch sử
            showHistory();
        }
    }

    // Hiện lại lịch sử tìm kiếm và ẩn kết quả
    private void showHistory() {
        recyclerResults.setVisibility(RecyclerView.GONE);
        recyclerHistory.setVisibility(RecyclerView.VISIBLE);
        updateSeeMore();
    }
}
