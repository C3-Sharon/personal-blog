package com.sharon.blog.controller;

import com.sharon.blog.pojo.ArtWork;
import com.sharon.blog.pojo.PageResult;
import com.sharon.blog.service.impl.ArtWorkService;
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
    public Map<String,Object> getArtWorks(@RequestParam(defaultValue = "paintings") String category,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "12") int size){
        PageResult<ArtWork> pageResult=artWorkService.getPageByCategory(category,page,size);
        Map<String,Object> result=new HashMap<>();
        result.put("page",pageResult.getPage());
        result.put("size",pageResult.getSize());
        result.put("total",pageResult.getTotal());
        result.put("data",pageResult.getData());
        return result;
    }
    @GetMapping("/{id}")
    public ArtWork getArtWork(@PathVariable Long id){
        return artWorkService.getById(id);
    }
    // 上传作品
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadArtwork(
            @RequestParam String title,
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();

        // 检查登录
        if (session.getAttribute("adminUser") == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
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
            artwork.setCreatedAt(LocalDateTime.now());
            artwork.setUpdatedAt(artwork.getCreatedAt());

            artWorkService.saveArtwork(artwork);

            result.put("success", true);
            result.put("message", "上传成功");
            result.put("data", artwork);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
        }

        return result;
    }
    @PutMapping("/{id}")
    public Map<String,Object> updateArtwork(@PathVariable Long id,
                                            @RequestBody ArtWork artwork,
                                            HttpSession session){
        Map<String,Object> result = new HashMap<>();
        if (session.getAttribute("adminUser") == null) {
            result.put("success", false);
            result.put("message","未登录");
            return result;
        }try{
            artwork.setId(id);
            artwork.setUpdatedAt(LocalDateTime.now());
            artWorkService.saveArtwork(artwork);
            result.put("success", true);
            result.put("message","成功更新");
            result.put("data", artwork);
        }catch (Exception e){
            result.put("success", false);
            result.put("message","更新失败" + e.getMessage());
        }
        return result;
    }
    @DeleteMapping("/{id}")
    public Map<String,Object> deleteArtwork(@PathVariable Long id,HttpSession session){
        Map<String,Object> result = new HashMap<>();
        if (session.getAttribute("adminUser") == null) {
            result.put("success", false);
            result.put("message","未登录");
            return result;
        }try{
            artWorkService.deleteArtwork(id);
            result.put("success", true);
            result.put("message","成功删除");
            result.put("data", id);
        }catch (Exception e){
            result.put("success", false);
            result.put("message","删除失败"+e.getMessage());
        }
        return result;
    }
}
