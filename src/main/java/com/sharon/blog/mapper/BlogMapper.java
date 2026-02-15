package com.sharon.blog.mapper;

import com.sharon.blog.pojo.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
@Mapper
public interface BlogMapper {
    List<Blog> findAllOrderByCreatedAtDesc();
    //注意这里和service层的命名不同，回顾原因
    //按照id查询博客
    // Mapper 接口
    Blog findById(@Param("id") Long id);//查询可以返回blog
    //保存博客（新增 更新） 犯错：返回类型没有随着更改为mybatis由Blog更改成int
    //Mybatis 的增删改操作（insert、update、delete）只能返回：
    //
    //int（影响的行数）
    //
    //long（影响的行数）
    //
    //boolean（true 表示有影响，false 表示没影响）
    //
    //void（不关心返回值）不能返回实体对象，因为 SQL 执行后不会自动把结果封装成对象
    int insert(Blog blog);
    //删除博客
    void deleteBlog(long blogId);

    int updateBlog(Blog blog);
}
