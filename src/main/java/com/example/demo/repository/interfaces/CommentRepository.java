package com.example.demo.repository.interfaces;

import com.example.demo.model.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    Comment save(String postId, Comment comment);
    Comment findById(String postId, String commentId);
    List<Comment> findAll(String postId, int page, int size, String sortBy);
    Comment updateText(String postId, String commentId, String newText);
    void deleteById(String postId, String commentId);
}
