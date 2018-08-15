package com.jnshu.controller;



import com.jnshu.service3.UserMessageService3;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 用户消息
 * @return
 */
@Controller
@RequestMapping("/a/u")
public class UserMessageController3 {
    @Autowired
    UserMessageService3 userMessageService3;
    private static final Logger log= LoggerFactory.getLogger(UserMessageController3.class);
    /**
     * 用户消息中心 /完成
     * @return 消息列表
     */
    @GetMapping("/user/userMessage/list")
    @ResponseBody
    public Object messageUser(HttpServletRequest request){

        long id= 0;
        try {
            id = Long.parseLong(CookieUtil.getCookieValue(request,"uid"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return userMessageService3.findMessageList(id);
     }
     /**
      * 活动消息
      * @return 消息列表
      */
     @GetMapping("/user/userMessage/banner/{id}")
     @ResponseBody
     public Object message(@PathVariable(value="id") long id, HttpServletRequest request){
        return userMessageService3.findMessage(id);
     }
}
