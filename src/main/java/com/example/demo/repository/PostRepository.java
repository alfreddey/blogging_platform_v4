package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.repository.custom.CustomPostRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, CustomPostRepository {
    Post findByTitle(String title);
}
