package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.model.entity.User;
import com.example.demo.repository.interfaces.UserRepository;
import com.example.demo.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("${api.base-url}/auth")
public class AuthRestController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtUtil jwtUtils;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getUsername());
            return new ApiResponse<>(HttpStatus.OK, "Login successful", token);
        } catch (AuthenticationException ex) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Invalid email or password", null);
        }
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "User already exists", null);
        }

        final User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());
        return new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", token);
    }
}
