package com.sharon.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**").excludePathPatterns("/admin/login");
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
