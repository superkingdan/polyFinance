package com.jnshu.controller;

import com.jnshu.entity.User;
import com.jnshu.service3.UserDataService3;
import com.jnshu.service3.UserLoginService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户资料
 * @return
 */
@Controller
@RequestMapping("/a/u")
public class UserDataController3 {
    @Autowired
    UserDataService3 userDataService3;
    @Autowired
    UserLoginService3 userLoginService3;



    private static final Logger log= LoggerFactory.getLogger(UserDataController3.class);
    /**
     * 获得用户我的页面信息 /完成
     * @return 用户信息
     */
    @GetMapping(value = "/user")
    @ResponseBody
    public Object user(HttpServletRequest request){
        return userDataService3.findUserByRequest(request);
    }


    /**
     * 获得用户个人信息 /完成
     * @return 用户个人信息
     */
    @GetMapping("/user/set/{id}")
    @ResponseBody
    public Object userSet(@PathVariable(value="id") long id,HttpServletRequest request){
        log.info("获得用户id为"+id+"的个人信息");
        return userDataService3.findData(id);
    }
    /**
     * 实名验证
     * @return 状态码
     */
    @PostMapping("/user/verification/{id}")
    @ResponseBody
    public Object verification(@PathVariable(value="id") long id,
                               @ModelAttribute User user,
                               HttpServletRequest request){
        return userDataService3.verificationReal(id,user);
    }
    /**
     * 上传图片
     * @return 状态码
     */
    @PostMapping("/userImage/Upload/{id}")
    @ResponseBody
    public Object userImage(@PathVariable(value="id") long id,
                            @RequestParam MultipartFile realImage,
                            @RequestParam(value = "imageName") String imageName,
                            HttpServletRequest request){
        System.out.println("开始上传");
        return userDataService3.updataImg(realImage,request,id,imageName);
    }
    /**
     * 修改资料 /完成
     * @return 状态码
     */
    @PutMapping("/user/set/{id}")
    @ResponseBody
    public Object updateUser(@PathVariable(value="id") long id,
                             @ModelAttribute User user,
                             HttpServletRequest request){
        return userDataService3.updataUser(id,user);
    }
    /**
     * 修改密码 /完成
     * @return 状态码
     */
    @PutMapping("/user/pasd/{id}")
    @ResponseBody
    public Object pasdUp(@PathVariable(value="id") long id,
                         @RequestParam(value="newPassword") String newPassword,
                         @RequestParam(value="password") String password,
                         HttpServletRequest request) {
        return userLoginService3.updataPassword(id,newPassword,password);
    }
    /**
     * 设置手势密码 /完成
     * @return 状态码
     */
    @PostMapping("/user/gesture/new/{id}")
    @ResponseBody
    public Object gestureNew(@PathVariable(value="id") long id,
                             @RequestParam(value="gesturePassword") int gesturePassword,
                             HttpServletRequest request){
        return userDataService3.newGesture(id,gesturePassword);
    }
    /**
     * 原手势密码 /完成
     * @return 状态码
     */
    @PostMapping("/user/gesture/{id}")
    @ResponseBody
    public Object gesture(@PathVariable(value="id") long id,
                          @RequestParam(value="gesturePassword") int gesturePassword,
                          HttpServletRequest request){
        return userDataService3.eGesture(id,gesturePassword);
    }
    /**
     * 修改手势密码 /完成
     * @return 状态码
     */
    @PutMapping("/user/gesture/{id}")
    @ResponseBody
    public Object gestureUp(@PathVariable(value="id") long id,
                            @RequestParam(value="gestureNewPassword") int gestureNewPassword,
                            HttpServletRequest request){
        return userDataService3.updataGesture(id,gestureNewPassword);
    }
    /**
     * 忘记手势密码 /完成
     * @return 状态码
     */
    @PostMapping("/user/gesture/forget/{id}")
    @ResponseBody
    public Object gestureForget(@PathVariable(value="id") long id,
                                @RequestParam(value="password") String password,
                                HttpServletRequest request){
        return userLoginService3.verificationPassword(id,password);
    }
}
