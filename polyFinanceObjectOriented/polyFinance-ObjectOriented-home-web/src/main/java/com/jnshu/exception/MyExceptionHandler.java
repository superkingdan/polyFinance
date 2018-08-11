package com.jnshu.exception;


import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常处理器
 * @author wangqichao
 */
@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public boolean handlerMyException(MyException ex, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        json.put("code", ex.getCode());
        json.put("message", ex.getMessage());
        try {
            SendMsgUtil.sendJsonMessage(response,json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
