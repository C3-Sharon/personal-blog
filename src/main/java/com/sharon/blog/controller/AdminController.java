package com.sharon.blog.controller;

import com.sharon.blog.entity.Blog;
import com.sharon.blog.service.impl.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
private BlogService blogService;

    @GetMapping("/new")
    public String newBlogForm(Model model) {
        model.addAttribute("pageTitle", "发布新博客");
        return "/admin/new";
    }
@PostMapping("/blogs")//这里一开始忘掉了导致博客点击上传后跳转到404
    public String saveBlog(@RequestParam("title") String title,
                           @RequestParam("content")String content){
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setCreatedAt(LocalDateTime.now());
        blogService.saveBlog(blog);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Blog> blogOptional = blogService.getBlogById(id);
        if (blogOptional.isPresent()) {
            model.addAttribute("blog", blogOptional.get());
            model.addAttribute("pageTitle",
                    "编辑博客 - "
                            + blogOptional.get().getTitle());
            return "admin/edit";
        }else{
            return "error/404";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBlog(@PathVariable Long id,
                             @RequestParam(value = "title")String title,
                             @RequestParam("content")String content ) {
        Optional<Blog> blogOptional = blogService.getBlogById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            blog.setTitle(title);
            blog.setContent(content);
            blogService.saveBlog(blog);
        }
        return "redirect:/";
        }

        @PostMapping("/delete/{id}")
        public String deleteBlog(@PathVariable Long id){
        blogService.deleteBlog(id);
        return "redirect:/";
        }
}
