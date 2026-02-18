package com.sharon.blog.controller;

import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.Comment;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.service.impl.BlogService;
import com.sharon.blog.service.impl.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ApiBlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/blogs")
    public Map<String, Object> getBlogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword) {

        PageResult<Blog> pageResult;

        if (keyword != null && !keyword.trim().isEmpty()) {
            pageResult = blogService.searchBlogs(keyword, page, size);
        } else {
            pageResult = blogService.getBlogsPage(page, size);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", pageResult.getData());
        result.put("page", pageResult.getPage());
        result.put("size", pageResult.getSize());
        result.put("total", pageResult.getTotal());
        result.put("totalPages", pageResult.getTotalPages());

        return result;
    }
    @GetMapping("/blogs/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id).orElse(null);
    }

    @PostMapping("/admin/login")
    public Map<String,Object> login(@RequestParam String username,
                                    @RequestParam String password,
                                    HttpSession session) {
        Map<String,Object> result = new HashMap<>();//记得学习map相关
        if("siriisthree".equals(username) && "523123".equals(password)) {
            session.setAttribute("adminUser", username);
            result.put("success", true);
            result.put("message","成功登录");
            result.put("username", username);
        }else{
            result.put("success", false);
            result.put("message","用户名或密码有误");
        }
        return result;
    }
@PostMapping("/admin/blogs")
    public Map<String,Object> createBlog(@RequestBody Blog blog) {
    Map<String, Object> result = new HashMap<>();
    try {
        blog.setCreatedAt(LocalDateTime.now());
        Blog savedBlog = blogService.saveBlog(blog);
        result.put("success", true);
        result.put("message", "成功发布");
        result.put("data", savedBlog);
    } catch (Exception e) {
        result.put("success", false);
        result.put("message", "发布失败" + e.getMessage());
    }
    return result;
}
      @PostMapping ("/admin/blogs/{id}")
  public Map<String,Object> updateBlog(@PathVariable Long id,@RequestBody Blog blog){
        Map<String,Object> result=new HashMap<>();
        try {
              blog.setId(id);
              Blog updateBlog =blogService.saveBlog(blog);
              result.put("success", true);
              result.put("message","成功更新");
              result.put("data",updateBlog);
            }catch (Exception e){
              result.put("success", false);
              result.put("message","更新失败"+e.getMessage());
         }
          return result;
    }
    @GetMapping("/admin/logout")
    public Map<String,Object> logout(HttpSession session){
        session.invalidate();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message","成功退出");
        return result;
    }
    @PostMapping("/admin/delete/{id}")
    public Map<String,Object> deleteBlog(@PathVariable Long id){
        Map<String,Object> result=new HashMap<>();
        try {
            blogService.deleteBlog(id);
            result.put("success", true);
            result.put("message","删除成功");
        }catch (Exception e){
            result.put("success", false);
            result.put("message","删除失败"+e.getMessage());
        }
        return result;
    }
    @GetMapping("/admin/check")
    public Map<String, Object> checkLogin(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        boolean isLoggedIn = session.getAttribute("adminUser") != null;

        result.put("isLoggedIn", isLoggedIn);
        if (isLoggedIn) {
            result.put("username", session.getAttribute("adminUser"));
        }

        return result;
    }

    @GetMapping("/blogs/{blogId}/comments")
    public List<Comment> getComments(@PathVariable Long blogId){
        return commentService.getCommentsByBlogId(blogId);
    }

    @PostMapping("/blogs/{blogId}/comments")
    public Map<String,Object> addComment(@PathVariable Long blogId,
                                         @RequestBody Comment comment){
        Map<String,Object> result=new HashMap<>();
        try {
            comment.setCreatedAt(LocalDateTime.now());
            comment.setBlogId(blogId);
            Comment savedComment=commentService.addComment(comment);
            result.put("success", true);
            result.put("message","成功留言");
            result.put("data",savedComment);
        }catch (Exception e){
            result.put("success", false);
            result.put("message","留言失败"+e.getMessage());
        }
        return result;
    }
    @DeleteMapping("/admin/comments/{id}")
    public Map<String,Object> deleteComment(@PathVariable Long id,HttpSession session){
        Map<String,Object> result=new HashMap<>();
        System.out.println("========== 删除留言 ==========");
        System.out.println("接收到的留言ID: " + id);
        System.out.println("ID类型: " + id.getClass().getName());
        System.out.println("session中的adminUser: " + session.getAttribute("adminUser"));
        if(session.getAttribute("adminUser") == null){
            result.put("success", false);
            result.put("message","未登录");
            return result;
        }
        try{
            System.out.println("开始调用 service.deleteComment，ID: " + id);
            commentService.deleteComment(id);
            System.out.println("service 调用完成");
            commentService.deleteComment(id);
            result.put("success", true);
            result.put("message","删除成功");
        }catch (Exception e){
            result.put("success", false);
            result.put("message","删除失败"+e.getMessage());
        }
        return result;
    }
}