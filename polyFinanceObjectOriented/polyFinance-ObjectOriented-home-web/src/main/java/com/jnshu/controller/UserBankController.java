package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//用户银行卡管理
@Controller
@RequestMapping("/a/u")
public class UserBankController {
    //    用户银行卡页面
    @GetMapping("/user/bank/{id}")
    @ResponseBody
    public Object userBank(@PathVariable(value="id") int id, HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("id","1");
        map.put("bankName","农业银行");
        map.put("bankCard","6222555577778888");
        map.put("icon","logo");
        map.put("singleLimited","50000");
        map.put("daylimited","5000");
        json.put("data",map);
        return json;
    }
    //    跳转到添加银行卡页面
    @GetMapping("/userBank/{id}")
    @ResponseBody
    public Object userBankUp(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("realName","罗佳超");
        map.put("idCard","33300000000");
        json.put("data",map);
        return json;
    }
    //    添加银行卡
    @PostMapping("/userBank/{id}")
    @ResponseBody
    public Object userBankUpdate(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }

}
