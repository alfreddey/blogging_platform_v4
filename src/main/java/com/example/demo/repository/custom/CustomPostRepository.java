package com.example.demo.repository.custom;

import com.example.demo.entity.Post;

public interface CustomPostRepository {
    Post updatePostContent(String postId, String content);
}
