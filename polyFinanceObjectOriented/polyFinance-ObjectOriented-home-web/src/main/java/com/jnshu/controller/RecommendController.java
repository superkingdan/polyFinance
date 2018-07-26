package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//推荐页
@Controller
@RequestMapping("/a")
public class RecommendController {

    //    banner图
    @GetMapping("/content/banner")
    @ResponseBody
    public Object findBack(@RequestParam  HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("id","1");
        map.put("title","标题");
        map.put("bannerCover","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/823.png");
        map.put("details","内容");
        json.put("data",map);
        return json;
    }



}
