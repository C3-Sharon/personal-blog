package com.sharon.blog.controller;

import com.sharon.blog.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ImageUploadController {

    @PostMapping("/api/admin/upload/image")
    @ResponseBody
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
               return Result.error("只准上传图片文件");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            String projectPath = System.getProperty("user.dir");
            String uploadPath = projectPath + File.separator + "uploads";

            System.out.println("========== 图片上传调试信息 ==========");
            System.out.println("项目路径: " + projectPath);
            System.out.println("上传目录: " + uploadPath);

            File uploadFolder = new File(uploadPath);
            if (!uploadFolder.exists()) {
                boolean created = uploadFolder.mkdirs();
                System.out.println("创建上传目录: " + (created ? "成功" : "失败"));
            }

            File dest = new File(uploadPath + File.separator + filename);
            file.transferTo(dest);

            System.out.println("文件保存路径: " + dest.getAbsolutePath());
            System.out.println("文件是否存在: " + dest.exists());

            String fileUrl = "/uploads/" + filename;
            System.out.println("图片访问URL: " + fileUrl);
            System.out.println("=====================================");

            return Result.ok("图片上传成功",fileUrl);

    }
}