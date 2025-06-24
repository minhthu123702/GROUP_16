//package com.example.hanoi_local_hub;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceDataHolder {
//    // Danh sách dịch vụ toàn app (khởi tạo và nạp sẵn dữ liệu)
//    public static List<ServiceItem> allServices = new ArrayList<>();
//
//    // Khối static khởi tạo dữ liệu
//    static {
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Gia sư Tiếng Anh",
//                "150.000đ",
//                "4.5",
//                "102",
//                "Gia sư",
//                "Dạy tiếng Anh cho học sinh cấp 2, luyện thi vào 10, giao tiếp cơ bản. Kinh nghiệm 3 năm, tận tâm, hỗ trợ bài tập về nhà.",
//                "Quận Thanh Xuân, Hà Nội",
//                "Các buổi tối trong tuần, 18h-21h",
//                "20",
//                "0987654321",
//                "tienganh@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Gia sư Toán",
//                "130.000đ",
//                "4.3",
//                "271",
//                "Gia sư",
//                "Gia sư Toán từ lớp 6-12, chuyên luyện thi chuyển cấp, giải toán tư duy logic. Có giáo trình riêng phù hợp từng học sinh.",
//                "Quận Cầu Giấy, Hà Nội",
//                "Thứ 2-6, 17h-20h",
//                "35",
//                "0911223344",
//                "toan@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Gia sư Văn",
//                "130.000đ",
//                "4.4",
//                "67",
//                "Gia sư",
//                "Ôn luyện Văn theo chương trình phổ thông, dạy viết văn nghị luận, văn sáng tạo, hỗ trợ soạn bài và làm đề.",
//                "Quận Đống Đa, Hà Nội",
//                "Thứ 7 & Chủ nhật, 8h-11h",
//                "18",
//                "0977554433",
//                "van@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Thiết kế",
//                "Liên hệ",
//                "4.7",
//                "50",
//                "Thiết kế",
//                "Thiết kế poster, banner, logo, chỉnh sửa ảnh/video chuyên nghiệp cho doanh nghiệp và cá nhân.",
//                "Toàn Hà Nội (Làm online hoặc tại văn phòng)",
//                "Làm việc giờ hành chính hoặc theo yêu cầu",
//                "22",
//                "0933888777",
//                "thietke@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Sửa chữa tủ lạnh",
//                "Liên hệ",
//                "4.3",
//                "58",
//                "Sửa chữa",
//                "Sửa chữa, bảo dưỡng tủ lạnh các hãng. Cam kết sửa đúng bệnh, có bảo hành và hỗ trợ 24/7.",
//                "Khu vực nội thành Hà Nội",
//                "Từ 7h00 đến 21h00 hàng ngày",
//                "17",
//                "0966888999",
//                "tulanh@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Chụp ảnh",
//                "300.000đ",
//                "4.4",
//                "364",
//                "Chụp Ảnh",
//                "Nhận chụp ảnh cá nhân, gia đình, sự kiện, khai trương, kỷ yếu. Ảnh chỉnh sửa chuyên nghiệp, giao file nhanh.",
//                "Toàn Hà Nội",
//                "Linh hoạt theo lịch khách hàng",
//                "49",
//                "0944556677",
//                "chupanh@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Makeup",
//                "Liên hệ",
//                "4.8",
//                "207",
//                "Khác",
//                "Makeup dự tiệc, cưới hỏi, chụp ảnh, trang điểm cá nhân. Đến tận nhà theo yêu cầu.",
//                "Quận Hai Bà Trưng, Hoàn Kiếm, Đống Đa",
//                "Từ 6h00 đến 22h00 hàng ngày",
//                "33",
//                "0922334455",
//                "makeup@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Sửa điều hòa",
//                "Liên hệ",
//                "4.8",
//                "9",
//                "Sửa chữa",
//                "Sửa chữa điều hòa tại nhà, vệ sinh, nạp gas, thay thế linh kiện chính hãng. Có mặt nhanh trong 60 phút.",
//                "Khu vực Cầu Giấy, Thanh Xuân, Hoàng Mai",
//                "Từ 8h00 đến 20h00",
//                "5",
//                "0977999666",
//                "dieuhoa@localhub.vn"
//        ));
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Gia sư Hóa học",
//                "140.000đ",
//                "4.2",
//                "112",
//                "Gia sư",
//                "Gia sư Hóa học các cấp, luyện thi học sinh giỏi. Giáo trình chi tiết, kèm sát chương trình.",
//                "Quận Ba Đình, Hà Nội",
//                "Thứ 3,5,7, 18h-21h",
//                "16",
//                "0961112222",
//                "hoahoc@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Dọn vệ sinh nhà cửa",
//                "250.000đ",
//                "4.6",
//                "39",
//                "Vệ sinh",
//                "Dịch vụ vệ sinh tổng quát nhà ở, văn phòng, chung cư. Đội ngũ chuyên nghiệp, sạch sẽ, đúng hẹn.",
//                "Quận Hoàng Mai, Hà Nội",
//                "Làm việc theo ca, 7h-17h",
//                "21",
//                "0932666888",
//                "vesinh@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Sửa xe máy lưu động",
//                "Liên hệ",
//                "4.5",
//                "81",
//                "Sửa chữa",
//                "Sửa chữa xe máy tại nhà, cứu hộ 24/7, thay dầu, thay lốp, kiểm tra động cơ, hỗ trợ mọi tuyến phố.",
//                "Các quận nội thành Hà Nội",
//                "24/7",
//                "14",
//                "0988223344",
//                "suaxemay@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Cắt tóc nam",
//                "120.000đ",
//                "4.7",
//                "56",
//                "Làm đẹp",
//                "Cắt tóc nam, tạo kiểu, uốn nhuộm, gội đầu massage. Nhận phục vụ tại nhà.",
//                "Quận Hoàn Kiếm, Đống Đa",
//                "Làm việc theo lịch đặt trước",
//                "13",
//                "0944667788",
//                "cattocnam@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Lắp đặt camera giám sát",
//                "Liên hệ",
//                "4.4",
//                "73",
//                "Lắp đặt",
//                "Tư vấn và lắp đặt camera an ninh, báo động chống trộm, bảo trì tận nơi.",
//                "Toàn Hà Nội",
//                "Làm việc giờ hành chính",
//                "10",
//                "0933555999",
//                "camera@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Sửa máy giặt",
//                "Liên hệ",
//                "4.3",
//                "37",
//                "Sửa chữa",
//                "Sửa máy giặt các hãng, thay thế linh kiện, bảo trì, bảo dưỡng, có mặt trong ngày.",
//                "Quận Long Biên, Gia Lâm",
//                "Từ 8h00 đến 20h00",
//                "6",
//                "0969777888",
//                "suamaygiat@localhub.vn"
//        ));
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Gia sư Tin học",
//                "180.000đ",
//                "4.6",
//                "29",
//                "Gia sư",
//                "Gia sư Tin học, dạy kỹ năng văn phòng, lập trình cơ bản cho học sinh, sinh viên.",
//                "Quận Cầu Giấy, Từ Liêm",
//                "Thứ 2-6, 19h-21h",
//                "8",
//                "0977445566",
//                "tinhoc@localhub.vn"
//        ));
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Dịch vụ chuyển nhà",
//                "350.000đ",
//                "4.5",
//                "42",
//                "Vận chuyển",
//                "Chuyển nhà trọn gói, đóng gói, tháo lắp, bốc xếp an toàn, giá hợp lý.",
//                "Quận Thanh Xuân, Hà Đông",
//                "Làm việc mọi ngày",
//                "11",
//                "0912999888",
//                "chuyennha@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Thi công nội thất",
//                "Liên hệ",
//                "4.6",
//                "65",
//                "Nội thất",
//                "Thiết kế và thi công nội thất chung cư, nhà ở, văn phòng. Bảo hành dài hạn.",
//                "Toàn Hà Nội",
//                "Theo dự án",
//                "19",
//                "0944332211",
//                "noithat@localhub.vn"
//        ));
//
//        allServices.add(new ServiceItem(
//                R.drawable.image33,
//                "Dịch vụ thú y tại nhà",
//                "200.000đ",
//                "4.9",
//                "22",
//                "Thú y",
//                "Khám chữa bệnh, tiêm phòng, triệt sản, chăm sóc thú cưng tại nhà. Gọi là có mặt!",
//                "Quận Tây Hồ, Ba Đình, Cầu Giấy",
//                "Linh hoạt theo lịch",
//                "7",
//                "0988445566",
//                "thuy@localhub.vn"
//        ));
//
//    }
//}
