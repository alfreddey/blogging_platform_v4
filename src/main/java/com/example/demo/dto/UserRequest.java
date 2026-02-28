package com.example.demo.dto;

import com.example.demo.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    public List<UserRole> roles;
}