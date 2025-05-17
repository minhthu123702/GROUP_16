package com.example.hanoiapp.controller;

import com.example.hanoiapp.dto.UserDTO;
import com.example.hanoiapp.model.User;
import com.example.hanoiapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // API đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }

    // API đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            // 💡 Lấy thông tin email và password từ body của request
            String email = userDTO.getEmail();
            String password = userDTO.getPassword();

            // 💡 Gọi service để xử lý đăng nhập
            User user = userService.loginUser(email, password);

            // 💡 Trả về thông báo thành công
            return ResponseEntity.ok("Đăng nhập thành công! Chào mừng, " + 
                    (user.getFullName() != null ? user.getFullName() : user.getEmail()));
        } catch (RuntimeException ex) {
            // 💡 Thông báo lỗi nếu thông tin không hợp lệ
            return ResponseEntity.status(401).body(ex.getMessage());
        }
    }
}
