package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常捕获类
 */
@ControllerAdvice
public class ExceptionCatch {

    //定义记录日志
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();


    static{
        //在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }

    @ExceptionHandler(CustomException.class)//捕获CustomException异常，并将自定义异常类CustomException传递到方法参数中，调用方法
    @ResponseBody//将数据转换为json返回到前端
    public ResponseResult customException(CustomException customException){
        //记录日志
        LOGGER.error("catch exception:{}",customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        LOGGER.error("catch exception:{}",exception.getMessage());
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        //从EXCEPIONS中找异常类型所对应的错误代码，找到了将错误代码返回给用户，如果找不到将返回99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode !=null){
            return new ResponseResult(resultCode);
        }
        //返回99999异常
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }
}
