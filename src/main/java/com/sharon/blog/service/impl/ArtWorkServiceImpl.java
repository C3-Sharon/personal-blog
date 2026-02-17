package com.sharon.blog.service.impl;

import com.sharon.blog.mapper.ArtWorkMapper;
import com.sharon.blog.pojo.ArtWork;
import com.sharon.blog.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ArtWorkServiceImpl implements ArtWorkService{
    @Autowired
    private ArtWorkMapper artWorkMapper;

    @Override
    public List<ArtWork> getByCategory(String category) {
        return artWorkMapper.findByCategory(category);
    }

    @Override
    public PageResult<ArtWork> getPageByCategory(String category, int page, int size) {
        int offset = (page - 1) * size;
        List<ArtWork> artworks = artWorkMapper.selectPage(category, offset, size);
        int total = artWorkMapper.countByCategory(category);
        int totalPages = (total + size - 1) / size;

        PageResult<ArtWork> result = new PageResult<>();
        result.setData(artworks);
        result.setPage(page);
        result.setSize(size);
        result.setTotal(total);
        result.setTotalPages(totalPages);
        return result;
    }

    @Override
    public ArtWork getById(Long id) {
        return artWorkMapper.findById(id);
    }

    @Override
    public void deleteArtwork(Long id) {
artWorkMapper.deleteById(id);
    }

    @Override
    public ArtWork saveArtwork(ArtWork artWork) {
       if(artWork.getId()==null){
           artWork.setCreatedAt(LocalDateTime.now());
           artWork.setUpdatedAt(LocalDateTime.now());
           artWorkMapper.insert(artWork);
    }else{
           artWork.setUpdatedAt(LocalDateTime.now());
           artWorkMapper.update(artWork);//这里一开始忘记写了导致一直编辑不了
       }
       return artWork;
    }
}
