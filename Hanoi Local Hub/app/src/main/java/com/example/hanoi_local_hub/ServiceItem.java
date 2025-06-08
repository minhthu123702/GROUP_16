package com.example.hanoi_local_hub;

public class ServiceItem {
    private final int imageResId;
    private final String title;
    private final String price;
    private final String rating;
    private final String contact;
    private final String category;
    private final String desc;        // Mô tả chi tiết
    private final String area;        // Khu vực phục vụ
    private final String workTime;    // Giờ làm việc
    private final String reviewCount; // Số người đánh giá

    public ServiceItem(
            int imageResId,
            String title,
            String price,
            String rating,
            String contact,
            String category,
            String desc,
            String area,
            String workTime,
            String reviewCount
    ) {
        this.imageResId = imageResId;
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.contact = contact;
        this.category = category;
        this.desc = desc;
        this.area = area;
        this.workTime = workTime;
        this.reviewCount = reviewCount;
    }

    // Getter methods
    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getRating() { return rating; }
    public String getContact() { return contact; }
    public String getCategory() { return category; }
    public String getDesc() { return desc; }
    public String getArea() { return area; }
    public String getWorkTime() { return workTime; }
    public String getReviewCount() { return reviewCount; }
}
