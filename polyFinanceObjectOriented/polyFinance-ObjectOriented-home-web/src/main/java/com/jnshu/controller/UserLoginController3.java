package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.jnshu.entity.User;
import com.jnshu.service3.UserLoginService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户注册及登入
 * @return
 */
@Controller
@RequestMapping("/a")
public class UserLoginController3 {

    @Autowired
    UserLoginService3 userLoginService3;
    private static final Logger log= LoggerFactory.getLogger(UserLoginController3.class);

    /**
     * 登入 /完成
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestParam(value="phoneNumber") String phoneNumber,
                        @RequestParam(value="password") String password,
                        HttpServletResponse response )  {
        return userLoginService3.login(phoneNumber,password,response);
    }

    /**
     * 注册 验证验证码 /完成
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Object register(@ModelAttribute User user,
                           @RequestParam(value="password") String password,
                           @RequestParam(value="code") String code,
                           HttpServletRequest request) {
        return userLoginService3.verification(user,password,code);
    }

    /**
     * 注册 发送验证码 /完成
     * @return
     */
    @PostMapping("/register/code")
    @ResponseBody
    public Object code(@RequestParam(value="phoneNumber") String phoneNumber,
                       HttpServletRequest request)  {
        return userLoginService3.setCode(phoneNumber);
    }

    /**
     * 找回密码 /完成
     * @return
     */
    @PutMapping("/user/pswd")
    @ResponseBody
    public Object findBack(@RequestParam(value="phoneNumber") String phoneNumber,
                           @RequestParam(value="code") String code,
                           @RequestParam(value="password")String password,
                           HttpServletRequest request) throws JSONException {
        return userLoginService3.findBackPassword(phoneNumber,password,code);
    }





}
