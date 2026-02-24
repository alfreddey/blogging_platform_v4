package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.model.Post;
import com.example.demo.repository.interfaces.custom.CustomPostRepositoryImpl;
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
class CustomPostRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CustomPostRepositoryImpl customPostRepository;

    @Test
    void updatePostContent_ShouldReturnUpdatedPost() {
        // Arrange
        String postId = "post-123";
        String newContent = "Updated content";
        Post expectedPost = new Post();
        expectedPost.setId(postId);
        expectedPost.setContent(newContent);

        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Post.class)
        )).thenReturn(expectedPost);

        // Act
        Post result = customPostRepository.updatePostContent(postId, newContent);

        // Assert
        assertNotNull(result);
        assertEquals(newContent, result.getContent());
        verify(mongoTemplate, times(1)).findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Post.class)
        );
    }
}