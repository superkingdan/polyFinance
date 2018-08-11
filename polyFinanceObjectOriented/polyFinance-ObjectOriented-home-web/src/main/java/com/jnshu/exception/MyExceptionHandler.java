package com.jnshu.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理器
 * @author wangqichao
 */
@RestControllerAdvice
public class MyExceptionHandler {
    private static final Logger log= LoggerFactory.getLogger(MyException.class);
    @ExceptionHandler(MyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerMyException(MyException ex) {
        Map<String,Object> result = new HashMap<>();
        result.put("code", ex.getCode());
        result.put("message", ex.getMessage());
        return result;
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public Map defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//        log.error("", e);
//        Map<String,Object> result = new HashMap<>();
//        result.put("message", e.getMessage());
//        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
//            result.put("code",404);
//        } else {
//            result.put("code",10010);
//        }
//        return result;
//    }

}
