package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.enums.UserRole;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.TokenBlacklistService;
import com.example.demo.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${api.base-url}/auth")
public class AuthRestController {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtUtil jwtUtils;
    private PasswordEncoder passwordEncoder;
    private TokenBlacklistService blacklistService;

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            assert userDetails != null;
            String token = jwtUtils.generateToken(userDetails.getUsername());

            return new ApiResponse<>(HttpStatus.OK, "Login successful", token);
        } catch (AuthenticationException ex) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Invalid email or password", null);
        }
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody UserRequest request) {
        if (userService.getByEmail(request.getEmail()) != null) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "User already exists", null);
        }

        final User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of(UserRole.ROLE_USER));

        userService.create(user);

        String token = jwtUtils.generateToken(user.getEmail());
        return new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", token);
    }

    @PostMapping("/admin/register")
    public ApiResponse<String> registerAdmin(@Valid @RequestBody UserRequest request) {
        if (userService.getByEmail(request.getEmail()) != null) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "User already exists", null);
        }

        final User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of(UserRole.ROLE_ADMIN, UserRole.ROLE_USER));

        userService.create(user);

        String token = jwtUtils.generateToken(user.getEmail(), UserRole.ROLE_ADMIN);
        return new ApiResponse<>(HttpStatus.CREATED, "User registered successfully", token);
    }

    @PostMapping("/admin/login")
    public ApiResponse<String> loginAdmin(@Valid @RequestBody UserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        var userDetails = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        assert userDetails != null;

        userDetails.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().equals(UserRole.ROLE_ADMIN.toString()))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("User does not have admin privileges") {});

        String token = jwtUtils.generateToken(userDetails.getUsername(), UserRole.ROLE_ADMIN);

        return new ApiResponse<>(HttpStatus.OK, "Login successful", token);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        Date expiry = jwtUtils.getExpirationDateFromToken(token);

        blacklistService.blacklistToken(token, expiry);

        return ResponseEntity.ok("Logged out successfully");
    }
}
