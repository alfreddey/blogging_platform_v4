package com.example.demo.entity;

import com.example.demo.enums.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id private String id;
    private String name;
    private String email;
    private String password;
    private List<UserRole> roles;
}
