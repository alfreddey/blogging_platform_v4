package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    public String id;
    public String name;
    public String email;
    public List<String> roles;
}
