package com.sharon.blog.entity;
//建立了Java对象与数据库之间的映射规则，表名为blog
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity//告诉JPA这个类要映射成数据库的一张表
public class Blog {
    @Id//告诉JPA这个字段是数据库表的主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键自动增长，不用手动设置id
    private long id;
    private String title;
    private String content;
    private LocalDateTime createTime;
    public Blog() {}
    public Blog( String title, String content, LocalDateTime createTime) {
        //这里不用传id，不用手动设置
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }
}
