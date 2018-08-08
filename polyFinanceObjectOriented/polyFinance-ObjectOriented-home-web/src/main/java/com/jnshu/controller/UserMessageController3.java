package com.jnshu.controller;



import com.jnshu.service3.UserMessageService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping("/user/userMessage/list/{id}")
    @ResponseBody
    public Object messageUser(@PathVariable(value="id") long id, HttpServletRequest request){
        return userMessageService3.findMessageList(id);
    }
}
