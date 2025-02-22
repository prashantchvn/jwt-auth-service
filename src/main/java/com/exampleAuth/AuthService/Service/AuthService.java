package com.exampleAuth.AuthService.Service;

import com.exampleAuth.AuthService.Entity.User;
import com.exampleAuth.AuthService.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String email, String password) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Email already registered!";
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .isVerified(false)
                .build();
        userRepository.save(user);
        return "Registration successful!";
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return "Invalid email or password";
        }
        return "Login successful!";
    }
}
