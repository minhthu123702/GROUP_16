package com.example.hanoiapp.service;

import com.example.hanoiapp.dto.UserDTO;
import com.example.hanoiapp.model.User;
import com.example.hanoiapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserType(userDTO.getUserType());
        user.setVerified(false); // Mặc định là false khi đăng ký

        // Các trường khác nếu có thì set, không thì bỏ qua (nullable)
        if (userDTO.getFullName() != null) user.setFullName(userDTO.getFullName());
        if (userDTO.getAddressDistrict() != null) user.setAddressDistrict(userDTO.getAddressDistrict());
        if (userDTO.getAddressWard() != null) user.setAddressWard(userDTO.getAddressWard());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getProfileImageUrl() != null) user.setProfileImageUrl(userDTO.getProfileImageUrl());

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        return userRepository.findByEmail(email)
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .orElseThrow(() -> new RuntimeException("Sai tên đăng nhập hoặc mật khẩu"));
    }
}