package com.jnshu.service3;


import com.alibaba.fastjson.JSONObject;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component(value = "userLoginService3")
public interface UserLoginService3 {
    /**
     * 注册 发送验证码
     * @return json
     */
    JSONObject setCode(String phoneNumber);
    /**
     * 验证验证码
     * @return json
     */
    JSONObject verificationCode(String phoneNumber, String code);
    /**
     * 注册
     * @return json
     */
    JSONObject registered(User user, String password, HttpServletResponse response);
    /**
     * 验证密码格式
     * @return json
     */
    JSONObject verificationPas(String password);
    /**
     * 登入
     * @return json
     */
    JSONObject login(String phoneNumber, String password, HttpServletResponse response) throws MyException;
    /**
     * 添加cookie
     * @return json
     */
    void addCookie(User user, HttpServletResponse response);
    /**
     * 判断验证
     * @return json
     */
    JSONObject verification(User user, String password, String code, HttpServletResponse response);
    /**
     * 找回密码
     * @return json
     */
    JSONObject findBackPassword(String phoneNumber, String password, String code);
    /**
     * 加密 加盐
     * @return json
     */
    User getPassword(String password);
    /**
     * 修改密码
     * @return json
     */
    JSONObject updataPassword(long id, String newPassword, String password);
    /*
     * 验证密码
     * @return json
     */
    JSONObject verificationPassword(long id, String password);
}
