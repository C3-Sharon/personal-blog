package com.sharon.blog.controller;

import com.sharon.blog.pojo.ArtWork;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.service.impl.ArtWorkService;
import com.sharon.blog.service.impl.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class GalleryController {
    @Autowired
    private ArtWorkService artWorkService;
@GetMapping("/gallery")
    public String gallery(@RequestParam(defaultValue = "painting") String category,
                          @RequestParam(defaultValue = "1") int page,
                          Model model,
                          HttpSession session){
        int pageSize = 6;
        PageResult<ArtWork> pageResult = artWorkService.getPageByCategory(category, page, pageSize);
        model.addAttribute("category", category);
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("showAdminActions", session.getAttribute("adminUser") != null);
        model.addAttribute("pageTitle", "赛博展厅 - 三碳化合物的博客");

        return "gallery";
    }
    // 上传页面
    @GetMapping("/admin/gallery/upload")
    public String uploadForm(Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        return "gallery-form";
    }

    // 处理上传
    @PostMapping("/admin/gallery/upload")
    public String upload(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        try {
            // 保存文件
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            String projectPath = System.getProperty("user.dir");
            String uploadPath = projectPath + File.separator + "uploads" + File.separator + "gallery";
            File uploadFolder = new File(uploadPath);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            File dest = new File(uploadPath + File.separator + filename);
            file.transferTo(dest);

            // 保存作品信息
            ArtWork artwork = new ArtWork();
            artwork.setTitle(title);
            artwork.setCategory(category);
            artwork.setDescription(description);
            artwork.setFilePath("/uploads/gallery/" + filename);
            artwork.setFileType(file.getContentType());

            artWorkService.saveArtwork(artwork);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/gallery?category=" + category;
    }

    // 编辑页面
    @GetMapping("/admin/gallery/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        ArtWork artwork = artWorkService.getById(id);
        model.addAttribute("artwork", artwork);
        return "gallery-form";
    }

    // 处理编辑
    @PostMapping("/admin/gallery/update/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String description,
            HttpSession session) {

        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        ArtWork artwork = artWorkService.getById(id);
        if (artwork != null) {
            artwork.setTitle(title);
            artwork.setCategory(category);
            artwork.setDescription(description);
            artWorkService.saveArtwork(artwork);
        }

        return "redirect:/gallery?category=" + category;
    }

    // 删除作品
    @PostMapping("/admin/gallery/delete/{id}")
    public String delete(@PathVariable Long id,
                         @RequestParam(required = false) String category,
                         HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        artWorkService.deleteArtwork(id);
        return "redirect:/gallery?category=" + category;
    }
}

