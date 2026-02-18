package com.sharon.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ImageUploadController {
//在前后端分离的时候这里的路径加了api，不知原先的混合开发那里这条路还走不走得通
    @PostMapping("/api/admin/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 检查文件是否为空
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "文件不能为空");
                return result;
            }

            // 2. 检查文件类型（只允许图片）
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                result.put("success", false);
                result.put("message", "只允许上传图片文件");
                return result;
            }

            // 3. 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 4. 确定上传目录（项目根目录下的 uploads 文件夹）
            String projectPath = System.getProperty("user.dir");
            String uploadPath = projectPath + File.separator + "uploads";

            // 打印路径信息（用于调试）
            System.out.println("========== 图片上传调试信息 ==========");
            System.out.println("项目路径: " + projectPath);
            System.out.println("上传目录: " + uploadPath);

            // 5. 创建上传目录（如果不存在）
            File uploadFolder = new File(uploadPath);
            if (!uploadFolder.exists()) {
                boolean created = uploadFolder.mkdirs();
                System.out.println("创建上传目录: " + (created ? "成功" : "失败"));
            }

            // 6. 保存文件
            File dest = new File(uploadPath + File.separator + filename);
            file.transferTo(dest);

            System.out.println("文件保存路径: " + dest.getAbsolutePath());
            System.out.println("文件是否存在: " + dest.exists());

            // 7. 返回图片访问URL
            String fileUrl = "/uploads/" + filename;
            System.out.println("图片访问URL: " + fileUrl);
            System.out.println("=====================================");

            result.put("success", true);
            result.put("url", fileUrl);
            result.put("message", "上传成功");

        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "上传失败：" + e.getMessage());
        }

        return result;
    }
}