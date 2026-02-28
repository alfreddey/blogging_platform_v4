package com.example.demo.mapper;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MongoUserMapper implements Mapper<User, UserResponse, UserRequest> {
    @Override
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        var userResponse = new UserResponse();

        userResponse.setEmail(user.getEmail());
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setRoles(user.getRoles());

        return userResponse;
    }

    @Override
    public User toEntity(UserRequest request) {
        List<UserRole> roles = new ArrayList<>();

        if (request.getRoles() != null) {
            roles = request.getRoles();
        }

        return request == null ? null : map(null, request.name, request.email, request.password, roles);
    }

    private static User map(String id, String name, String email, String password, List<UserRole> roles) {
        var user = new User();

        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);

        return user;
    }
}
