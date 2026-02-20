package com.sharon.blog.service.impl;

import com.sharon.blog.mapper.BlogMapper;
import com.sharon.blog.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sharon.blog.pojo.PageResult;
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
    public PageResult<Blog> searchBlogs(String keyword, int page, int size) {
        int offset = (page - 1) * size;

        List<Blog> blogs = blogMapper.searchBlogs(keyword, offset, size);
        int total = blogMapper.countSearch(keyword);

        int totalPages = (total + size - 1) / size;

        PageResult<Blog> result = new PageResult<>();
        result.setData(blogs);
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setTotalPages(totalPages);
        return result;
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {

        Blog blog = blogMapper.findById(id);

        return Optional.ofNullable(blog);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getCreatedAt() == null) {
            blog.setCreatedAt(LocalDateTime.now());
        }

        if (blog.getId() == null) {

            blogMapper.insert(blog);
        } else {

            blogMapper.updateBlog(blog);
        }
        return blog;
    }

    @Override
    public void deleteBlog(long blogId) {
        blogMapper.deleteBlog(blogId);

    }

    @Override
    public PageResult<Blog> getBlogsPage(int page, int size) {
        int offset = (page - 1) * size;
        List<Blog> blogs = blogMapper.selectPage(offset, size);
        int total=blogMapper.countAll();
        int totalPages=(total+size-1)/size;
        PageResult<Blog> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setTotalPages(totalPages);
        pageResult.setSize(size);
        pageResult.setPage(page);
        pageResult.setData(blogs);
        return pageResult;
    }
}
