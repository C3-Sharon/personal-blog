package com.sharon.blog.service.impl;

import com.sharon.blog.entity.Blog;
import com.sharon.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Optional<Blog> getBlogById(long blogId) {
        return blogRepository.findById(blogId);
    }


    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }
        return blogRepository.save(blog);
    }

    @Override
    public void deleteBlog(long blogId) {
        blogRepository.deleteById(blogId);

    }


    }
