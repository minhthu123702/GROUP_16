package com.example.hanoiapp.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String password;
    private String email;
    private String fullName;
    private String userType;
    private boolean isVerified;
    private String phoneNumber;
    private String profileImageUrl;
    private String addressDistrict;
    private String addressWard;
}