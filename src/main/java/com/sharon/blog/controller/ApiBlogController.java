package com.sharon.blog.controller;

import com.sharon.blog.pojo.Blog;
import com.sharon.blog.pojo.Comment;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.service.impl.BlogService;
import com.sharon.blog.service.impl.CommentService;
import com.sharon.blog.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
class LoginStatus {
    private boolean isLoggedIn;
    private String username;
}

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ApiBlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/blogs")
    public Result<PageResult<Blog>> getBlogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword) {

        PageResult<Blog> pageResult;

        if (keyword != null && !keyword.trim().isEmpty()) {
            pageResult = blogService.searchBlogs(keyword, page, size);
        } else {
            pageResult = blogService.getBlogsPage(page, size);
        }

       return Result.ok(pageResult);
    }

    @GetMapping("/blogs/{id}")
    public Result<Blog> getBlogById(@PathVariable Long id) {
        return Result.ok(blogService.getBlogById(id).orElse(null));
    }

    @PostMapping("/admin/login")
    public Result<LoginStatus> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        System.out.println("期待账号: " + adminUsername + "，收到账号: " + username);
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            session.setAttribute("adminUser", username);
            return Result.ok(new LoginStatus(true,username));
        } else {
            return Result.error("用户名或密码错误");
        }
    }

@PostMapping("/admin/blogs")
    public Result<Blog> createBlog(@RequestBody Blog blog,HttpSession session) {

    if (session.getAttribute("adminUser") == null) {
        return Result.error("请先登录");
    }
        blog.setCreatedAt(LocalDateTime.now());
        Blog savedBlog = blogService.saveBlog(blog);
        return Result.ok("成功发布",savedBlog);

}

@PostMapping ("/admin/blogs/{id}")
  public Result<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog blog, HttpSession session) {
          if (session.getAttribute("adminUser") == null) {
              return Result.error("未登录");
          }
              blog.setId(id);
              Blog updateBlog =blogService.saveBlog(blog);
              return Result.ok("成功更新",updateBlog);
    }

    @GetMapping("/admin/logout")
    public Result<LoginStatus> logout(HttpSession session){
        session.invalidate();
        return Result.ok();
    }

    @PostMapping("/admin/delete/{id}")
    public Result<Void> deleteBlog(@PathVariable Long id, HttpSession session){
        if (session.getAttribute("adminUser") == null) {
            return Result.error("请先登录");
        }
            blogService.deleteBlog(id);
            return Result.ok();
    }

    @GetMapping("/admin/check")
    public Result<LoginStatus> checkLogin(HttpSession session) {
        Object adminUser = session.getAttribute("adminUser");
        System.out.println("检查登录态，Session中的用户: " + adminUser);
        boolean isLoggedIn = adminUser != null;
        if(isLoggedIn){
            return Result.ok(new LoginStatus(true,adminUser.toString()));
        }else {
            return Result.ok(new LoginStatus(false,null));
        }
    }

    @GetMapping("/blogs/{blogId}/comments")
    public Result<List<Comment>> getComments(@PathVariable Long blogId){
        return Result.ok(commentService.getCommentsByBlogId(blogId));
    }

    @PostMapping("/blogs/{blogId}/comments")
    public Result<Comment> addComment(@PathVariable Long blogId, @RequestBody Comment comment){
            comment.setCreatedAt(LocalDateTime.now());
            comment.setBlogId(blogId);
            Comment savedComment=commentService.addComment(comment);
            return Result.ok("留言成功",savedComment);
    }

    @DeleteMapping("/admin/comments/{id}")
    public Result<Comment> deleteComment(@PathVariable Long id,HttpSession session){

        if(session.getAttribute("adminUser") == null){
            return Result.error("未登录");
        }
            System.out.println("开始调用 service.deleteComment，ID: " + id);
            commentService.deleteComment(id);
            System.out.println("service 调用完成");
            return Result.ok();

    }
}