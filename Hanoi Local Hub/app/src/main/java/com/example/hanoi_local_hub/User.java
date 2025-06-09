package com.example.hanoi_local_hub;


public class User {
    private String name;
    private String code;
    private int avatarResId;   // Resource ID của ảnh avatar, ví dụ: R.drawable.avatar1
    private boolean isOnline;  // Trạng thái online: true = online, false = offline

    public User(String name, String code, int avatarResId, boolean isOnline) {
        this.name = name;
        this.code = code;
        this.avatarResId = avatarResId;
        this.isOnline = isOnline;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    // Setter nếu cần
    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
