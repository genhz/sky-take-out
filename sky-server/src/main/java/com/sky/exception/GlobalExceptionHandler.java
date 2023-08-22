package com.sky.exception;

import cn.hutool.core.util.StrUtil;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理类
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //预期异常（业务异常） BusinessException
    @ExceptionHandler(BusinessException.class)
    public Result handlerBusinessException(BusinessException e) {
        log.error("出现业务异常：{}", e);
        return Result.error(e.getCode(), e.getMessage());
    }

    //非预期异常 Exception 兜底异常处理
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        log.error("出现未知异常：{}", e);
        return Result.error(500, "未知异常，请稍后重试");
    }
    //属性重复异常处理
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handlerDuplicateKeyException(DuplicateKeyException e) {
        log.error("出现字段重复异常：{}", e);

        if (StrUtil.contains(e.getMessage(),"employee.username")){
            return Result.error("账户重复");
        }
        if (StrUtil.contains(e.getMessage(),"employee.phone")){
            return Result.error("手机号重复");
        }
        if (StrUtil.contains(e.getMessage(),"employee.id_number")){
            return Result.error("身份证号码重复");
        }

        return Result.error("字段重复");
    }
}
