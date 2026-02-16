package com.sharon.blog.pojo;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {
    private int page;
   private int size;
   private int total;
   private int totalPages;
   private List<T> data;
}
