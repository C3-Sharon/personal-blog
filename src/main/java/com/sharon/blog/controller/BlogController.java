package com.sharon.blog.controller;

import com.sharon.blog.service.impl.BlogService;
import com.sharon.blog.util.MarkdownUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.pojo.Blog;
import java.util.Optional;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private MarkdownUtil markdownUtil;

    // 1. 博客列表页：展示所有博客
    @GetMapping("/")

    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,// 新增
            Model model,
            HttpSession session) {
        int pageSize = 3;
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


