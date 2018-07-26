package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//用户资料及修改
@Controller
@RequestMapping("/a/u")
public class UserDataController {
    //    我的
    @GetMapping(value = "/user/{id}")
    @ResponseBody
    public Object user(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("realName"," 罗佳超");
        map.put("property","aaa");
        map.put("cumulativeIncome","12222");
        map.put("realStatus","111");
        json.put("data",map);
        return json;
    }


    //    获取个人资料
    @GetMapping("/user/set/{id}")
    @ResponseBody
    public Object userSet(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        Map<String, String> map=new HashMap<String, String>();
        map.put("phoneNumber","11111");
        map.put("idCard","222222");
        map.put("email","11@qq.com");
        map.put("address","www.wiwj2s.coma");
        map.put("realName","罗佳超");
        map.put("defaultcard","农业银行(2222)");
        json.put("data",map);
        return json;
    }
    //    实名验证
    @PostMapping("/user/verification/{id}")
    @ResponseBody
    public Object verification(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    修改资料
    @PutMapping("/user/set/{id}")
    @ResponseBody
    public Object updateUser(@PathVariable(value="id") int id,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    修改密码
    @PutMapping("/user/pasd/{id}")
    @ResponseBody
    public Object pasdUp(@PathVariable(value="id") int id,@RequestParam(value="newPassword") String newPassword,
                         @RequestParam(value="password") String password,HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    新手势密码
    @PostMapping("/user/gesture/new")
    @ResponseBody
    public Object gestureNew(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    原手势密码
    @PostMapping("/user/gesture")
    @ResponseBody
    public Object gesture(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    手改手势密码
    @PutMapping("/user/gesture")
    @ResponseBody
    public Object gestureUp(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
    //    忘记手势密码
    @PostMapping("/user/gesture/forget")
    @ResponseBody
    public Object gestureForget(HttpServletRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code",1);
        json.put("message","ok");
        return json;
    }
}
