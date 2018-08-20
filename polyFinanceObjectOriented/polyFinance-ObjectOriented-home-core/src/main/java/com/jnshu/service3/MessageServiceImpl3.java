package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.jnshu.cache.RedisCacheManager;
import com.jnshu.dao3.MessageMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.dao3.UserBackMapper3;
import com.jnshu.dto3.MessageListRPO;
import com.jnshu.entity.Message;
import com.jnshu.entity.TimedTask;
import com.jnshu.entity.UserBack;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.AliOSSUtil;
import com.jnshu.utils3.CookieUtil;
import com.jnshu.utils3.OSSUtil;
import com.jnshu.utils3.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
public class MessageServiceImpl3 implements MessageService3 {
    @Autowired
    MessageMapper3 messageMapper3;
    @Autowired
    UserBackMapper3 userBackMapper3;
    @Autowired
    AliOSSUtil aliOSSUtil;
    @Autowired
    RedisCacheManager redisCacheManager;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;

    /*消息列表*/
    @Override
    public JSONObject findMessageList(MessageListRPO messageListRPO) throws MyException {
        JSONObject json =new JSONObject();
        System.out.println(messageListRPO.getLoginName());
        if (messageListRPO.getLoginName()!=null) {
            UserBack userBack=userBackMapper3.findByName(messageListRPO.getLoginName());
            if (userBack==null){
                throw new MyException(-1, "没有该用户");
            }
            messageListRPO.setUpdateBy(userBack.getId());
        }
        List<MessageListRPO> messageListRPOS1=messageMapper3.findMessageListRPO(messageListRPO);
        if (messageListRPOS1!=null) {
            for (int i = 0; messageListRPOS1.size() > i; i++) {
                MessageListRPO messageListRPO2=messageListRPOS1.get(i);
                if (messageListRPO2!=null) {
                    UserBack userBack;
                    userBack=userBackMapper3.findById(messageListRPOS1.get(i).getUpdateBy());
                    if (userBack!=null) {
                        if (messageListRPOS1.get(i).getUpdateBy() != 0) {
                            messageListRPOS1.get(i).setLoginName(userBack.getLoginName());
                        }
                        if (messageListRPOS1.get(i).getUpdateBy() == 0) {
                            messageListRPOS1.get(i).setLoginName(userBack.getLoginName());
                        }
                    }
                    if (userBack==null) {
                        if (messageListRPOS1.get(i).getUpdateBy() != 0) {
                            messageListRPOS1.get(i).setLoginName("未知管理员");
                        }
                        if (messageListRPOS1.get(i).getUpdateBy() == 0) {
                            messageListRPOS1.get(i).setLoginName("未知管理员");
                        }
                    }
                }
            }
        }
        Page<MessageListRPO> messageListRPOS= (Page<MessageListRPO>) messageListRPOS1;

        json.put("code",0);
        json.put("message","成功");
        json.put("data",messageListRPOS);
        json.put("total",messageListRPOS.getTotal());
        return json;
    }

    /*消息详情*/
    @Override
    public JSONObject findMessageById(long id) {
        JSONObject json =new JSONObject();
        Message message=messageMapper3.findById(id);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",message);
        return json;
    }

    @Override
    public JSONObject deleteMessageById(long id) {
        JSONObject json =new JSONObject();
        messageMapper3.deleteMessage(id);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }

    /*新增消息*/
    @Override
    public JSONObject addMessage(Message message, HttpServletRequest request) throws MyException {
        JSONObject json =new JSONObject();
        if (message.getIntroduce()==null){
            throw new MyException(-1,"简述为空");
        }
        message.setCreateAt(System.currentTimeMillis());
        Long userBackId= null;
        try {
            userBackId=Long.valueOf(CookieUtil.getCookieValue(request,"uid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setCreateBy(userBackId);
        message.setUpdateBy(userBackId);
        if (message.getIsSent()==0){
            if(message.getMessageType()==1) {
                int isSent = 1;
                message.setIsSent(isSent);
            }
        }

        messageMapper3.addMessage(message);
        long id=message.getId();
        if (message.getMessageType()==1){
            /*添加定时任务*/
            int nature=5;
            long taskTime=message.getUpdateAt();//需要知道前台传回来的是什么类型
            TimedTask timedTask=new TimedTask();
            timedTask.setTaskTime(taskTime);
            timedTask.setNature(nature);
            timedTask.setMessageId(id);
            timedTaskMapper3.addTask(timedTask);
        }
        json.put("code",0);
        json.put("message","成功");

        return json;
    }

    /*消息上下线*/
    @Override
    public JSONObject updateMessage(int isSent,long id, HttpServletRequest request) {
        JSONObject json =new JSONObject();
        Message message=messageMapper3.findById(id);
        message.setIsSent(isSent);

        System.out.println("添加isSent="+isSent);
        if(isSent==0&&message.getMessageType()==1){
            /*添加定时任务*/
            int nature=5;
            long taskTime=message.getUpdateAt();//需要知道前台传回来的是什么类型
            TimedTask timedTask=new TimedTask();
            timedTask.setTaskTime(taskTime);
            timedTask.setNature(nature);
            timedTask.setMessageId(message.getId());
            timedTaskMapper3.addTask(timedTask);
        }
        if(isSent==2&&message.getMessageType()==1){
            /*删除定时任务*/
            timedTaskMapper3.deleteTask(timedTaskMapper3.findMessage(id).getId());
        }

        try {
            message.setUpdateBy(Long.valueOf(CookieUtil.getCookieValue(request,"uid")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setUpdateAt(System.currentTimeMillis());
        messageMapper3.updateMessage(message);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }


    /*上传图片*/
    @Override
    public JSONObject imageUpload(MultipartFile realImage, HttpServletRequest request, String imageName) {
        JSONObject json=new JSONObject();
        if (realImage==null){
            json.put("code",-1);
            json.put("message","没有图片");
            return json;
        }
        Long  userBackId;
        try {
            userBackId =Long.valueOf(CookieUtil.getCookieValue(request,"uid"));
        } catch (Exception e) {
            json.put("code",-1);
            json.put("message","请登入");
            return json;
        }
        /*得到图片的类型*/
        String[] photoName = realImage.getOriginalFilename().split("\\.");
        String photoType = photoName[photoName.length-1];
        if (!photoType.equals("png")&!photoType.equals("jpg")&!photoType.equals("img")){
            json.put("code",-1);
            json.put("message","图片格式不正确");
            return json;
        }
        String code = VerificationUtil.getVerificationCode();
        /*图片存储路径*/
        String photoKey = "Message/"+userBackId+imageName+code+"."+photoType;
        String bucketName ="jnshuphoto";
        try {
            aliOSSUtil.uploadFile(photoKey,realImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = OSSUtil.getImgUrl(photoKey,bucketName);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",data);
        return json;
    }
}
