package com.sharon.blog.service.impl;

import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.PageResult;

import java.util.List;
import java.util.Optional;

public interface BlogService {


    //查询所有博客
    List<Blog> getAllBlogs();
    //按照id查询博客

    Optional<Blog> getBlogById(Long id);

    //保存博客（新增 更新）
    Blog saveBlog(Blog blog);
    //删除博客
    void deleteBlog(long blogId);
    //更改博客不需要单独写方法，因为save Blog既能处理新增，也能处理更新（JPA save方法的特性）
    //TODO:待改进：单独写update的时机：只想更新特定字段，不想把所有字段都传过来，比如只更新博客的 title，不碰 content 和 createdAt
//分页
    PageResult<Blog> getBlogsPage(int page, int size);
    PageResult<Blog> searchBlogs(String keyword,int page,int size);
//到底什么时候用blog，什么时候用pageresult
}
