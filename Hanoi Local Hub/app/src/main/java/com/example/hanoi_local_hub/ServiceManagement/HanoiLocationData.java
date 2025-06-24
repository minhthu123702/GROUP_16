package com.example.hanoi_local_hub.ServiceManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HanoiLocationData {

    // Dùng LinkedHashMap để giữ nguyên thứ tự các quận
    private static final Map<String, List<String>> districtWardMap = new LinkedHashMap<>();

    // Khối static để khởi tạo dữ liệu khi lớp được tải
    static {
        // Dữ liệu mẫu - bạn có thể thêm đầy đủ các quận và phường khác vào đây
        districtWardMap.put("Cầu Giấy", Arrays.asList("Dịch Vọng", "Dịch Vọng Hậu", "Mai Dịch", "Nghĩa Đô", "Nghĩa Tân", "Quan Hoa", "Trung Hòa", "Yên Hòa"));
        districtWardMap.put("Ba Đình", Arrays.asList("Cống Vị", "Điện Biên", "Đội Cấn", "Giảng Võ", "Kim Mã", "Liễu Giai", "Ngọc Hà", "Nguyễn Trung Trực", "Phúc Xá", "Quán Thánh", "Thành Công", "Trúc Bạch", "Vĩnh Phúc"));
        districtWardMap.put("Đống Đa", Arrays.asList("Cát Linh", "Hàng Bột", "Khâm Thiên", "Khương Thượng", "Kim Liên", "Láng Hạ", "Láng Thượng", "Nam Đồng", "Ngã Tư Sở", "Ô Chợ Dừa", "Phương Liên", "Phương Mai", "Quang Trung", "Quốc Tử Giám", "Thịnh Quang", "Thổ Quan", "Trung Liệt", "Trung Phụng", "Trung Tự", "Văn Chương", "Văn Miếu"));
        districtWardMap.put("Hoàn Kiếm", Arrays.asList("Chương Dương", "Cửa Đông", "Cửa Nam", "Đồng Xuân", "Hàng Bạc", "Hàng Bài", "Hàng Bồ", "Hàng Bông", "Hàng Buồm", "Hàng Đào", "Hàng Gai", "Hàng Mã", "Hàng Trống", "Lý Thái Tổ", "Phan Chu Trinh", "Phúc Tân", "Trần Hưng Đạo", "Tràng Tiền"));
        districtWardMap.put("Hà Đông", Arrays.asList("Biên Giang", "Đồng Mai", "Dương Nội", "Hà Cầu", "Kiến Hưng", "La Khê", "Mộ Lao", "Nguyễn Trãi", "Phú La", "Phú Lãm", "Phú Lương", "Phúc La", "Quang Trung", "Vạn Phúc", "Văn Quán", "Yên Nghĩa", "Yết Kiêu"));
    }

    // Hàm để lấy danh sách các Quận/Huyện
    public static List<String> getDistrictList() {
        // Lấy danh sách các quận/huyện gốc từ Map
        List<String> districts = new ArrayList<>(districtWardMap.keySet());

        // Thêm lựa chọn "Toàn bộ" vào vị trí đầu tiên của danh sách (index 0)
        districts.add(0, "Toàn bộ khu vực Hà Nội");

        return districts;
    }

    // Hàm để lấy danh sách các Phường/Xã dựa trên Quận/Huyện đã chọn
    public static List<String> getWardList(String district) {
        if (district != null && districtWardMap.containsKey(district)) {
            return districtWardMap.get(district);
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu không tìm thấy
    }
}