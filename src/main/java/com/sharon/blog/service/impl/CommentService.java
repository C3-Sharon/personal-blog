package com.sharon.blog.service.impl;

import com.sharon.blog.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByBlogId(Long blogId);
    Comment addComment(Comment comment);
    void deleteComment(Long id);
}
