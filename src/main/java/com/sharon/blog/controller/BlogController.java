package com.sharon.blog.controller;

import com.sharon.blog.entity.Blog;
import com.sharon.blog.repository.BlogRepository;
import com.sharon.blog.service.impl.BlogService;
import com.sharon.blog.util.MarkdownUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
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
    @Autowired
    private MarkdownUtil markdownUtil;

    // 1. 博客列表页：展示所有博客
    @GetMapping("/")
    public String list(Model model, HttpSession session) {

        // 从数据库取出所有博客，按时间倒序（最新的在前面）
        List<Blog> blogs = blogService.getAllBlogs();
        // 把博客列表放进Model，传给前端页面
        model.addAttribute("blogs", blogs);
        // 返回视图名：blog/list.html
        boolean isAdmin = session.getAttribute("adminUser")!=null;
        System.out.println("========== isAdmin: " + isAdmin + " ==========");
        model.addAttribute("showAdminActions", isAdmin);//只有在管理员模式下才能看见编辑和删除按钮
        model.addAttribute("pageTitle", "首页 - 三碳化合物的博客");
        model.addAttribute("isHomePage", true);
        return "blog/list";
    }

    // 2. 博客详情页：展示单篇博客
    @GetMapping("/blog/{id}")
    public String detail(@PathVariable Long id, Model model,HttpSession session) {
        // 根据ID查找博客，Optional是Java 8用来避免空指针的容器
        Optional<Blog> blogOptional = blogService.getBlogById(id);

        // 判断博客是否存在
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            // 存在：把博客对象放进Model，返回详情页
            model.addAttribute("blog", blogOptional.get());
            // 把 Markdown 内容转换成 HTML
            String htmlContent = markdownUtil.render(blog.getContent());

            model.addAttribute("blog", blog);
            model.addAttribute("htmlContent", htmlContent);  // 传给页面的 HTML
            boolean isAdmin = session.getAttribute("adminUser")!=null;
            model.addAttribute("showAdminActions", isAdmin);
            model.addAttribute("pageTitle",
                    blogOptional.get().getTitle()
                            + " - 三碳化合物的博客");
            model.addAttribute("isHomePage", false);
            return "blog/detail";
        } else {
            // 不存在：返回404页面
            model.addAttribute("pageTitle", "页面未找到");
            model.addAttribute("isHomePage", false);
            return "error/404";
        }

    }
    }


