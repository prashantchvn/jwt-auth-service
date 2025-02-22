package com.exampleAuth.AuthService.Service;

import com.exampleAuth.AuthService.Entity.User;
import com.exampleAuth.AuthService.Repository.UserRepository;
import com.exampleAuth.AuthService.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

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

    public Map<String, String> login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        Map<String, String> response = new HashMap<>();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            response.put("error", "Invalid email or password");
            return response;
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        response.put("token", jwtUtils.generateToken(userDetails));

        return response;
    }
}
