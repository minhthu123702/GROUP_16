package com.example.hanoi_local_hub.ServiceManagement; // Thay bằng package của bạn

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceModel {

    // --- Các trường do người dùng nhập hoặc hệ thống tạo ra ---
    private String title;
    private String description;
    private String categoryName;
    private String pricingInfo;
    private List<String> serviceArea;
    private String operatingHours;
    private List<String> portfolioImages; // Thay thế cho imageResId, có thể chứa nhiều ảnh
    private String status;

    // --- Các trường liên hệ (Bổ sung lại theo yêu cầu của bạn) ---
    private String contact; // Tên người/cửa hàng liên hệ
    private String phone;
    private String certifications;
    private String email;
    // Nếu bạn muốn có nhiều thông tin liên hệ hơn, có thể dùng Map<String, String> contactInfo;

    public String getCertifications() {
        return certifications;
    }

    // --- Các trường do hệ thống tính toán hoặc quản lý ---
    private double averageRating;
    private int reviewCount;

    public void setCertifications(String certifications) {
        this.certifications = certifications;
    }

    private String providerId; // ID của người đăng dịch vụ

    @ServerTimestamp
    private Date createdAt; // Thời gian tạo, tự động gán bởi server

    @Exclude
    private String serviceId; // ID của document, không lưu vào Firestore nhưng cần ở client

    // BẮT BUỘC: Phải có một constructor rỗng để Firestore hoạt động
    public ServiceModel() {}

    // --- GETTERS ---
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategoryName() { return categoryName; }
    public String getPricingInfo() { return pricingInfo; }
    public List<String> getServiceArea() { return serviceArea; }
    public String getOperatingHours() { return operatingHours; }
    public List<String> getPortfolioImages() { return portfolioImages; }
    public String getStatus() { return status; }
    public String getContact() { return contact; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public double getAverageRating() { return averageRating; }
    public int getReviewCount() { return reviewCount; }
    public String getProviderId() { return providerId; }
    public Date getCreatedAt() { return createdAt; }
    @Exclude
    public String getServiceId() { return serviceId; }


    // --- SETTERS ---
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setPricingInfo(String pricingInfo) { this.pricingInfo = pricingInfo; }
    public void setServiceArea(List<String> serviceArea) { this.serviceArea = serviceArea; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }
    public void setPortfolioImages(List<String> portfolioImages) { this.portfolioImages = portfolioImages; }
    public void setStatus(String status) { this.status = status; }
    public void setContact(String contact) { this.contact = contact; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
}