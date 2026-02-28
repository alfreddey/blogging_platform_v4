package com.example.demo.repository.interfaces;

import com.example.demo.model.Post;
import com.example.demo.repository.interfaces.custom.CustomPostRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, CustomPostRepository {
    Post findByTitle(String title);
}
