package com.example.demo.repository.interfaces.custom;

import com.example.demo.model.Post;

public interface CustomPostRepository {
    Post updatePostContent(String postId, String content);
}
