package com.example.hanoi_local_hub.ServiceManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.example.hanoi_local_hub.R;


public class MyServicesActivity extends AppCompatActivity implements ServiceAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ServiceAdapter serviceAdapter;
    private List<ServiceModel> serviceList;

    private LinearLayout tabShowing, tabPending, tabRejected, tabDeleted;
    private TextView textShowing, textPending, textRejected, textDeleted;
    private View lineShowing, linePending, lineRejected, lineDeleted;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // (Giả sử bạn có các file màu và dimen tương ứng)
    private int colorActive;
    private int colorInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home_management); // Dùng layout chính của bạn

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setupViews();
        setupRecyclerView();
        setupTabs();

        // Tải danh sách mặc định khi mở màn hình (Đang hiển thị)
        loadServices("approved");
        setActiveTab(tabShowing, textShowing, lineShowing);
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        // Ánh xạ các Tabs (bạn cần đặt ID cho các layout và textview trong XML)
        tabShowing = findViewById(R.id.tab_showing);
        tabPending = findViewById(R.id.tab_pending);
        tabRejected = findViewById(R.id.tab_rejected);
        tabDeleted = findViewById(R.id.tab_deleted);

        // Giả sử bạn đặt ID cho TextView và View bên trong mỗi tab
        textShowing = tabShowing.findViewWithTag("tab_text");
        lineShowing = tabShowing.findViewWithTag("tab_line");
        // ... Làm tương tự cho các tab khác

        colorActive = ContextCompat.getColor(this, R.color.accent_blue);
        colorInactive = ContextCompat.getColor(this, R.color.text_secondary);
    }

    private void setupRecyclerView() {
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serviceAdapter);
    }

    private void setupTabs() {
        tabShowing.setOnClickListener(v -> {
            loadServices("approved");
            setActiveTab(tabShowing, textShowing, lineShowing);
        });
        tabPending.setOnClickListener(v -> {
            loadServices("pending");
            setActiveTab(tabPending, textPending, linePending);
        });
        tabRejected.setOnClickListener(v -> {
            loadServices("rejected");
            setActiveTab(tabRejected, textRejected, lineRejected);
        });
        tabDeleted.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng này đang được phát triển", Toast.LENGTH_SHORT).show();
            setActiveTab(tabDeleted, textDeleted, lineDeleted);
        });
    }

    private void setActiveTab(LinearLayout activeTab, TextView activeText, View activeLine) {
        // Reset tất cả các tab
        // Cần đảm bảo các biến text... và line... không bị null
        if(textShowing != null) textShowing.setTextColor(colorInactive);
        if(lineShowing != null) lineShowing.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        // ... làm tương tự cho các tab khác

        // Kích hoạt tab được chọn
        if(activeText != null) activeText.setTextColor(colorActive);
        if(activeLine != null) activeLine.setBackgroundColor(colorActive);
    }

    private void loadServices(String status) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem dịch vụ.", Toast.LENGTH_SHORT).show();
            return;
        }
        String currentUserId = currentUser.getUid();

        serviceList.clear(); // Xóa dữ liệu cũ
        serviceAdapter.notifyDataSetChanged(); // Báo cho adapter biết dữ liệu đã thay đổi
        // Có thể thêm ProgressBar ở đây

        db.collection("services")
                .whereEqualTo("providerId", currentUserId)
                .whereEqualTo("status", status)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ServiceModel service = document.toObject(ServiceModel.class);
                            service.setServiceId(document.getId());
                            serviceList.add(service);
                        }
                        serviceAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("MyServicesActivity", "Error getting documents.", task.getException());
                        Toast.makeText(this, "Lỗi khi tải dữ liệu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // --- Xử lý sự kiện click từ Adapter ---
    @Override
    public void onEditClick(ServiceModel service) {
        Toast.makeText(this, "Chỉnh sửa: " + service.getTitle(), Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, AddEditServiceActivity.class);
        // intent.putExtra("SERVICE_ID", service.getServiceId());
        // startActivity(intent);
    }

    @Override
    public void onHideClick(ServiceModel service) {
        Toast.makeText(this, "Ẩn: " + service.getTitle(), Toast.LENGTH_SHORT).show();
        // Code để cập nhật status của service thành "paused"
    }

    @Override
    public void onDeleteClick(ServiceModel service) {
        Toast.makeText(this, "Xóa: " + service.getTitle(), Toast.LENGTH_SHORT).show();
        // Code để xóa service (hoặc cập nhật status thành 'deleted')
    }
}