package com.sharon.blog.pojo;
//建立了Java对象与数据库之间的映射规则，表名为blog，JPA把这个类翻译成了建表语句
import java.time.LocalDateTime;


import lombok.Data;
@Data
public class Blog {
    private Long id;//犯错：这里改成包装类后，blogmapper xml有几个位置参数得改

    private String title;

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
