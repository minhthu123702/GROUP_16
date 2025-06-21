package com.example.hanoi_local_hub;

public class UserProfile {
    public String userId;
    public String username;
    public String cccd;
    public String email;
    public String phone;
    public String birthday;
    public String fullName;
    public String gender; // "Nam" hoặc "Nữ"

    public UserProfile() {} // Bắt buộc cho Firebase

    public UserProfile(String userId, String username, String cccd, String email,
                       String phone, String birthday, String fullName, String gender) {
        this.userId = userId;
        this.username = username;
        this.cccd = cccd;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.fullName = fullName;
        this.gender = gender;
    }
}
