package com.sharon.blog.mapper;

import com.sharon.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    int insert(Comment comment);

    List<Comment> findById(@Param("blogId") Long id);

    int deleteById(@Param("id") Long id);

    int countByBlogId(@Param("blogId") Long blogId);

}
