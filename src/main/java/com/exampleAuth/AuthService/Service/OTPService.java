package com.exampleAuth.AuthService.Service;

import com.exampleAuth.AuthService.Entity.User;
import com.exampleAuth.AuthService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {
    @Autowired
    private UserRepository userRepository;
    private final Random random = new Random();

    public String generateOtp(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return "User not found";
        }

        String otp = String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
        user.setOtp(otp);
        userRepository.save(user);

        return "OTP sent successfully!";
    }

    public String validateOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !user.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        user.setVerified(true);
        user.setOtp(null); // Clear OTP after verification
        userRepository.save(user);

        return "OTP validated successfully!";
    }
}
