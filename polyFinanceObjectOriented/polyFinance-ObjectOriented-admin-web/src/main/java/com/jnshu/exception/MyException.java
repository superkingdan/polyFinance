package com.jnshu.exception;

/**
 * 自定义异常类
 * @author wangqichao
 */
public class MyException extends Exception{

    private static final long serialVersionUID = -5519743897907627214L;

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(Integer code,String message) {
        super(message);
        this.code=code;
    }
}
