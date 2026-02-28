package com.example.demo.repository.custom;

import com.example.demo.entity.User;

public interface CustomUserRepository {
    User updatePasswordById(String id, String newPassword);
}
