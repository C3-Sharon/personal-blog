package com.sharon.blog.service.impl;

import com.sharon.blog.pojo.ArtWork;
import com.sharon.blog.pojo.PageResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArtWorkService {
    List<ArtWork> getByCategory(String category);

    PageResult<ArtWork> getPageByCategory(String category, int page, int size);

    ArtWork getById(Long id);

    void deleteArtwork(Long id);

    ArtWork saveArtwork(ArtWork artWork);
}
