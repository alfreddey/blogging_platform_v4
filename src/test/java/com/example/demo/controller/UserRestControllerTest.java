package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.User;
import com.example.demo.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Mapper<User, UserResponse, UserRequest> userMapper;

    @InjectMocks
    private UserRestController userRestController;

    @Test
    void getById_ShouldReturnUser() {
        User user = new User();
        UserResponse response = new UserResponse();
        when(userService.getById("1")).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        ApiResponse<UserResponse> result = userRestController.getById("1");

        assertEquals(HttpStatus.OK, result.getStatus());
        assertNotNull(result.getData());
        verify(userService).getById("1");
    }

    @Test
    void getAll_ShouldReturnList() {
        User user = new User();
        UserResponse response = new UserResponse();
        when(userService.getAll(0, 4, "id")).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        ApiResponse<List<UserResponse>> result = userRestController.getAll(0, 4, "id");

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(1, result.getData().size());
    }

    @Test
    void create_ShouldReturnCreated() {
        UserRequest request = new UserRequest();
        User user = new User();
        UserResponse response = new UserResponse();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        ApiResponse<UserResponse> result = userRestController.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatus());
        verify(userService).create(user);
    }

    @Test
    void updatePassword_ShouldReturnUpdatedUser() {
        UserRequest request = new UserRequest();
        request.password = "newPass";
        User user = new User();
        UserResponse response = new UserResponse();

        when(userService.updatePassword("1", "newPass")).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        ApiResponse<UserResponse> result = userRestController.updatePassword("1", request);

        assertEquals(HttpStatus.OK, result.getStatus());
        verify(userService).updatePassword("1", "newPass");
    }

    @Test
    void delete_ShouldReturnNoContent() {
        ApiResponse<Void> result = userRestController.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatus());
        verify(userService).delete("1");
    }
}