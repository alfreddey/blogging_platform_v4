package com.example.demo.repository.custom;

import com.example.demo.entity.Post;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public Post updatePostContent(String postId, String content) {
        var query = new Query(Criteria.where("id").is(postId));
        var update = new Update().set("content", content);

        return mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Post.class);
    }
}
