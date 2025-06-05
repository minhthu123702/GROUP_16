package com.example.hanoi_local_hub;

public class ServiceItem {
    private final String category;
    public int imageResId;
    public String title;
    public String price;
    public String rating;
    public String contact;
     public ServiceItem(int imageResId, String title, String price, String rating, String contact, String category) {
            this.imageResId = imageResId;
            this.title = title;
            this.price = price;
            this.rating = rating;
            this.contact = contact;
            this.category = category;
        }

        // Getter methods
        public int getImageResId() { return imageResId; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getRating() { return rating; }
        public String getContact() { return contact; }
        public String getCategory() { return category; }
    }

