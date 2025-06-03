package com.example.hanoi_local_hub;

public class Service {
    public int imageResId;
    public String title;
    public String price;
    public String rating;
    public String contact;

    // Constructor đúng thứ tự và kiểu dữ liệu
    public Service(int imageResId, String title, String price, String rating, String contact) {
        this.imageResId = imageResId;
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.contact = contact;
    }
}
