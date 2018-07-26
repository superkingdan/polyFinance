package com.jnshu.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//用户设置
@Controller
@RequestMapping("/a/u")
public class UserSiteController {
    //    帮助中心
    @GetMapping("/user/help")
    @ResponseBody
    public Object help(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("www.baidu.com","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/823.png");
        json.put("帮助url",map);
        return json;
    }
    //    关于我们
    @GetMapping("/user/about")
    @ResponseBody
    public Object about(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("www.baidu.com","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/823.png");
        json.put("关于我们url",map);
        return json;
    }
    //   意见反馈
    @PostMapping("/user/feedback")
    @ResponseBody
    public Object feedback(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    登出
    @GetMapping("/user/logout")
    @ResponseBody
    public Object logout(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    更新
    @GetMapping("/user/Update")
    @ResponseBody
    public Object Update(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("更新","更新");
        json.put("更新",map);
        return json;
    }
}
