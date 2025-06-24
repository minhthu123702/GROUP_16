package com.example.hanoi_local_hub;

import java.util.List;

public class ServiceItem {
    private String id;                  // Document ID (thủ công set sau khi get từ Firestore)
    private String title;               // Tên dịch vụ (field: title)
    private String description;         // Mô tả chi tiết (field: description)
    private String pricingInfo;         // Giá (field: pricingInfo)
    private String categoryName;        // Danh mục (field: categoryName)
    private String operatingHours;      // Giờ làm việc (field: operatingHours)
    private List<String> serviceArea;   // Khu vực phục vụ (field: serviceArea)
    private List<String> portfolioImages; // Ảnh (field: portfolioImages)
    private double averageRating;       // Đánh giá TB (field: averageRating)
    private int reviewCount;            // Số lượt đánh giá (field: reviewCount)
    private String providerId;          // ID người cung cấp (field: providerId)
    // Nếu lấy info liên hệ từ bảng users thì không để phone/email ở đây

    // Firebase yêu cầu constructor rỗng
    public ServiceItem() {}

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPricingInfo() { return pricingInfo; }
    public void setPricingInfo(String pricingInfo) { this.pricingInfo = pricingInfo; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getOperatingHours() { return operatingHours; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }

    public List<String> getServiceArea() { return serviceArea; }
    public void setServiceArea(List<String> serviceArea) { this.serviceArea = serviceArea; }

    public List<String> getPortfolioImages() { return portfolioImages; }
    public void setPortfolioImages(List<String> portfolioImages) { this.portfolioImages = portfolioImages; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
}
