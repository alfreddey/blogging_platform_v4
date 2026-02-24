package com.example.demo.service.interfaces;

import com.example.demo.model.User;

import java.util.List;

public interface UserService {
    User getById(String userId);
    List<User> getAll(int page, int size, String sortBy);
    User create(User user);
    void delete(String userId);
    User updatePassword(String userId, String password);
}
