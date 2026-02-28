package com.example.demo.service.interfaces;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    User getById(String userId);
    User getByEmail(String email);
    List<User> getAll(int page, int size, String sortBy);
    User create(User user);
    void delete(String userId);
    User updatePassword(String userId, String password);
}
