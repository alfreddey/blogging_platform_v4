package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    private String id;
    private String authorEmail;
    private String text;

    @CreatedDate
    private Date createdAt;
}
