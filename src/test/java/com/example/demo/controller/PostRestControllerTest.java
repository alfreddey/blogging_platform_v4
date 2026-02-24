package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.Post;
import com.example.demo.service.interfaces.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PostRestControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private Mapper<Post, PostResponse, PostRequest> postMapper;

    @InjectMocks
    private PostRestController postRestController;

    @Test
    void getAll_ShouldReturnMappedResponses() {
        Post post = new Post();
        PostResponse response = new PostResponse();
        when(postService.getAll(0, 4, "id")).thenReturn(List.of(post));
        when(postMapper.toResponse(post)).thenReturn(response);

        ApiResponse<List<PostResponse>> result = postRestController.getAll(0, 4, "id");

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(1, result.getData().size());
        verify(postService).getAll(0, 4, "id");
    }

    @Test
    void create_ShouldReturnCreatedStatus() {
        // Arrange
        PostRequest request = new PostRequest();
        Post post = new Post();
        PostResponse response = new PostResponse();

        when(postMapper.toEntity(request)).thenReturn(post);
        when(postService.create(post)).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(response);

        // Act
        ApiResponse<PostResponse> result = postRestController.create(request);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatus());
        assertNotNull(result.getData());
        verify(postService).create(post);
    }

    @Test
    void delete_ShouldInvokeService() {
        // Act
        ApiResponse<Void> result = postRestController.delete("1");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatus());
        verify(postService).delete("1");
    }

    @Test
    void searchPost_ShouldReturnFoundPost() {
        // Arrange
        Post post = new Post();
        PostResponse response = new PostResponse();
        when(postService.findByTitle("query")).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(response);

        // Act
        ApiResponse<PostResponse> result = postRestController.searchPost("query");

        // Assert
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(response, result.getData());
    }
}