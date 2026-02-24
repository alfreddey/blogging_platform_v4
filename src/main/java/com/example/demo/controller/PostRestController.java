package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.Post;
import com.example.demo.service.interfaces.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-url}/posts")
@Tag(name = "Post Management", description = "Operations for creating, searching, and managing blog posts")
public class PostRestController {
    private final PostService postService;
    private final Mapper<Post, PostResponse, PostRequest> postMapper;

    public PostRestController(PostService postService, Mapper<Post, PostResponse, PostRequest> postMapper) {
        this.postMapper = postMapper;
        this.postService = postService;
    }

    @Operation(summary = "Get all posts", description = "Retrieves a full list of all blog posts from the system.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of posts retrieved successfully")
    })
    @GetMapping
    public ApiResponse<List<PostResponse>> getAll(@RequestParam (defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @RequestParam(defaultValue = "id") String sortBy) {
        var posts = postService.getAll(page, size, sortBy)
                .stream()
                .map(postMapper::toResponse)
                .toList();

        return new ApiResponse<>(HttpStatus.OK, "Posts retrieved successfully", posts);
    }

    @Operation(summary = "Get post by ID", description = "Fetch details for a specific post using its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PostResponse> getById(@Valid @PathVariable String postId) {
        return new ApiResponse<>(HttpStatus.OK, "Post retrieved successfully", postMapper.toResponse(postService.getById(postId)));
    }

    @Operation(summary = "Create a new post", description = "Saves a new post and syncs it to the cache.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post created successfully")
    })
    @PostMapping
    public ApiResponse<PostResponse> create(@Valid @RequestBody PostRequest request) {
        var post = postMapper.toEntity(request);

        return new ApiResponse<>(HttpStatus.CREATED, "Post created successfully", postMapper.toResponse(postService.create(post)));
    }

    @Operation(summary = "Delete a post", description = "Removes a post from both database and cache.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post deleted successfully")
    })
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> delete(@Valid @PathVariable String postId) {
        postService.delete(postId);
        return new ApiResponse<>(HttpStatus.OK, "Post deleted successfully", null);
    }

    @Operation(summary = "Update post content", description = "Partially updates the content field of an existing post.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Content updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PatchMapping("/{postId}/content")
    public ApiResponse<PostResponse> updatePostContent(@Valid @PathVariable String postId, @RequestBody PostRequest request) {
        var post = postService.updatePostContent(postId, request.getContent());

        return new ApiResponse<>(HttpStatus.OK, "Post content updated successfully", postMapper.toResponse(post));
    }

    @Operation(
            summary = "Search posts by title",
            description = "Finds a post by an exact title match using optimized Binary Search."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Match found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No post matches the query")
    })
    @GetMapping("/search")
    public ApiResponse<PostResponse> searchPost(@Valid @RequestParam String query) {
        var post = postService.findByTitle(query);

        return new ApiResponse<>(HttpStatus.OK, "Post found successfully", postMapper.toResponse(post));
    }
}
