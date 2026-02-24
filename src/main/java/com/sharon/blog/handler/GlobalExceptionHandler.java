package com.sharon.blog.handler;

import com.sharon.blog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handlerMaxSizeException(Exception e)  {
        return Result.error("文件过大 (最大不超过15MB)");
    }
    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception e)  {
        e.printStackTrace();
        return Result.error("出错了，请稍后再试");
    }



}

