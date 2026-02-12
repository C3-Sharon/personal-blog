package com.sharon.blog.entity;
//建立了Java对象与数据库之间的映射规则，表名为blog，JPA把这个类翻译成了建表语句
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity//告诉JPA这个类要映射成数据库的一张表
public class Blog {
    @Id//告诉JPA这个字段是数据库表的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键自动增长，不用手动设置id
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 100000)
    private String content;

    private LocalDateTime createdAt;
    public Blog() {}
    public Blog( String title, String content, LocalDateTime createdAt) {
        //这里不用传id，不用手动设置
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
