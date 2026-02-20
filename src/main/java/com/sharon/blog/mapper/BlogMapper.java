package com.sharon.blog.mapper;
import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> findAllOrderByCreatedAtDesc();

    Blog findById(@Param("id") Long id);

    int insert(Blog blog);

    void deleteBlog(long blogId);

    int updateBlog(Blog blog);

    List<Blog> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    int countAll();

    List<Blog> searchBlogs(@Param("keyword") String keyword,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    int countSearch(@Param("keyword") String keyword);
}
