package com.sharon.blog.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ArtWork {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private String fileType;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
