package com.exampleAuth.AuthService.Service;

import com.exampleAuth.AuthService.Entity.User;
import com.exampleAuth.AuthService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPService otpService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String forgotPassword(String email) {
        return otpService.generateOtp(email);
    }

    public String resetPassword(String email, String otp, String newPassword) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.getOtp().equals(otp)) {
            return "Invalid OTP!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        userRepository.save(user);
        return "Password reset successfully!";
    }
}
