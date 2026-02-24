package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.repository.interfaces.CommentRepository;
import com.example.demo.service.interfaces.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MongoCommentService implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment create(String postId, Comment comment) {
        return commentRepository.save(postId, comment);
    }

    @Override
    public List<Comment> getAll(String postId, int page, int size, String sortBy) {
        return commentRepository.findAll(postId, page, size, sortBy);
    }

    @Override
    public Comment getById(String postId, String commentId) {
        return commentRepository.findById(postId, commentId);
    }

    @Override
    public void delete(String postId, String commentId) {
        commentRepository.deleteById(postId, commentId);
    }

    @Override
    public Comment updateText(String postId, String commentId, String newText) {
        return commentRepository.updateText(postId, commentId, newText);
    }
}
