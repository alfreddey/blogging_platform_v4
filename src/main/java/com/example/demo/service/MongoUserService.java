package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.UserService;
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
@CacheConfig(cacheNames = "users")
public class MongoUserService implements UserService {
    private final UserRepository userRepository;

    public MongoUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable
    @Override
    public User getById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
    }

    @Cacheable
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with email: " + email + " not found"));
    }

    @CachePut(key = "#result.id")
    @Transactional
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(key = "#userId")
    @Transactional
    @Override
    public void delete(String userId) {
        userRepository.deleteById(userId);
    }

    @CachePut(key = "#userId")
    @Transactional
    @Override
    public User updatePassword(String userId, String password) {
        return userRepository.updatePasswordById(userId, password);
    }

    @Override
    public List<User> getAll(int page, int size, String sortBy) {
        var pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable).getContent();
    }
}
