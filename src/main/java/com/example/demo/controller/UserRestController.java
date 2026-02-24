package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.User;
import com.example.demo.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-url}/users")
@Tag(name = "User Management", description = "Operations for managing user accounts and profiles")
public class UserRestController {
    private final UserService userService;
    private final Mapper<User, UserResponse, UserRequest> userMapper;

    public UserRestController(UserService userService, Mapper<User, UserResponse, UserRequest> userMapper) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @Operation(
            summary = "Retrieves user details by ID",
            description = """
                    Returns a single user object.
                    Requires a valid UUID.
                    If no user is found, a 404 is returned.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User profile found and retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No user found with that UUID"
            )
    })
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(@PathVariable String id) {
        var user = userService.getById(id);

        return new ApiResponse<>(HttpStatus.OK, "User retrieved successfully", userMapper.toResponse(user));
    }

    @Operation(
            summary = "Retrieves a list of user details",
            description = "Returns a list of all registered users."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "List of users retrieved successfully"
            )
    })
    @GetMapping
    public ApiResponse<List<UserResponse>> getAll(@RequestParam (defaultValue = "0") int page, @RequestParam (defaultValue = "4") int size, @RequestParam (defaultValue = "id") String sortBy) {
        var users = userService.getAll(page, size, sortBy);

        return new ApiResponse<>(HttpStatus.OK, "Users retrieved successfully", users.stream().map(userMapper::toResponse).toList());
    }

    @Operation(summary = "Register a new user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User account successfully created and persisted."
            )
    })
    @PostMapping
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
        var user = userService.create(userMapper.toEntity(request));

        return new ApiResponse<>(HttpStatus.CREATED, "User created successfully", userMapper.toResponse(user));
    }

    @Operation(
            summary = "Update a registered user's password",
            description = "Updates the password for a specific user identified by their ID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Password updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @PatchMapping("/{id}/password")
    public ApiResponse<UserResponse> updatePassword(@Valid @PathVariable String id, @Valid @RequestBody UserRequest request) {
        var user = userService.updatePassword(id, request.password);

        return new ApiResponse<>(HttpStatus.OK, "User password updated successfully", userMapper.toResponse(user));
    }

    @Operation(
            summary = "Delete a user account",
            description = "Permanently removes a user from the system by their UUID."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User successfully deleted"
            )
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@Valid @PathVariable String id) {
        userService.delete(id);

        return new ApiResponse<>(HttpStatus.NO_CONTENT, "User deleted successfully", null);
    }
}
