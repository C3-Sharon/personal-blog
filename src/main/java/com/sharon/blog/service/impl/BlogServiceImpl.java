package com.sharon.blog.service.impl;

import com.sharon.blog.pojo.Blog;
import com.sharon.blog.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public List<Blog> getAllBlogs() {
        return blogMapper.findAllOrderByCreatedAtDesc();
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        // Mapper 返回 Blog 或 null
        Blog blog = blogMapper.findById(id);
        // 手动包装成 Optional
        return Optional.ofNullable(blog);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }
//因为这个判断将id类型从long改成了Long
        if (blog.getId() == null) {
            // 新增
            blogMapper.insert(blog);
        } else {
            // 更新
            blogMapper.updateBlog(blog);
        }
        return blog;
    }

    @Override
    public void deleteBlog(long blogId) {
        blogMapper.deleteBlog(blogId);

    }
//20260215犯错：调用了类名而不是对象

    }
