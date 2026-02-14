package com.sharon.blog;

import com.sharon.blog.entity.Blog;
import com.sharon.blog.repository.BlogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class PersonalBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalBlogApplication.class, args);
    }
    @Bean
public CommandLineRunner initData(BlogRepository blogRepository){
        return args -> {
            if(blogRepository.count()==0){
                Blog blog2= new Blog("2026.2.12吃饭记录","今天早餐吃了昨天剩下的意面(由于空气炸锅设置时间失误导致意面口感无限接近于薯片)，" +
                        "早上在便利店买了一个茶叶蛋，但是在还剩一半的时候茶叶蛋不幸滚落在地，悲。" +
                        "中午吃了牛肉螺蛳粉，晚上吃了冬菜肉片饭",LocalDateTime.now());
                blogRepository.save(blog2);
                System.out.println("测试数据成功插入");

            }else{
                System.out.println("数据库中已有数据，跳过初始化");
            }
        };
}
}
