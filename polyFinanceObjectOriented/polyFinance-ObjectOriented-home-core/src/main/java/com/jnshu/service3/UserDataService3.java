package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.entity.RealNameApplication;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Component(value = "userDataService3")
public interface UserDataService3 {
    /*查找用户信息 金额等*/
    JSONObject findUserByRequest(HttpServletRequest request) throws MyException;
    /*查找用户信息 金额等*/
    JSONObject findUserById(long id) throws MyException;
    /*用户账户设置页面*/
    JSONObject findData(long id) throws MyException;
    /*用户账户资料修改*/
    JSONObject updataUser(long id, User user);
    /*设置手势密码*/
    JSONObject newGesture(long id, int gesturePassword) throws MyException;
    /*验证手势密码*/
    JSONObject eGesture(long id, int gesturePassword);
    /*修改手势密码*/
    JSONObject updataGesture(long id, int gestureNewPassword);
    /*上传图片*/
    JSONObject updataImg(MultipartFile realImage, HttpServletRequest request, long id, String imageName);
    /*实名验证*/
    JSONObject verificationReal(long id, RealNameApplication realNameApplication) throws MyException;



}
