package com.example.demo.controller;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.mapper.Mapper;
import com.example.demo.entity.Post;
import com.example.demo.service.interfaces.PostService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostGraphQLController {
    private final PostService postService;
    private final Mapper<Post, PostResponse, PostRequest> postMapper;

    public PostGraphQLController(PostService postService, Mapper<Post, PostResponse, PostRequest> postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @QueryMapping
    public PostResponse post(@Argument String id) {
        return postMapper.toResponse(postService.getById(id));
    }

    @QueryMapping
    public List<PostResponse> posts(@Argument int page, @Argument int size, @Argument String sortBy) {
        return postService.getAll(page, size, sortBy).stream().map(postMapper::toResponse).toList();
    }

    @MutationMapping
    public PostResponse createPost(@Argument PostRequest input) {
        var post = postMapper.toEntity(input);

        return postMapper.toResponse(postService.create(post));
    }

    @MutationMapping
    public void deletePost(@Argument String id) {
        postService.delete(id);
    }

    @MutationMapping
    public PostResponse updatePostContent(@Argument String id, @Argument String content) {
        return postMapper.toResponse(postService.updatePostContent(id, content));
    }
}
