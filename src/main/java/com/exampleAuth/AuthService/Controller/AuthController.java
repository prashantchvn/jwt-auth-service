package com.exampleAuth.AuthService.Controller;

import com.exampleAuth.AuthService.POJO.LoginRequest;
import com.exampleAuth.AuthService.Service.AuthService;
import com.exampleAuth.AuthService.Service.ForgotPasswordService;
import com.exampleAuth.AuthService.Service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @Autowired
    private OTPService otpService;

    @GetMapping("/test")
    public String testAPI(){
        return "Working fine";
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginRequest loginRequest) {
        return authService.registerUser(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        return otpService.generateOtp(email);
    }

    @PostMapping("/validate-otp")
    public String validateOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.validateOtp(email, otp);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        return forgotPasswordService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
        return forgotPasswordService.resetPassword(email, otp, newPassword);
    }
}
