package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dto3.MessageListRPO;
import com.jnshu.entity.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Component(value = "messageService3")
public interface MessageService3 {
    /*获取后台消息列表*/
    JSONObject findMessageList(MessageListRPO messageListRPO);
    /*获取消息详情*/
    JSONObject findMessageById(long id);
    /*删除消息*/
    JSONObject deleteMessageById(long id);
    /*新增消息*/
    JSONObject addMessage(Message message, HttpServletRequest request) throws Exception;
    /*上传图片*/
    JSONObject imageUpload(MultipartFile realImage, HttpServletRequest request, String imageName) throws Exception;
    /*编辑消息*/
    JSONObject updateMessage(int isSent, long id, HttpServletRequest request);

}
