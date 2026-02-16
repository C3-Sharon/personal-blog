package com.sharon.blog.mapper;
import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.PageResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {
    List<Blog> findAllOrderByCreatedAtDesc();
    //注意这里和service层的命名不同，回顾原因
    //按照id查询博客
    // Mapper 接口
    Blog findById(@Param("id") Long id);//查询可以返回blog

    //void（不关心返回值）不能返回实体对象，因为 SQL 执行后不会自动把结果封装成对象
    int insert(Blog blog);
    //删除博客
    void deleteBlog(long blogId);

    int updateBlog(Blog blog);

    List<Blog> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    int countAll();

    List<Blog> searchBlogs(@Param("keyword") String keyword,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    int countSearch(@Param("keyword") String keyword);
}
