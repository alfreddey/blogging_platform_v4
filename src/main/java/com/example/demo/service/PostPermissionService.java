package com.example.demo.service;

import com.example.demo.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostPermissionService {
    private final PostRepository postRepository;

    public boolean canEditPost(String postId, String userEmail) {
        var post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        return post.getAuthorEmail().equals(userEmail);
    }
}
