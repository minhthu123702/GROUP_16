package com.example.hanoi_local_hub;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerHistory;
    private TextView txtSeeMore;
    private EditText edtKeyword;
    private SearchHistoryAdapter adapter;
    private List<String> historyList;
    private boolean isShowAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerHistory = findViewById(R.id.recyclerHistory);
        txtSeeMore = findViewById(R.id.txtSeeMore);
        edtKeyword = findViewById(R.id.edtKeyword);

        // Nút back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Demo lịch sử, có thể lấy từ SharedPreferences/DB
        historyList = new ArrayList<>();
        historyList.add("Thiết kế logo");
        historyList.add("Chụp ảnh");
        historyList.add("Sửa chữa");

        // Khởi tạo adapter
        adapter = new SearchHistoryAdapter(historyList, new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int pos) {
                historyList.remove(pos);
                adapter.notifyItemRemoved(pos);
                updateSeeMore();
            }

            @Override
            public void onKeywordClick(int pos) {
                // Khi bấm vào một dòng lịch sử, nhập lại vào EditText
                edtKeyword.setText(historyList.get(pos));
                edtKeyword.setSelection(edtKeyword.getText().length());
            }
        });

        recyclerHistory.setAdapter(adapter);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        updateSeeMore();

        // Sự kiện tìm kiếm khi nhấn enter trên bàn phím
        edtKeyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String keyword = edtKeyword.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    addKeywordToHistory(keyword);
                    // TODO: Xử lý tìm kiếm thực tế ở đây (ví dụ: load kết quả, chuyển màn...)
                }
                return true;
            }
            return false;
        });

        // Sự kiện lọc (nếu cần)
        findViewById(R.id.btnFilter).setOnClickListener(v -> {
            // TODO: Hiển thị dialog lọc
        });

        // Sự kiện "Xem thêm"
        txtSeeMore.setOnClickListener(v -> {
            isShowAll = !isShowAll;
            adapter.setShowAll(isShowAll);
            txtSeeMore.setText(isShowAll ? "Thu gọn" : "Xem thêm");
        });
    }

    // Thêm từ khóa mới vào đầu lịch sử, không trùng nhau
    private void addKeywordToHistory(String keyword) {
        historyList.remove(keyword); // Xóa nếu đã có (để tránh trùng)
        historyList.add(0, keyword); // Thêm lên đầu
        adapter.notifyDataSetChanged();
        updateSeeMore();
    }

    // Ẩn/hiện "Xem thêm" tùy độ dài list
    private void updateSeeMore() {
        txtSeeMore.setVisibility(historyList.size() > 10 ? TextView.VISIBLE : TextView.GONE);
        // Khi xóa đến còn <=10 , thì luôn thu gọn lại
        if (historyList.size() <= 10 && isShowAll) {
            isShowAll = false;
            adapter.setShowAll(false);
            txtSeeMore.setText("Xem thêm");
        }
    }
}
