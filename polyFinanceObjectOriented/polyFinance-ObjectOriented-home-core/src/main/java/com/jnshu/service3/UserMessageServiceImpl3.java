package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.MessageMapper3;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.entity.Message;
import com.jnshu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMessageServiceImpl3 implements UserMessageService3 {

    @Autowired
    UserMapper3 userMapper3;
    @Autowired
    MessageMapper3 messageMapper3;

    @Override
    public JSONObject findMessageList(long id) {
        JSONObject json=new JSONObject();
        User user= userMapper3.findUserById(id);
        List<Message> messageList=new ArrayList<>();
        List<Message> messages= messageMapper3.findAllByUser(id);
        List<Message> messages1= messageMapper3.findByTpye(user.getRealStatus());
        messageList.addAll(messages);
        messageList.addAll(messages1);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",messageList);
        return json;
        }
//        List<Message> messages=messageMapper3.findByTpye(user.getRealStatus());
//        json.put("code",0);
//        json.put("message","成功");
//        json.put("data",messages);
//        return json;
    }
