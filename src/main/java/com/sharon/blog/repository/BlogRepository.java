package com.sharon.blog.repository;

import com.sharon.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//建立接口，让Java能操作数据库
public interface BlogRepository extends JpaRepository<Blog,Long> {
    //JpaRepository<Blog,Long>是Spring Data JPA提供的超级父接口
    //第一个泛型Blog：这个仓库操作哪个实体类；第二个泛型Long：这个实体类的主键是什么类型
}
