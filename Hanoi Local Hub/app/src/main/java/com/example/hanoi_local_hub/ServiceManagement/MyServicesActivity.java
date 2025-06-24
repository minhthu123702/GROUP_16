package com.example.hanoi_local_hub.ServiceManagement; // Thay bằng package của bạn
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanoi_local_hub.R;
import com.example.hanoi_local_hub.ServiceManagement.ServiceModel; // Đảm bảo bạn có file Model này
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyServicesActivity extends AppCompatActivity implements ServiceAdapter.OnItemClickListener, DeleteConfirmationDialog.OnDeleteListener {

    private static final String TAG = "MyServicesActivity";

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddService;
    private TextView tvEmptyList;
    private ServiceAdapter serviceAdapter;
    private List<ServiceModel> serviceList;
    private ActivityResultLauncher<Intent> addEditServiceLauncher;
    private LinearLayout tabShowing, tabPending, tabRejected, tabPaused;
    private TextView textShowing, textPending, textRejected, textPaused;
    private View lineShowing, linePending, lineRejected, linePaused;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentStatus = "approved"; // Trạng thái mặc định

    private int colorActive;
    private int colorInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_home_management);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
// KHỞI TẠO LAUNCHER
        addEditServiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Kiểm tra xem kết quả trả về có phải là "OK" không
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Nếu đúng, nghĩa là người dùng đã Thêm/Sửa thành công
                        // Tải lại danh sách dịch vụ của tab hiện tại
                        Toast.makeText(this, "Đang cập nhật danh sách...", Toast.LENGTH_SHORT).show();
                        loadServices(currentStatus);
                    }
                }
        );

        setupViews();
        setupRecyclerView();
        setupTabs();

        // Tải danh sách mặc định khi mở màn hình
        loadServices(currentStatus);
        setActiveTab(textShowing, lineShowing);
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAddService = findViewById(R.id.fab_add_service);
        tvEmptyList = findViewById(R.id.tv_empty_list);

        // Ánh xạ các LinearLayout của Tab
        tabShowing = findViewById(R.id.tab_showing);
        tabPending = findViewById(R.id.tab_pending);
        tabRejected = findViewById(R.id.tab_rejected);
        tabPaused = findViewById(R.id.tab_paused);

        // Ánh xạ các TextView và View bên trong Tab
        textShowing = findViewById(R.id.text_showing);
        lineShowing = findViewById(R.id.line_showing);
        textPending = findViewById(R.id.text_pending);
        linePending = findViewById(R.id.line_pending);
        textRejected = findViewById(R.id.text_rejected);
        lineRejected = findViewById(R.id.line_rejected);
        textPaused = findViewById(R.id.text_paused);
        linePaused = findViewById(R.id.line_paused);

        // Lấy màu từ resources
        colorActive = ContextCompat.getColor(this, R.color.accent_blue);
        colorInactive = ContextCompat.getColor(this, R.color.text_secondary);

        findViewById(R.id.img_back).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(serviceAdapter);
        fabAddService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            // Dùng launcher để mở
            addEditServiceLauncher.launch(intent);
        });
    }

    private void setupTabs() {
        // Gán sự kiện click cho từng layout tab
        tabShowing.setOnClickListener(v -> handleTabClick("approved", textShowing, lineShowing));
        tabPending.setOnClickListener(v -> handleTabClick("pending", textPending, linePending));
        tabRejected.setOnClickListener(v -> handleTabClick("rejected", textRejected, lineRejected));
        tabPaused.setOnClickListener(v -> handleTabClick("paused", textPaused, linePaused));

    }

    private void handleTabClick(String status, TextView activeText, View activeLine) {
        currentStatus = status;
        loadServices(currentStatus);
        setActiveTab(activeText, activeLine);
    }

    private void setActiveTab(TextView activeText, View activeLine) {
        // Reset tất cả các tab về trạng thái không active
        textShowing.setTextColor(colorInactive);
        lineShowing.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        textPending.setTextColor(colorInactive);
        linePending.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        textRejected.setTextColor(colorInactive);
        lineRejected.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        textPaused.setTextColor(colorInactive);
        linePaused.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));

        // Kích hoạt tab được chọn
        activeText.setTextColor(colorActive);
        activeLine.setBackgroundColor(colorActive);
    }

    private void loadServices(String status) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = currentUser.getUid();

        tvEmptyList.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        serviceList.clear();

        db.collection("services")
                .whereEqualTo("providerId", currentUserId)
                .whereEqualTo("status", status)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult().isEmpty()) {
                            tvEmptyList.setVisibility(View.VISIBLE);
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServiceModel service = document.toObject(ServiceModel.class);
                                service.setServiceId(document.getId());
                                serviceList.add(service);
                            }
                        }
                        serviceAdapter.notifyDataSetChanged();
                    } else {
                        // In ra lỗi chi tiết trong Logcat với mức độ Error (màu đỏ)
                        Log.e(TAG, "Lỗi khi thực hiện truy vấn Firestore: ", task.getException());
                        Toast.makeText(this, "Lỗi khi tải dữ liệu. Hãy kiểm tra Logcat.", Toast.LENGTH_LONG).show();;
                    }
                });
    }
    private void setupListeners() {
        // ... các sự kiện click cho tab

        fabAddService.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            // Dùng launcher để mở
            addEditServiceLauncher.launch(intent);
        });
    }

    // --- Xử lý sự kiện click từ Adapter ---
    @Override
    public void onEditClick(ServiceModel service) {
        Intent intent = new Intent(this, AddServiceActivity.class);
        intent.putExtra("SERVICE_ID_TO_EDIT", service.getServiceId());
        // Dùng launcher để mở
        addEditServiceLauncher.launch(intent);
    }

    @Override
    public void onHideClick(ServiceModel service) {
        // Nếu đang hiển thị thì đổi thành 'paused', nếu đang ẩn thì đổi lại thành 'approved'
        String newStatus = service.getStatus().equals("approved") ? "paused" : "hide";
        db.collection("services").document(service.getServiceId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã cập nhật trạng thái!", Toast.LENGTH_SHORT).show();
                    loadServices(currentStatus); // Tải lại tab hiện tại để item biến mất
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDeleteClick(ServiceModel service) {
        // Sử dụng phương thức factory newInstance để tạo và truyền serviceId
        DeleteConfirmationDialog dialogFragment = DeleteConfirmationDialog.newInstance(service.getServiceId());

        // Hiển thị dialog bằng FragmentManager
        dialogFragment.show(getSupportFragmentManager(), "DeleteConfirmationDialog");
    }

    // THÊM HÀM MỚI NÀY
    // Đây là hàm sẽ được gọi từ DialogFragment sau khi xóa thành công
    @Override
    public void onDeleteSuccess() {
        Toast.makeText(this, "Đã xóa dịch vụ thành công!", Toast.LENGTH_SHORT).show();

        // Tải lại danh sách dịch vụ trên màn hình hiện tại
        loadServices(currentStatus);


    }
    @Override
    public void onItemViewClick(ServiceModel service) {
        // Tạo một Intent để mở ServiceDetailActivity
        Intent intent = new Intent(MyServicesActivity.this, ServiceDetailActivity.class);

        // Gửi ID của dịch vụ được click qua cho màn hình chi tiết
        // Dùng key "SERVICE_ID" để phân biệt với hành động sửa ("SERVICE_ID_TO_EDIT")
        intent.putExtra("SERVICE_ID", service.getServiceId());

        // Mở màn hình chi tiết
        startActivity(intent);
}
}