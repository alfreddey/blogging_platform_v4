package com.example.demo.repository;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public Comment save(String postId, Comment comment) {
        var query = new Query(Criteria.where("_id").is(postId));
        var update = new Update().push("comments", comment);

        var post = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Post.class);

        if (post == null || post.getComments() == null || post.getComments().isEmpty()) {
            throw new RuntimeException("Post with id: %s not found or update failed".formatted(postId));
        }

        List<Comment> comments = post.getComments();

        return comments.get(comments.size() - 1);
    }

    @Override
    public Comment findById(String postId, String commentId) {
        var query = new Query(Criteria.where("_id").is(postId).and("comments._id").is(commentId));

        query.fields().include("comments.$");

        var post = mongoTemplate.findOne(query, Post.class);

        if (post == null || post.getComments() == null || post.getComments().isEmpty()) {
            throw new ResourceNotFoundException("Comment with id: %s and postId: %s not found".formatted(commentId, postId));
        }

        return post.getComments().get(0);
    }

    @Override
    public List<Comment> findAll(String postId, int page, int size, String sortBy) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(postId)),
                Aggregation.unwind("comments"),
                Aggregation.replaceRoot("comments"),
                Aggregation.sort(Sort.Direction.ASC, sortBy),
                Aggregation.skip((long) page * size),
                Aggregation.limit(size)
        );

        return mongoTemplate.aggregate(aggregation, "posts", Comment.class).getMappedResults();
    }

    @Override
    public Comment updateText(String postId, String commentId, String newText) {
        var query = new Query(Criteria.where("_id").is(postId).and("comments._id").is(commentId));
        var update = new Update().set("comments.$.text", newText);

        var post = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Post.class);

        var exception = new ResourceNotFoundException("Comment with id: %s and postId: %s not found".formatted(commentId, postId));

        if (post == null) {
            throw exception;
        }

        return post.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> exception);
    }

    @Override
    public void deleteById(String postId, String commentId) {
        var query = new Query(Criteria.where("_id").is(postId));
        var update = new Update().pull("comments._id", commentId);

        mongoTemplate.updateFirst(query, update, Post.class);
    }
}
