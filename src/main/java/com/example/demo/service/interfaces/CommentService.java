package com.example.demo.service.interfaces;

import com.example.demo.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment create(String postId, Comment comment);
    List<Comment> getAll(String postId, int page, int size, String sortBy);
    Comment getById(String postId, String commentId);
    void delete(String postId, String commentId);
    Comment updateText(String postId, String commentId, String newText);
}
