package com.example.hanoi_local_hub;

import java.io.Serializable;

public class User implements Serializable {
    private String avatarUrl;
    private String birthday;
    private String code;
    private String email;
    private String gender;
    private Boolean isOnline;
    private String name;
    private String phone;
    private Boolean providerStatus;
    private String role;
    private String uid;
    private String address;      // BỔ SUNG address nếu bạn dùng

    // Các trường bổ sung (nếu có)
    private String registerDate;
    private int avatarResId;
    private String type;
    private String service;
    private String approveDate;
    private String degreeInfo;

    // Constructor đầy đủ dùng cho Firestore (tự động xác định provider/customer)
    public User(String avatarUrl, String birthday, String code, String email, String gender,
                Boolean isOnline, String name, String phone, Boolean providerStatus, String role, String uid) {
        this.avatarUrl = avatarUrl;
        this.birthday = birthday;
        this.code = code;
        this.email = email;
        this.gender = gender;
        this.isOnline = isOnline;
        this.name = name;
        this.phone = phone;
        this.providerStatus = providerStatus;
        this.role = role;
        this.uid = uid;
    }

    // Constructor cho user tạm (không cần providerStatus v.v)
    public User(String name, String code, int avatarResId, boolean isOnline) {
        this.name = name;
        this.code = code;
        this.avatarResId = avatarResId;
        this.isOnline = isOnline;
        this.type = "customer";
    }

    // Getters
    public String getAvatarUrl() { return avatarUrl; }
    public String getBirthday() { return birthday; }
    public String getBirth() { return birthday; }       // BỔ SUNG getter này nếu bạn thích
    public String getAddress() { return address; }       // BỔ SUNG getter này nếu bạn thích
    public String getCode() { return code; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public Boolean getIsOnline() { return isOnline; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Boolean getProviderStatus() { return providerStatus; }
    public String getRole() { return role; }
    public String getUid() { return uid; }
    public String getRegisterDate() { return registerDate; }
    public int getAvatarResId() { return avatarResId; }
    public String getType() { return type; }
    public String getService() { return service; }
    public String getApproveDate() { return approveDate; }
    public String getDegreeInfo() { return degreeInfo; }

    // Setters
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setCode(String code) { this.code = code; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setProviderStatus(Boolean providerStatus) { this.providerStatus = providerStatus; }
    public void setRole(String role) { this.role = role; }
    public void setUid(String uid) { this.uid = uid; }
    public void setRegisterDate(String registerDate) { this.registerDate = registerDate; }
    public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }
    public void setType(String type) { this.type = type; }
    public void setService(String service) { this.service = service; }
    public void setApproveDate(String approveDate) { this.approveDate = approveDate; }
    public void setDegreeInfo(String degreeInfo) { this.degreeInfo = degreeInfo; }
    public void setAddress(String address) { this.address = address; } // setter cho address

    public boolean isOnline() {
        return isOnline != null && isOnline;
    }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
