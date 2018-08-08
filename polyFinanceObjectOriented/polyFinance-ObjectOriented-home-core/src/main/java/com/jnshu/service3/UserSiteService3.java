package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component(value = "userSiteService3")
public interface UserSiteService3 {
    /*帮助*/
    JSONObject getHelp();
    /*关于*/
    JSONObject getAbout();
    /*反馈*/
    JSONObject feedback(long id, String content);
    /*更新*/
    JSONObject getUpdata();
    /*清除缓存*/
    JSONObject clean(HttpServletRequest request, HttpServletResponse response);


}
