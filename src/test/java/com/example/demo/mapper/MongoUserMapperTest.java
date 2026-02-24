package com.example.demo.mapper;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MongoUserMapperTest {

    private final MongoUserMapper mapper = new MongoUserMapper();

    @Test
    void toEntity_ShouldHandleNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_ShouldMapRequest() {
        UserRequest request = new UserRequest();
        request.name = "John";
        request.email = "john@example.com";
        request.password = "secure";

        User user = mapper.toEntity(request);

        assertAll(
                () -> assertEquals("John", user.getName()),
                () -> assertEquals("john@example.com", user.getEmail()),
                () -> assertEquals("secure", user.getPassword())
        );
    }
}