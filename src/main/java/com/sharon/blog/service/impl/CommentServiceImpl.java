package com.sharon.blog.service.impl;

import com.sharon.blog.mapper.CommentMapper;
import com.sharon.blog.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByBlogId(Long blogId) {
        return commentMapper.findById(blogId);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public void deleteComment(Long id) {
        commentMapper.deleteById(id);
    }
}
