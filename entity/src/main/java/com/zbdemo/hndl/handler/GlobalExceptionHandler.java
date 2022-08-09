package com.zbdemo.hndl.handler;

import com.zbdemo.hndl.base.ResultData;
import com.zbdemo.hndl.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @author zhangbing
 * @title: GlobalExceptionHandler
 * @projectName hndl
 * @description: 全局异常捕获
 * @date 2022/7/14下午6:46
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResultData handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        // 缺少参数抛出的异常是MissingServletRequestParameterException
        if (e instanceof MissingServletRequestParameterException){
                ResultData result = ResultData.createResult(ResponseEnum.ERROR_CODE,MessageFormat.format("缺少参数{0}", ((MissingServletRequestParameterException) e).getParameterName()));
                return result;
        }
        // 单参数校验失败后抛出的异常是ConstraintViolationException
        if (e instanceof ConstraintViolationException){
            ResultData result = ResultData.createResult(ResponseEnum.ERROR_CODE);
            Set<ConstraintViolation<?>> sets = ((ConstraintViolationException) e).getConstraintViolations();
            if(!CollectionUtils.isEmpty(sets)){
                StringBuilder sb = new StringBuilder();
                sets.forEach(error -> {
                    if (error instanceof FieldError) {
                        sb.append(((FieldError)error).getField()).append(":");
                    }
                    sb.append(error.getMessage()).append(";");
                });
                String msg = sb.toString();
                msg = StringUtils.substring(msg, 0, msg.length() -1);
                result.setMsg(msg);
            }
            return result;
        }
        // get请求的对象参数校验失败后抛出的异常是BindException
        if (e instanceof BindException){
            ResultData result = ResultData.createResult(ResponseEnum.ERROR_CODE);
            List<ObjectError> errors = ((BindException) e).getBindingResult().getAllErrors();
            String msg = getValidExceptionMsg(errors);
            if (StringUtils.isNotBlank(msg)){
                result.setMsg(msg);
            }

            return result;
        }
        // post请求的对象参数校验失败后抛出的异常是MethodArgumentNotValidException
        if (e instanceof MethodArgumentNotValidException){
            ResultData result = ResultData.createResult(ResponseEnum.ERROR_CODE);
            List<ObjectError> errors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            String msg = getValidExceptionMsg(errors);
            if (StringUtils.isNotBlank(msg)){
                result.setMsg(msg);
            }

            return result;
        }
        log.error(MessageFormat.format("错误信息{0}",e.getMessage()));
        return ResultData.createResult(ResponseEnum.ERROR_CODE,e.getMessage());
    }

    private String getValidExceptionMsg(List<ObjectError> errors) {
        if(!CollectionUtils.isEmpty(errors)){
            StringBuilder sb = new StringBuilder();
            errors.forEach(error -> {
                if (error instanceof FieldError) {
                    sb.append(((FieldError)error).getField()).append(":");
                }
                sb.append(error.getDefaultMessage()).append(";");
            });
            String msg = sb.toString();
            msg = StringUtils.substring(msg, 0, msg.length() -1);
            return msg;
        }
        return null;
    }
}
