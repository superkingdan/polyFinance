package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.jnshu.cache.RedisCacheManager;
import com.jnshu.dao3.MessageMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.dao3.UserBackMapper3;
import com.jnshu.dto3.MessageListRPO;
import com.jnshu.entity.Message;
import com.jnshu.entity.TimedTask;
import com.jnshu.utils3.OSSUtil;
import com.jnshu.utils3.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class MessageServiceImpl3 implements MessageService3 {
    @Autowired
    MessageMapper3 messageMapper3;
    @Autowired
    UserBackMapper3 userBackMapper3;
    @Autowired
    CookieService3 cookieService3;
    @Autowired(required=false)
    OSSClient ossClient;
    @Autowired
    RedisCacheManager redisCacheManager;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;

    /*消息列表*/
    @Override
    public JSONObject findMessageList(MessageListRPO messageListRPO) {
        JSONObject json =new JSONObject();
        messageListRPO.setCreateBy(userBackMapper3.findByName(messageListRPO.getLoginName()).getCreateBy());
        List<MessageListRPO> messageListRPOS= messageMapper3.findMessageListRPO(messageListRPO);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",messageListRPOS);

        return json;
    }

    /*消息详情*/
    @Override
    public JSONObject findMessageById(long id) {
        JSONObject json =new JSONObject();
        Message message= messageMapper3.findById(id);
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
    public JSONObject addMessage(Message message, HttpServletRequest request) throws Exception {
        JSONObject json =new JSONObject();
        message.setCreateAt(System.currentTimeMillis());
        Long userBackId= cookieService3.findByCookie(request);

        message.setCreateBy(userBackId);

        String content= String.valueOf(redisCacheManager.get(userBackId+",image"));
        if(content!=null) {
            message.setContent(content);
        }


        if (message.getMessageType()==1){
            /*添加定时任务*/
            long taskTime=message.getUpdateAt();//需要知道前台传回来的是什么类型
            TimedTask timedTask=new TimedTask();
            timedTask.setTaskTime(taskTime);
            timedTask.setNature(5);
            timedTask.setMessageId(message.getId());
            timedTaskMapper3.addTask(timedTask);
            message.setIsSent(1);
        }
        messageMapper3.addMessage(message);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }

    /*编辑消息*/
    @Override
    public JSONObject updateMessage(int isSent,long id, HttpServletRequest request) {
        JSONObject json =new JSONObject();
        Message message= messageMapper3.findById(id);
        message.setIsSent(isSent);
        try {
            message.setUpdateBy(cookieService3.findByCookie(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(message.getUpdateAt()==0) {
            message.setUpdateAt(System.currentTimeMillis());
        }
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
            userBackId = cookieService3.findByCookie(request);
        } catch (Exception e) {
            json.put("code",-1);
            json.put("message","请登入");
            return json;
        }

        System.out.println(realImage.getOriginalFilename());
        /*得到图片的类型*/
        String[] photoName = realImage.getOriginalFilename().split("\\.");
        String photoType = photoName[photoName.length-1];
        System.out.println(photoType);
        if (!photoType.equals("png")&!photoType.equals("jpg")&!photoType.equals("img")){
            json.put("code",-1);
            json.put("message","图片格式不正确");
            return json;
        }
        String code = VerificationUtil.getVerificationCode();
        /*图片存储路径*/
        String photoKey = "Message/"+userBackId+imageName+"."+photoType;
        String bucketName ="avatarljc1";
        try {
            ossClient.putObject(bucketName, photoKey, new ByteArrayInputStream(realImage.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ossClient.shutdown();
        String avatar = OSSUtil.getImgUrl(photoKey,bucketName);
        redisCacheManager.set(userBackId+",image",avatar);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
}
