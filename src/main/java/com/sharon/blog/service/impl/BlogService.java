package com.sharon.blog.service.impl;

import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.PageResult;

import java.util.List;
import java.util.Optional;

public interface BlogService {

    List<Blog> getAllBlogs();

    Optional<Blog> getBlogById(Long id);

    Blog saveBlog(Blog blog);

    void deleteBlog(long blogId);

    PageResult<Blog> getBlogsPage(int page, int size);
    PageResult<Blog> searchBlogs(String keyword,int page,int size);

}
