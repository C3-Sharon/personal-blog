package com.sharon.blog.mapper;

import com.sharon.blog.pojo.ArtWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArtWorkMapper {
    List<ArtWork> findByCategory(@Param("category") String category);
    ArtWork findById(@Param("id") Long id);
    int insert(ArtWork artWork);
    int update(ArtWork artWork);
    List<ArtWork> selectPage(@Param("category") String category,
                             @Param("offset") int offset,
                             @Param("limit") int limit);
    int countByCategory(@Param("category") String category);
    int deleteById(@Param("id") Long id);
}
