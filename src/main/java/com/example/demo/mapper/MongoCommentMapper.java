package com.example.demo.mapper;

import com.example.demo.dto.CommentRequest;
import com.example.demo.dto.CommentResponse;
import com.example.demo.entity.Comment;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MongoCommentMapper implements Mapper<Comment, CommentResponse, CommentRequest> {
    @Override
    public Comment toEntity(CommentRequest request) {
        var comment = new Comment();

        comment.setId(new ObjectId().toHexString());
        comment.setAuthorEmail(request.getAuthorEmail());
        comment.setText(request.getText());
        comment.setCreatedAt(new Date());

        return comment;
    }

    @Override
    public CommentResponse toResponse(Comment comment) {
        var response = new CommentResponse();

        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setAuthorEmail(comment.getAuthorEmail());
        response.setCreatedAt(comment.getCreatedAt());

        return response;
    }
}
