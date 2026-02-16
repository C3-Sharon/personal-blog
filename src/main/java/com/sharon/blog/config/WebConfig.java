package com.sharon.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根路径
        String projectPath = System.getProperty("user.dir");
        String uploadPath = "file:" + projectPath + "/uploads/";

        // 映射 /uploads/** 到本地的 uploads 目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        System.out.println("========== 静态资源映射配置 ==========");
        System.out.println("访问路径: /uploads/**");
        System.out.println("本地路径: " + uploadPath);
        System.out.println("=====================================");
    }
}
