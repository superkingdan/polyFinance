package com.jnshu.controller3;

import com.jnshu.dto3.MessageListRPO;
import com.jnshu.entity.Message;
import com.jnshu.service3.CookieService3;
import com.jnshu.service3.MessageService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/a/u")
public class MessageController3 {

    @Autowired
    MessageService3 messageService3;
    @Autowired
    CookieService3 cookieService3;

    private static final Logger log= LoggerFactory.getLogger(MessageController3.class);
    /**
     * 多条件查询消息列表
     * @return 消息列表
     */
    @GetMapping("/messages")
    @ResponseBody
    public Object messages(@ModelAttribute MessageListRPO messageListRPO,
                           HttpServletRequest request)throws Exception{
        return messageService3.findMessageList(messageListRPO);
    }

    /**
     * 消息编辑，查看详情
     * @return 消息
     */
    @GetMapping("/messages/{id}")
    @ResponseBody
    public Object getMessage(@PathVariable(value="id") long id,
                             HttpServletRequest request){
        return messageService3.findMessageById(id);
    }
    /**
     * 新增消息
     * @return
     */
    @PostMapping("/messages")
    @ResponseBody
    public Object newMessage(@ModelAttribute Message message,
                             HttpServletRequest request) throws Exception {

        return messageService3.addMessage(message,request);
    }

    /*
     *上传图片
     * @return
     */
    @PostMapping("/imageUpload")
    @ResponseBody
    public Object newMessage( @RequestParam MultipartFile realImage,
                              @RequestParam(value = "imageName") String imageName,
                             HttpServletRequest request) throws Exception {
        return messageService3.imageUpload(realImage,request,imageName);
    }

    /**
     * 消息上下线
     * @return
     */
    @PutMapping("/messages/{id}")
    @ResponseBody
    public Object upMessage(@PathVariable(value="id") int id,
                            @RequestParam(value ="isSent")int isSent,
                            HttpServletRequest request) {
        return messageService3.updateMessage(isSent,id,request);
    }

    /**
     * 删除指定消息
     * @return
     */
    @DeleteMapping("/messages/{id}")
    @ResponseBody
    public Object deleteMessage(@PathVariable(value="id") int id,
                                HttpServletRequest request){
        return messageService3.deleteMessageById(id);
    }

}
