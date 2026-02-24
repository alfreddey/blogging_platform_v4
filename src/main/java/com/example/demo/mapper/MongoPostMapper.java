package com.example.demo.mapper;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.model.Post;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MongoPostMapper implements Mapper<Post, PostResponse, PostRequest> {
    @Override
    public PostResponse toResponse(Post post) {
        var response = new PostResponse();

        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthorEmail(post.getAuthorEmail());
        response.setTags(post.getTags());
        response.setCreatedAt(post.getCreatedAt());
        response.setComments(post.getComments());

        return response;
    }

    @Override
    public Post toEntity(PostRequest request) {
        var post = new Post();

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthorEmail(request.getAuthorEmail());
        post.setTags(request.getTags() != null ? request.getTags() : Collections.emptyList());
        post.setComments(Collections.emptyList());

        return post;
    }
}
