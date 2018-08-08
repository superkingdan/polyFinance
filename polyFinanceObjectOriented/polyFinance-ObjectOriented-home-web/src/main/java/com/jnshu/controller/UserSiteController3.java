package com.jnshu.controller;

import com.jnshu.service3.UserSiteService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//用户设置
@Controller
@RequestMapping("/a/u")
public class UserSiteController3 {
    @Autowired
    UserSiteService3 userSiteService3;

    private static final Logger log= LoggerFactory.getLogger(UserSiteController3.class);
    /**
     * 帮助中心 /完成
     * @return dataValue
     */
    @GetMapping("/user/help")
    @ResponseBody
    public Object help(HttpServletRequest request) {
        return userSiteService3.getHelp();
    }
    /**
     * 关于我们 /完成
     * @return dataValue
     */
    @GetMapping("/user/about")
    @ResponseBody
    public Object about(HttpServletRequest request) {
        return userSiteService3.getAbout();
    }
    /**
     * 意见反馈 /完成
     * @return 状态码
     */
    @PostMapping("/user/feedback/{id}")
    @ResponseBody
    public Object feedback(@PathVariable(value="id") long id,
                           @RequestParam(value = "content") String  content,
                           HttpServletRequest request) {
        return userSiteService3.feedback(id,content);
    }
    /**
     * 登出 /完成
     * @return
     */
    @GetMapping("/user/logout")
    @ResponseBody
    public Object logout(HttpServletRequest request, HttpServletResponse response){
        return userSiteService3.clean(request,response);
    }
    /**
     * 更新 /完成
     * @return dataValue
     */
    @GetMapping("/user/Update")
    @ResponseBody
    public Object Update(HttpServletRequest request) {
        return userSiteService3.getUpdata();
    }
}
