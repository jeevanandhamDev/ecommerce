package com.jb.jb.controllers;

import com.jb.jb.entities.User;
import com.jb.jb.repositories.UserRepository;
import com.jb.jb.services.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private OtpService otpService;
    @Autowired private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String otp = userData.get("otp");

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        if (userRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        User user = new User();
        user.setName(userData.get("name"));
        user.setEmail(email);
        user.setPassword(userData.get("password")); // Hash in real apps
        user.setPhoneNumber(userData.get("phone"));
        user.setAddress(userData.get("address"));

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> data) {
        boolean valid = otpService.verifyOtp(data.get("email"), data.get("otp"));
        return valid ? ResponseEntity.ok("verified") : ResponseEntity.badRequest().body("Invalid OTP");
    }
}