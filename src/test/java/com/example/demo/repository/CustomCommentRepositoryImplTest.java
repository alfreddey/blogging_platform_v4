package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.repository.interfaces.CommentRepositoryImpl;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.mongodb.client.result.UpdateResult;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomCommentRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CommentRepositoryImpl commentRepository;

    @Test
    void findById_ShouldReturnSpecificComment() {
        String postId = "post-123";
        String commentId = "comm-456";

        Comment foundComment = new Comment();
        foundComment.setId(commentId);

        Post mockPost = new Post();
        mockPost.setComments(List.of(foundComment));

        when(mongoTemplate.findOne(any(Query.class), eq(Post.class)))
                .thenReturn(mockPost);

        Comment result = commentRepository.findById(postId, commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
    }

    @Test
    void save_ShouldReturnLastCommentInArray() {
        String postId = "post-123";
        Comment inputComment = new Comment();
        inputComment.setText("Fresh Comment");

        Comment persistedComment = new Comment();
        persistedComment.setId("mongo-gen-id");
        persistedComment.setText("Fresh Comment");

        Post mockPost = new Post();
        mockPost.setComments(List.of(new Comment(), persistedComment));

        when(mongoTemplate.findAndModify(any(), any(), any(), eq(Post.class)))
                .thenReturn(mockPost);

        Comment result = commentRepository.save(postId, inputComment);

        assertEquals("mongo-gen-id", result.getId());
        assertEquals("Fresh Comment", result.getText());
    }

    @Test
    void updateText_ShouldReturnUpdatedComment() {
        // Arrange
        String postId = "post-123";
        String commentId = "comm-456";
        String newText = "Revised Comment";

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setText(newText);

        Post post = new Post();
        post.setComments(List.of(comment));

        when(mongoTemplate.findAndModify(
                any(Query.class),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Post.class)
        )).thenReturn(post);

        Comment result = commentRepository.updateText(postId, commentId, newText);

        assertNotNull(result);
        assertEquals(newText, result.getText());
        verify(mongoTemplate).findAndModify(any(), any(), any(), eq(Post.class));
    }

    @Test
    void findAll_ShouldReturnPaginatedComments() {
        // Arrange
        String postId = "post-123";
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";

        Comment comment = new Comment();
        AggregationResults<Comment> results =
                new AggregationResults<>(List.of(comment), new Document());

        when(mongoTemplate.aggregate(any(Aggregation.class), eq("posts"), eq(Comment.class)))
                .thenReturn(results);

        // Act
        List<Comment> list = commentRepository.findAll(postId, page, size, sortBy);

        // Assert
        assertEquals(1, list.size());
    }


    @Test
    void deleteById_ShouldCallUpdateFirstWithPull() {
        String postId = "post-123";
        String commentId = "comm-456";
        UpdateResult mockResult = mock(UpdateResult.class);

        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Post.class)))
                .thenReturn(mockResult);

        commentRepository.deleteById(postId, commentId);

        verify(mongoTemplate, times(1)).updateFirst(
                argThat(query -> query.getQueryObject().get("_id").equals(postId)),
                argThat(update -> update.getUpdateObject().containsKey("$pull")),
                eq(Post.class)
        );
    }
}