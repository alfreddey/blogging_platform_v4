package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.entity.Comment;
import com.example.demo.service.interfaces.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("${api.base-url}/posts/{postId}/comments")
@Tag(name = "Comment Management", description = "Operations for managing comments within specific blog posts")
public class CommentRestController {
    private final CommentService commentService;
    private final Mapper<Comment, CommentResponse, CommentRequest> commentMapper;

    @Operation(summary = "Create a comment", description = "Adds a new comment to the specified post.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PostMapping
    public ApiResponse<CommentResponse> create(@PathVariable String postId, @Valid @RequestBody CommentRequest requestBody) {
        var commentEntity = commentMapper.toEntity(requestBody);
        var comment = commentService.create(postId, commentEntity);
        return new ApiResponse<>(HttpStatus.CREATED, "Comment successfully created", commentMapper.toResponse(comment));
    }

    @Operation(summary = "Delete a comment", description = "Removes a comment from a post by its ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment deleted successfully")
    })
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> delete(@PathVariable String postId, @PathVariable String commentId) {
        commentService.delete(postId, commentId);
        return new ApiResponse<>(HttpStatus.OK, "Comment deleted successfully", null);
    }

    @Operation(summary = "Update comment text", description = "Partially updates the text content of a specific comment.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Text updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment or Post not found")
    })
    @PatchMapping("/{commentId}/text")
    public ApiResponse<CommentResponse> updateText(@PathVariable String postId, @PathVariable String commentId, @Valid @RequestBody CommentRequest requestBody) {
        var comment = commentService.updateText(postId, commentId, requestBody.getText());
        return new ApiResponse<>(HttpStatus.OK, "Comment text updated successfully", commentMapper.toResponse(comment));
    }

    @Operation(summary = "Get comment by ID", description = "Fetches a specific comment using its unique ID.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponse> get(@PathVariable String postId, @PathVariable String commentId) {
        var comment = commentService.getById(postId, commentId);
        return new ApiResponse<>(HttpStatus.OK, "Comment retrieved successfully", commentMapper.toResponse(comment));
    }

    @Operation(summary = "Get all comments for a post", description = "Retrieves a paginated list of comments belonging to a post.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of comments retrieved successfully")
    })
    @GetMapping
    public ApiResponse<List<CommentResponse>> get(@PathVariable String postId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "4") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy) {
        var comments = commentService.getAll(postId, page, size, sortBy).stream()
                .map(commentMapper::toResponse)
                .toList();
        return new ApiResponse<>(HttpStatus.OK, "Comments retrieved successfully", comments);
    }
}
