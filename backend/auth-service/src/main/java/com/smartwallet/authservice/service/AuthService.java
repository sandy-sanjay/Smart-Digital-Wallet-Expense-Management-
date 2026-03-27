package com.smartwallet.authservice.service;

import com.smartwallet.authservice.dto.AuthRequest;
import com.smartwallet.authservice.dto.AuthResponse;
import com.smartwallet.authservice.entity.User;
import com.smartwallet.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
            userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or Email already exists!");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: BCrypt
        
        User savedUser = userRepository.save(user);
        return new AuthResponse("dummy-jwt-token", savedUser.getId(), savedUser.getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        return userRepository.findByUsername(request.getUsername())
            .filter(u -> u.getPassword().equals(request.getPassword()))
            .map(u -> new AuthResponse("dummy-jwt-token", u.getId(), u.getUsername()))
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
