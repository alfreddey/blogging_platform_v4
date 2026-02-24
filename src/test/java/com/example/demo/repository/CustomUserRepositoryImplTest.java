package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.model.User;
import com.example.demo.repository.interfaces.custom.CustomUserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@ExtendWith(MockitoExtension.class)
class CustomUserRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CustomUserRepositoryImpl customUserRepository;

    @Test
    void updatePasswordById_ShouldReturnUpdatedUser() {
        // Arrange
        String userId = "user-123";
        String newPass = "secret123";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setPassword(newPass);

        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(User.class)
        )).thenReturn(mockUser);

        // Act
        User result = customUserRepository.updatePasswordById(userId, newPass);

        // Assert
        assertNotNull(result);
        assertEquals(newPass, result.getPassword());
        verify(mongoTemplate).findAndModify(any(), any(), any(), eq(User.class));
    }

    @Test
    void updatePasswordById_ShouldReturnNull_WhenUserNotFound() {
        // Arrange
        when(mongoTemplate.findAndModify(any(), any(), any(), eq(User.class)))
                .thenReturn(null);

        // Act
        User result = customUserRepository.updatePasswordById("non-existent", "pass");

        // Assert
        assertNull(result);
    }
}