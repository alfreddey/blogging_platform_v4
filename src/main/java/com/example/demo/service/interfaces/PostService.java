package com.example.demo.service.interfaces;

import com.example.demo.entity.Post;

import java.util.List;

public interface PostService {
    Post getById(String postId);
    List<Post> getAll(int page, int size, String sortBy);
    Post create(Post post);
    void delete(String id);
    Post updatePostContent(String id, String content);
    Post findByTitle(String title);
}
