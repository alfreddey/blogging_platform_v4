package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Post;
import com.example.demo.repository.interfaces.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MongoPostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private MongoPostService postService;

    @Test
    void getAll_ShouldReturnPagedContent() {
        Post p1 = new Post();
        p1.setId("1");
        p1.setTitle("A");

        Post p2 = new Post();
        p2.setId("2");
        p2.setTitle("B");

        when(postRepository.findAll(ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(p1, p2)));

        var result = postService.getAll(0, 2, "title");

        assertEquals(2, result.size());
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
    }

    @Test
    void getById_ShouldReturnFromRepo_WhenExists() {
        Post mockPost = new Post();
        mockPost.setId("1");
        mockPost.setTitle("Title");

        when(postRepository.findById("1")).thenReturn(Optional.of(mockPost));

        Post result = postService.getById("1");

        assertEquals(mockPost, result);
        verify(postRepository).findById("1");
    }

    @Test
    void getById_ShouldThrowException_WhenNotFoundInRepo() {
        when(postRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getById("1"));
    }

    @Test
    void create_ShouldSaveToRepo() {
        Post newPost = new Post();
        newPost.setId("1");
        newPost.setTitle("Title");

        when(postRepository.save(newPost)).thenReturn(newPost);

        Post result = postService.create(newPost);

        assertEquals(newPost, result);
        verify(postRepository).save(newPost);
    }

    @Test
    void delete_ShouldCallRepositoryDeleteById() {
        postService.delete("1");

        verify(postRepository).deleteById("1");
    }

    @Test
    void updatePostContent_ShouldReturnUpdatedPost() {
        Post updated = new Post();
        updated.setId("1");
        updated.setTitle("Updated");

        when(postRepository.updatePostContent("1", "new content")).thenReturn(updated);

        Post result = postService.updatePostContent("1", "new content");

        assertEquals(updated, result);
        verify(postRepository).updatePostContent("1", "new content");
    }

    @Test
    void findByTitle_ShouldReturnPost_WhenFound() {
        Post mockPost = new Post();
        mockPost.setId("3");
        mockPost.setTitle("Search Result term");

        when(postRepository.findByTitle("term")).thenReturn(mockPost);

        Post result = postService.findByTitle("term");

        assertEquals(mockPost, result);
    }

    @Test
    void findByTitle_ShouldThrowException_WhenNoMatch() {
        when(postRepository.findByTitle("none")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> postService.findByTitle("none"));
    }
}