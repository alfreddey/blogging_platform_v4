package com.example.demo.controller;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.entity.User;
import com.example.demo.service.interfaces.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {
    private final UserService userService;
    private final Mapper<User, UserResponse, UserRequest> userMapper;

    public UserGraphQLController(UserService userService, Mapper<User, UserResponse, UserRequest> userMapper) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @QueryMapping
    public UserResponse user(@Argument String id) {
        return userMapper.toResponse(userService.getById(id));
    }

    @QueryMapping
    public List<UserResponse> users(@Argument int page, @Argument int size, @Argument String sortBy) {
        return userService.getAll(page, size, sortBy).stream().map(userMapper::toResponse).toList();
    }

    @MutationMapping
    public UserResponse createUser(@Argument UserRequest input) {
        return userMapper.toResponse(userService.create(userMapper.toEntity(input)));
    }

    @MutationMapping
    public void deleteUser(@Argument String id) {
        userService.delete(id);
    }

    @MutationMapping
    public UserResponse updateUserPassword(@Argument String id, @Argument String password) {
        return userMapper.toResponse(userService.updatePassword(id, password));
    }
}
