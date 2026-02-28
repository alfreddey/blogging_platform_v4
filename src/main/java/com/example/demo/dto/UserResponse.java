package com.example.demo.dto;

import com.example.demo.enums.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    public String id;
    public String name;
    public String email;
    public List<UserRole> roles;
}
