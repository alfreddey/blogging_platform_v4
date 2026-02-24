package com.example.demo.mapper;

import com.example.demo.dto.PostResponse;
import com.example.demo.model.Post;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MongoPostMapperTest {

    private final MongoPostMapper mapper = new MongoPostMapper();

    @Test
    void toResponse_ShouldMapAllFields() {
        Post post = new Post();
        post.setId("507f1f77bcf86cd799439011");
        post.setTitle("Java");
        post.setTags(List.of("coding"));

        PostResponse response = mapper.toResponse(post);

        assertEquals(post.getId(), response.getId());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(1, response.getTags().size());
    }
}