package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.interfaces.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MongoUserService userService;

    @Test
    void getById_ShouldReturnUser_WhenExists() {
        User user = new User();
        user.setId("user123");
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        User result = userService.getById("user123");

        assertNotNull(result);
        assertEquals("user123", result.getId());
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById("any")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById("any"));
    }

    @Test
    void create_ShouldSaveAndReturnUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    void delete_ShouldCallRepository() {
        userService.delete("user123");
        verify(userRepository).deleteById("user123");
    }

    @Test
    void updatePassword_ShouldReturnUpdatedUser() {
        User updatedUser = new User();
        when(userRepository.updatePasswordById("user1", "pass")).thenReturn(updatedUser);

        User result = userService.updatePassword("user1", "pass");

        assertEquals(updatedUser, result);
        verify(userRepository).updatePasswordById("user1", "pass");
    }

    @Test
    void getAll_ShouldReturnList() {
        User user = new User();
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<User> result = userService.getAll(0, 10, "username");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}