package com.sharon.blog.controller;

import com.sharon.blog.pojo.Comment;
import com.sharon.blog.service.impl.BlogService;
import com.sharon.blog.service.impl.CommentService;
import com.sharon.blog.util.MarkdownUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.pojo.Blog;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private MarkdownUtil markdownUtil;
    @Autowired
    private CommentService commentService;

    // 1. 博客列表页：展示所有博客
    @GetMapping("/")

    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,// 新增
            Model model,
            HttpSession session) {
        int pageSize = 5;
        PageResult<Blog> pageResult;

        // 判断是搜索还是普通分页
        if (keyword != null && !keyword.trim().isEmpty()) {
            pageResult = blogService.searchBlogs(keyword, page, pageSize);
            model.addAttribute("keyword", keyword);
        } else {
            pageResult = blogService.getBlogsPage(page, pageSize);
        }
        //加日志，看数据
        System.out.println("===== 分页数据 =====");
        System.out.println("当前页: " + pageResult.getPage());
        System.out.println("总记录数: " + pageResult.getTotal());
        System.out.println("总页数: " + pageResult.getTotalPages());
        System.out.println("本页数据条数: " + pageResult.getData().size());
        System.out.println("本页数据: " + pageResult.getData());

        model.addAttribute("pageResult", pageResult);
        model.addAttribute("showAdminActions", session.getAttribute("adminUser") != null);
        model.addAttribute("pageTitle", "首页 - 三碳化合物的博客");
        model.addAttribute("isHomePage", true);

        return "blog/list";
    }

    // 2. 博客详情页：展示单篇博客
    @GetMapping("/blog/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Blog> blogOptional = blogService.getBlogById(id);

        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            String htmlContent = markdownUtil.render(blog.getContent());

            // 获取该博客的留言
            List<Comment> comments = commentService.getCommentsByBlogId(id);

            model.addAttribute("blog", blog);
            model.addAttribute("htmlContent", htmlContent);
            model.addAttribute("comments", comments);  // 传给模板

            boolean isAdmin = session.getAttribute("adminUser") != null;
            model.addAttribute("showAdminActions", isAdmin);
            model.addAttribute("pageTitle", blog.getTitle() + " - 三碳化合物的博客");
            model.addAttribute("isHomePage", false);
            return "blog/detail";
        } else {
            model.addAttribute("pageTitle", "页面未找到");
            model.addAttribute("isHomePage", false);
            return "error/404";
        }
    }

    @PostMapping("/blog/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String nickname,
                             @RequestParam String content){
           Comment comment = new Comment();
           comment.setBlogId(id);
           comment.setNickname(nickname);
           comment.setContent(content);
           commentService.addComment(comment);
           return "redirect:/blog/" + id;
    }
    }


