package com.sharon.blog.controller;

import com.sharon.blog.entity.Blog;
import com.sharon.blog.repository.BlogRepository;
import com.sharon.blog.service.impl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;

    // 1. 博客列表页：展示所有博客
    @GetMapping("/")
    public String list(Model model) {
        // 从数据库取出所有博客，按时间倒序（最新的在前面）
        List<Blog> blogs = blogService.getAllBlogs();
        // 把博客列表放进Model，传给前端页面
        model.addAttribute("blogs", blogs);
        // 返回视图名：blog/list.html
        model.addAttribute("showAdminActions", true);
        model.addAttribute("pageTitle", "首页 - 三碳化合物的博客");
        return "blog/list";
    }

    // 2. 博客详情页：展示单篇博客
    @GetMapping("/blog/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // 根据ID查找博客，Optional是Java 8用来避免空指针的容器
        Optional<Blog> blogOptional = blogService.getBlogById(id);

        // 判断博客是否存在
        if (blogOptional.isPresent()) {
            // 存在：把博客对象放进Model，返回详情页
            model.addAttribute("blog", blogOptional.get());
            model.addAttribute("pageTitle",
                    blogOptional.get().getTitle()
                            + " - 三碳化合物的博客");
            return "blog/detail";
        } else {
            // 不存在：返回404页面
            return "error/404";
        }

    }
    }


