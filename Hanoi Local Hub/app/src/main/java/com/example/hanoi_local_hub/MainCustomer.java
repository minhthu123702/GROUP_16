package com.example.hanoi_local_hub;

import com.example.hanoi_local_hub.ServiceItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainCustomer extends AppCompatActivity {

    private List<ServiceItem> allServices = new ArrayList<>();
    private ServiceAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_home);

        recyclerView = findViewById(R.id.recyclerViewServices);
        spinner = findViewById(R.id.spinnerCategory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Danh sách danh mục
        String[] categories = {"Tất cả", "Gia sư", "Thiết kế", "Sửa chữa", "Chụp Ảnh"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        // Danh sách dịch vụ (đầy đủ)
        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Gia sư Tiếng Anh",
                "150.000đ",
                "4.5",
                "102",
                "Gia sư",
                "Dạy tiếng Anh cho học sinh cấp 2, luyện thi vào 10, giao tiếp cơ bản. Kinh nghiệm 3 năm, tận tâm, hỗ trợ bài tập về nhà.",
                "Quận Thanh Xuân, Hà Nội",
                "Các buổi tối trong tuần, 18h-21h",
                "20",
                "0987654321",                 // Số điện thoại
                "tienganh@localhub.vn"        // Email
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Gia sư Toán",
                "130.000đ",
                "4.3",
                "271",
                "Gia sư",
                "Gia sư Toán từ lớp 6-12, chuyên luyện thi chuyển cấp, giải toán tư duy logic. Có giáo trình riêng phù hợp từng học sinh.",
                "Quận Cầu Giấy, Hà Nội",
                "Thứ 2-6, 17h-20h",
                "35",
                "0911223344",
                "toan@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Gia sư Văn",
                "130.000đ",
                "4.4",
                "67",
                "Gia sư",
                "Ôn luyện Văn theo chương trình phổ thông, dạy viết văn nghị luận, văn sáng tạo, hỗ trợ soạn bài và làm đề.",
                "Quận Đống Đa, Hà Nội",
                "Thứ 7 & Chủ nhật, 8h-11h",
                "18",
                "0977554433",
                "van@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Thiết kế",
                "Liên hệ",
                "4.7",
                "50",
                "Thiết kế",
                "Thiết kế poster, banner, logo, chỉnh sửa ảnh/video chuyên nghiệp cho doanh nghiệp và cá nhân.",
                "Toàn Hà Nội (Làm online hoặc tại văn phòng)",
                "Làm việc giờ hành chính hoặc theo yêu cầu",
                "22",
                "0933888777",
                "thietke@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Sửa chữa tủ lạnh",
                "Liên hệ",
                "4.3",
                "58",
                "Sửa chữa",
                "Sửa chữa, bảo dưỡng tủ lạnh các hãng. Cam kết sửa đúng bệnh, có bảo hành và hỗ trợ 24/7.",
                "Khu vực nội thành Hà Nội",
                "Từ 7h00 đến 21h00 hàng ngày",
                "17",
                "0966888999",
                "tulanh@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Chụp ảnh",
                "300.000đ",
                "4.4",
                "364",
                "Chụp Ảnh",
                "Nhận chụp ảnh cá nhân, gia đình, sự kiện, khai trương, kỷ yếu. Ảnh chỉnh sửa chuyên nghiệp, giao file nhanh.",
                "Toàn Hà Nội",
                "Linh hoạt theo lịch khách hàng",
                "49",
                "0944556677",
                "chupanh@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Makeup",
                "Liên hệ",
                "4.8",
                "207",
                "Khác",
                "Makeup dự tiệc, cưới hỏi, chụp ảnh, trang điểm cá nhân. Đến tận nhà theo yêu cầu.",
                "Quận Hai Bà Trưng, Hoàn Kiếm, Đống Đa",
                "Từ 6h00 đến 22h00 hàng ngày",
                "33",
                "0922334455",
                "makeup@localhub.vn"
        ));

        allServices.add(new ServiceItem(
                R.drawable.image33,
                "Sửa điều hòa",
                "Liên hệ",
                "4.8",
                "9",
                "Sửa chữa",
                "Sửa chữa điều hòa tại nhà, vệ sinh, nạp gas, thay thế linh kiện chính hãng. Có mặt nhanh trong 60 phút.",
                "Khu vực Cầu Giấy, Thanh Xuân, Hoàng Mai",
                "Từ 8h00 đến 20h00",
                "5",
                "0977999666",
                "dieuhoa@localhub.vn"
        ));


        // Tạo adapter có click
        adapter = new ServiceAdapter(this, new ArrayList<>(allServices), service -> {
            Intent intent = new Intent(MainCustomer.this, ServiceDetailActivity.class);
            intent.putExtra("imageResId", service.getImageResId());
            intent.putExtra("title", service.getTitle());
            intent.putExtra("desc", service.getDesc());
            intent.putExtra("category", service.getCategory());
            intent.putExtra("area", service.getArea());
            intent.putExtra("price", service.getPrice());
            intent.putExtra("time", service.getWorkTime());
            intent.putExtra("contact", service.getContact());
            intent.putExtra("rating", service.getRating());
            intent.putExtra("count", service.getReviewCount());
            intent.putExtra("phone", service.getPhone());
            intent.putExtra("email", service.getEmail());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Lọc theo spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                if (selectedCategory.equals("Tất cả")) {
                    adapter.updateData(new ArrayList<>(allServices));
                } else {
                    List<ServiceItem> filtered = new ArrayList<>();
                    for (ServiceItem item : allServices) {
                        if (item.getCategory().equals(selectedCategory)) {
                            filtered.add(item);
                        }
                    }
                    adapter.updateData(filtered);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(MainCustomer.this, SearchActivity.class));
        });

    }

}

