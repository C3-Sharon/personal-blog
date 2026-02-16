package com.sharon.blog.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class Comment {
    private Long id;
    private Long blogId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
}
