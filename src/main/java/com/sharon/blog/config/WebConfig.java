package com.sharon.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Value("${app.cors.allowed-origin:http://localhost:5137}")
    private String corsAllowedOrigin;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(corsAllowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**")
                .addPathPatterns("/api/admin/**")
                .addPathPatterns("/api/gallery/**")
                .excludePathPatterns("/admin/login", "/api/admin/login", "/api/admin/check");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = uploadPath.startsWith("file:") ? uploadPath : "file:" + uploadPath;

        if (!location.endsWith("/") && !location.endsWith("\\")) {
            location += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);

        System.out.println("========== 动态资源映射启动 ==========");
        System.out.println("映射位置: " + location);
        System.out.println("=====================================");
    }
}


