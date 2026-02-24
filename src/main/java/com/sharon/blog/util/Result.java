package com.sharon.blog.util;

import lombok.Data;

@Data
public class Result<T> {
    private boolean success;
    private String message;
    private T data;
    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
public static <T> Result<T> ok(){
return new Result<>(true,"操作成功",null);
}
public static <T> Result<T> ok(T data){
        return new Result<>(true,"操作成功",data);
}
public static <T> Result<T> ok(String message,T data){
        return new Result<>(true,message,data);
}
public static <T> Result<T> error(){
        return new Result<>(false,"操作失败",null);
}
public static <T> Result<T> error(String message){
        return new Result<>(false,message,null);
}
public static <T> Result<T> error(String message,T data){
        return new Result<>(false,message,data);
}
}
