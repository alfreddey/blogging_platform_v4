package com.example.demo.repository.interfaces.custom;

import com.example.demo.model.User;

public interface CustomUserRepository {
    User updatePasswordById(String id, String newPassword);
}
