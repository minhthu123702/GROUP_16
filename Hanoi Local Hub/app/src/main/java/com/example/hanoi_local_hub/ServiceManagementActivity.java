package com.example.hanoi_local_hub; // Gói của bạn

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServiceManagementActivity extends AppCompatActivity {

    private static final String TAG = "ServiceManagement";

    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<ServiceCategory> categoryList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        Button buttonThem = findViewById(R.id.button_them);
        recyclerViewCategories = findViewById(R.id.recyclerView_categories);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setupRecyclerView();

        buttonThem.setOnClickListener(v -> {
            // Mở màn hình AddCategoryActivity
            Intent intent = new Intent(ServiceManagementActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Luôn tải lại dữ liệu khi người dùng quay lại màn hình này
        // để đảm bảo danh sách được cập nhật sau khi thêm hoặc sửa.
        loadCategoriesFromFirestore();
    }

    private void setupRecyclerView() {
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, this);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(int position) {
                ServiceCategory selectedCategory = categoryList.get(position);
                Intent intent = new Intent(ServiceManagementActivity.this, ViewCategoryActivity.class);
                intent.putExtra("CATEGORY_ID", selectedCategory.getId());
                // Nếu muốn truyền thêm:
                intent.putExtra("CATEGORY_NAME", selectedCategory.getName());
                intent.putExtra("CATEGORY_DESC", selectedCategory.getDescription());
                startActivity(intent);
            }


            @Override
            public void onDeleteClick(int position) {
                ServiceCategory selectedCategory = categoryList.get(position);

                // Xác nhận xóa (tùy, nếu không muốn xác nhận thì bỏ dòng này)
                new android.app.AlertDialog.Builder(ServiceManagementActivity.this)
                        .setTitle("Xóa danh mục")
                        .setMessage("Bạn có chắc chắn muốn xóa \"" + selectedCategory.getName() + "\" không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            // Xóa trên Firestore
                            db.collection("categories").document(selectedCategory.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ServiceManagementActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                                        categoryList.remove(position);
                                        categoryAdapter.notifyItemRemoved(position);
                                        // Nếu muốn chắc chắn, gọi lại loadCategoriesFromFirestore()
                                        // loadCategoriesFromFirestore();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ServiceManagementActivity.this, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }


            @Override
            public void onEditClick(int position) {
                ServiceCategory selectedCategory = categoryList.get(position);
                Intent intent = new Intent(ServiceManagementActivity.this, EditCategoryActivity.class);

                // Gửi dữ liệu của mục cần sửa sang màn hình Edit
                intent.putExtra("CATEGORY_ID", selectedCategory.getId());
                intent.putExtra("CATEGORY_NAME", selectedCategory.getName());
                intent.putExtra("CATEGORY_DESC", selectedCategory.getDescription());

                startActivity(intent);
            }

        });
    }

    private void loadCategoriesFromFirestore() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        categoryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ServiceCategory category = document.toObject(ServiceCategory.class);
                            // Lấy ID của document và gán vào đối tượng
                            category.setId(document.getId());
                            categoryList.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Lỗi khi lấy dữ liệu.", task.getException());
                        Toast.makeText(ServiceManagementActivity.this, "Lỗi khi tải dữ liệu.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ===================================================================
    // LỚP MODEL (Đã cập nhật để chứa ID và description)
    // ===================================================================
    public static class ServiceCategory {
        private String id;
        private String name;
        private String description;

        public ServiceCategory() { /* Constructor rỗng cho Firestore */ }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }


    // ===================================================================
    // LỚP ADAPTER (Sử dụng avatar mặc định)
    // ===================================================================
    public static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private List<ServiceCategory> categoryList;
        private Context context;
        private static OnItemClickListener listener;

        public interface OnItemClickListener { void onViewClick(int p); void onDeleteClick(int p); void onEditClick(int p); }
        public void setOnItemClickListener(OnItemClickListener l) { listener = l; }

        public CategoryAdapter(List<ServiceCategory> list, Context ctx) { this.categoryList = list; this.context = ctx; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ServiceCategory category = categoryList.get(position);
            holder.categoryName.setText(category.getName());

            // Luôn đặt ảnh mặc định là "cate_avtar"
            holder.categoryAvatar.setImageResource(R.drawable.cate_avtar);
        }

        @Override
        public int getItemCount() { return categoryList.size(); }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryAvatar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.textView_category_name);
                categoryAvatar = itemView.findViewById(R.id.cate_avatar);
                ImageView iconEye = itemView.findViewById(R.id.ic_eye);
                ImageView iconDelete = itemView.findViewById(R.id.delete);
                ImageView iconEdit = itemView.findViewById(R.id.edit);

                iconEye.setOnClickListener(v -> { if (listener != null) listener.onViewClick(getAdapterPosition()); });
                iconDelete.setOnClickListener(v -> { if (listener != null) listener.onDeleteClick(getAdapterPosition()); });
                iconEdit.setOnClickListener(v -> { if (listener != null) listener.onEditClick(getAdapterPosition()); });
            }
        }
    }
}