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

    // Dùng để phân biệt loại user (customer/provider)
    private String type;

    // Thêm trường cho provider
    private String service;
    private String approveDate;
    private String degreeInfo;

    // Constructor đầy đủ cho provider
    public User(String name, String code, String birth, String gender, String address,
                String email, String phone, String registerDate,
                int avatarResId, boolean isOnline,
                String service, String approveDate, String degreeInfo) {
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
        this.type = "provider";
        this.service = service;
        this.approveDate = approveDate;
        this.degreeInfo = degreeInfo;
    }

    // Constructor cho customer
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
        this.type = "customer";
        this.service = "";
        this.approveDate = "";
        this.degreeInfo = "";
    }

    // Constructor rút gọn (tự động hiểu là customer)
    public User(String name, String code, int avatarResId, boolean isOnline) {
        this(name, code, "", "", "", "", "", "", avatarResId, isOnline);
    }

    // Getters
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getBirth() { return birth; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRegisterDate() { return registerDate; }
    public int getAvatarResId() { return avatarResId; }
    public boolean isOnline() { return isOnline; }
    public String getType() { return type; }
    public String getService() { return service; }
    public String getApproveDate() { return approveDate; }
    public String getDegreeInfo() { return degreeInfo; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCode(String code) { this.code = code; }
    public void setBirth(String birth) { this.birth = birth; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setRegisterDate(String registerDate) { this.registerDate = registerDate; }
    public void setAvatarResId(int avatarResId) { this.avatarResId = avatarResId; }
    public void setOnline(boolean online) { isOnline = online; }
    public void setType(String type) { this.type = type; }
    public void setService(String service) { this.service = service; }
    public void setApproveDate(String approveDate) { this.approveDate = approveDate; }
    public void setDegreeInfo(String degreeInfo) { this.degreeInfo = degreeInfo; }

    public interface OnUserClickListener {
        void onUserClick(User user);
    }
}
