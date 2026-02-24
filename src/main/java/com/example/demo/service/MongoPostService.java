package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Post;
import com.example.demo.repository.interfaces.PostRepository;
import com.example.demo.service.interfaces.PostService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "posts")
public class MongoPostService implements PostService {
    private final PostRepository postRepository;

    public MongoPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAll(int page, int size, String sortBy) {
        var pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        return postRepository.findAll(pageable).getContent();
    }

    @Cacheable
    @Override
    public Post getById(String postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    @CachePut(key = "#result.id")
    @Transactional
    @Override
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @CacheEvict(key = "#postId")
    @Transactional
    @Override
    public void delete(String postId) {
        postRepository.deleteById(postId);
    }

    @CachePut(key = "#postId")
    @Transactional
    @Override
    public Post updatePostContent(String postId, String content) {
        return postRepository.updatePostContent(postId, content);
    }

    @Cacheable(key = "#title")
    @Override
    public Post findByTitle(String title) {
        var post = postRepository.findByTitle(title);

        if (post != null) {
            return post;
        }

        throw new ResourceNotFoundException("Post not found with title: " + title);
    }
}
