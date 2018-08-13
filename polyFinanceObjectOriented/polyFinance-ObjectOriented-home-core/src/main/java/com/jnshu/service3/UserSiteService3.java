package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.exception.MyException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component(value = "userSiteService3")
public interface UserSiteService3 {
    /*帮助*/
    JSONObject getHelp() throws MyException;
    /*关于*/
    JSONObject getAbout() throws MyException;
    /*反馈*/
    JSONObject feedback(long id, String content);
    /*更新*/
    JSONObject getUpdata() throws MyException;
    /*清除缓存*/
    JSONObject clean(HttpServletRequest request, HttpServletResponse response);


}
