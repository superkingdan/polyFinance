package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component(value = "userMessageService3")
public interface UserMessageService3 {
    /*获取消息列表*/
    JSONObject findMessageList(long id);
    /*获取消息*/
    JSONObject findMessage(long id);
}
