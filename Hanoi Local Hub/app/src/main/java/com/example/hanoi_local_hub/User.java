package com.example.hanoi_local_hub;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String code;
    private String birth;
    private String gender;
    private String address;
    private String email;
    private String phone;
    private String registerDate;
    private int avatarResId;
    private boolean isOnline;

    // Full constructor
    public User(String name, String code, String birth, String gender, String address,
                String email, String phone, String registerDate,
                int avatarResId, boolean isOnline) {
        this.name = name;
        this.code = code;
        this.birth = birth;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.registerDate = registerDate;
        this.avatarResId = avatarResId;
        this.isOnline = isOnline;
    }

    // Optional: constructor rút gọn (nếu bạn chỉ cần tên, mã, avatar, trạng thái)
    public User(String name, String code, int avatarResId, boolean isOnline) {
        this.name = name;
        this.code = code;
        this.avatarResId = avatarResId;
        this.isOnline = isOnline;

        // Gán giá trị mặc định để tránh lỗi null
        this.birth = "";
        this.gender = "";
        this.address = "";
        this.email = "";
        this.phone = "";
        this.registerDate = "";
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getBirth() {
        return birth;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
    public interface OnUserClickListener {
        void onUserClick(User user);
        holder.itemView.setOnClickListener(v -> {
            listener.onUserClick(userList.get(position));
        });
    }

}
