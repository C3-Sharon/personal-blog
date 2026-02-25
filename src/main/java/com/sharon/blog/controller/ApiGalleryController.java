package com.sharon.blog.controller;

import com.sharon.blog.pojo.ArtWork;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.service.impl.ArtWorkService;
import com.sharon.blog.util.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/gallery")
public class ApiGalleryController {
    @Autowired
    private ArtWorkService artWorkService;
    @GetMapping
    public Result<PageResult<ArtWork>> getArtWorks(@RequestParam(defaultValue = "painting") String category,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "12") int size){
        PageResult<ArtWork> pageResult=artWorkService.getPageByCategory(category,page,size);
        System.out.println("查询分类: " + category);
        System.out.println("数据库返回记录数: " + (pageResult.getData() != null ? pageResult.getData().size() : "null"));
        return Result.ok(pageResult);
    }
    @GetMapping("/{id}")
    public Result<ArtWork> getArtWork(@PathVariable Long id){
        return Result.ok(artWorkService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<ArtWork> uploadArtwork(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        if (session.getAttribute("adminUser") == null) {
            return Result.error("请先登录");
        }
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

            ArtWork artwork = new ArtWork();
            artwork.setTitle(title);
            artwork.setCategory(category);
            artwork.setDescription(description);
            artwork.setFilePath("/uploads/gallery/" + filename);
            artwork.setFileType(file.getContentType());
            artwork.setCreatedAt(LocalDateTime.now());
            artwork.setUpdatedAt(artwork.getCreatedAt());

            artWorkService.saveArtwork(artwork);

            return Result.ok(artwork);

        }

    @PutMapping("/{id}")
    public Result<ArtWork> updateArtwork(@PathVariable Long id,
                                @RequestBody ArtWork artwork,
                                HttpSession session){
        if (session.getAttribute("adminUser") == null) {
            return Result.error("请先登录");
        }
            artwork.setId(id);
            artwork.setUpdatedAt(LocalDateTime.now());
            artWorkService.saveArtwork(artwork);
            return Result.ok(artwork);
    }
    @DeleteMapping("/{id}")
    public Result<Void> deleteArtwork(@PathVariable Long id,HttpSession session){
        if (session.getAttribute("adminUser") == null) {
            return Result.error("请先登录");
        }
            artWorkService.deleteArtwork(id);
            return Result.ok();
    }
}
