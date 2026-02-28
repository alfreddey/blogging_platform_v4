package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.repository.custom.CustomUserRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CustomUserRepository, MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
