package com.example.hanoi_local_hub;

public class UserProfile {
    public String uid;
    public String avatarResId;
    public String name;
    public String cccd;
    public String email;
    public String phone;
    public String birthday;
    public String gender;

    public UserProfile() {}

    public UserProfile(String uid, String avatarResId, String name, String cccd, String email,
                       String phone, String birthday, String gender) {
        this.uid = uid;
        this.avatarResId = avatarResId;
        this.name = name;
        this.cccd = cccd;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
    }
}
