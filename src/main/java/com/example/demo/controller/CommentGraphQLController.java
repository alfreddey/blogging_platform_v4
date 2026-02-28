package com.example.demo.controller;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.entity.Comment;
import com.example.demo.service.interfaces.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class CommentGraphQLController {
    private final Mapper<Comment, CommentResponse, CommentRequest> commentMapper;
    private final CommentService commentService;

    @QueryMapping
    public CommentResponse comment(@Argument String postId, @Argument String commentId) {
        var comment = commentService.getById(postId, commentId);

        return commentMapper.toResponse(comment);
    }

    @QueryMapping
    public List<CommentResponse> comments(@Argument String postId, @Argument int page, @Argument int size, @Argument String sortBy) {
        var comments = commentService.getAll(postId, page, size, sortBy);

        return comments.stream().map(commentMapper::toResponse).toList();
    }

    @MutationMapping
    public CommentResponse createComment(@Argument String postId, @Argument CommentRequest input) {
        var commentEntity = commentMapper.toEntity(input);
        var comment = commentService.create(postId, commentEntity);

        return commentMapper.toResponse(comment);
    }

    @MutationMapping
    public void deleteComment(@Argument String postId, @Argument String commentId) {
        commentService.delete(postId, commentId);
    }

    @MutationMapping
    public CommentResponse updateCommentText(@Argument String postId, @Argument String commentId, @Argument String text) {
        var updated = commentService.updateText(postId, commentId, text);
        return commentMapper.toResponse(updated);
    }
}
