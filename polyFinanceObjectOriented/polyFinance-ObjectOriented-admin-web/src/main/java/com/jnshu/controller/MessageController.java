package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/a/u")
public class MessageController {

    private static final Logger log= LoggerFactory.getLogger(MessageController.class);
//    需求：点击”消息列表“按钮，查询当前消息。模糊查询消息列表。
    @GetMapping("/messages")
    @ResponseBody
    public Object messages(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("total","9");
        map.put("pageSize","8");
        map.put("id","1");
        map.put("title","更新");
        map.put("sentPersonType","0");
        map.put("isSent","0");
        map.put("loginName","luojiac");
        map.put("createAt","1998/01/01");
        map.put("messageType","1");
        map.put("isPush","1");
        json.put("更新",map);
        return json;
    }

//    消息列表。编辑，查看详情。
    @GetMapping("/messages/{id}")
    @ResponseBody
    public Object getMessage(HttpServletRequest request) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("id","1");
        map.put("title","标题");
        map.put("sentPersonType","0");
        map.put("introduce","简述");
        map.put("content","www.17173.com");
        map.put("isSent","0");
        map.put("loginName","luojiac");
        map.put("createAt","1998/01/01");
        map.put("messageType","1");
        map.put("isPush","1");
        map.put("updateAt","10/1");
        json.put("更新",map);
        return json;
    }
//    新增消息。图片上传和删除同上
    @PostMapping("/messages")
    @ResponseBody
    public Object newMessage(HttpServletRequest request) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }

//    指定消息上下线
    @PutMapping("/messages/{id}")
    @ResponseBody
    public Object upMessage(HttpServletRequest request) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
//    删除指定消息
    @DeleteMapping("/messages/{id}")
    @ResponseBody
    public Object deleteMessage(HttpServletRequest request) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }

}
