package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.cache.RedisCacheManager;
import com.jnshu.dao3.RealNameApplicationMapper3;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.entity.RealNameApplication;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserDataServiceImpl3 implements UserDataService3 {
    @Autowired
    UserMapper3 userMapper3;
    @Autowired
    UserBankService3 userBankService3;
    @Autowired
    AliOSSUtil aliOSSUtil;

    @Autowired
    RedisCacheManager redisCacheManager;
    @Autowired
    RealNameApplicationMapper3 realNameApplicationMapper3;

    /*用户信息*/
    @Override
    public JSONObject findUserByRequest(HttpServletRequest request) {
        JSONObject json =new JSONObject();
        Long id= null;
        try {
            id=Long.valueOf(CookieUtil.getCookieValue(request,"uid"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (id==null){
            json.put("code",-1);
            json.put("message","未登入");
            return json;
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data", userMapper3.findUserById(id));
        return json;
    }

    @Override
    public JSONObject findUserById(long id) {
        JSONObject json =new JSONObject();
        json.put("code",0);
        json.put("message","成功");
        json.put("data", userMapper3.findUserById(id));
        return json;
    }

    /*用户账户设置页面*/
    @Override
    public JSONObject findData(long id) {
        JSONObject json=new JSONObject();
        User user= userMapper3.findUserById(id);
        String defaultCard = null;
        if (user.getDefaultCard()!=0) {
            defaultCard = userBankService3.defaultCard(user.getDefaultCard());
        }
        Map<String ,String> map = new HashMap<String ,String>();
        map.put("phoneNumber",user.getPhoneNumber());
        map.put("idCard",user.getIdCard());
        map.put("email",user.getEmail());
        map.put("address",user.getAddress());
        map.put("realName",user.getRealName());
        map.put("defaultCard",defaultCard);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",map);
        return json;
    }

    /*修改资料*/
    @Override
    public JSONObject updataUser(long id, User user) {
        JSONObject json=new JSONObject();
        System.out.println(user);
        user.setId(id);
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
    /*设置手势密码*/
    @Override
    public JSONObject newGesture(long id,int gesturePassword) {
        JSONObject json=new JSONObject();
        User user= userMapper3.findUserById(id);
        user.setGesturePassword(gesturePassword);
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
    /*验证手势密码*/
    @Override
    public JSONObject eGesture(long id,int gesturePassword) {
        JSONObject json=new JSONObject();
        User user= userMapper3.findUserById(id);
        if (user.getGesturePassword()!=gesturePassword){
            json.put("code",-1);
            json.put("message","手势密码错误");
            return json;
        }
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
    /*修改手势密码*/
    @Override
    public JSONObject updataGesture(long id, int gestureNewPassword) {
        JSONObject json=new JSONObject();
        User user= userMapper3.findUserById(id);
        user.setGesturePassword(gestureNewPassword);
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }

    /*上传图片*/
    @Override
    public JSONObject updataImg(MultipartFile realImage, HttpServletRequest request, long id, String imageName) {
        JSONObject json=new JSONObject();
        if (realImage==null){
            json.put("code",-1);
            json.put("message","没有图片");
            return json;
        }
        User user = userMapper3.findUserById(id);
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
        String photoKey = "Task7/"+user.getPhoneNumber()+code+"."+photoType;
        String bucketName ="jnshuphoto";
        try {
            aliOSSUtil.uploadFile(photoKey, realImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = OSSUtil.getImgUrl(photoKey,bucketName);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",data);
        return json;
    }
    /*实名验证*/
    @Override
    public JSONObject verificationReal(long id, RealNameApplication realNameApplication) throws MyException {
        JSONObject json=new JSONObject();
        System.out.println("输入的=="+realNameApplication);
        if (realNameApplication.getIdCard()==null){
            throw new MyException(-1,"身份证号不能为空");
        }
        if (!Verification.regexIdCard(realNameApplication.getIdCard())){
            throw new MyException(-1,"身份证号格式不正确");
        }
        if (realNameApplication.getRealName()==null){
            throw new MyException(-1,"姓名不能为空");
        }
        List<RealNameApplication> realNameApplications=realNameApplicationMapper3.findIdCard(realNameApplication.getIdCard());
        System.out.println(realNameApplications);
        if (realNameApplications==null) {
            if (realNameApplicationMapper3.findByUserId(id) != null) {
                if (realNameApplicationMapper3.findByUserId(id).getApplicationStatus() != 1) {
                    realNameApplication.setIsFirst(1);
                    realNameApplicationMapper3.updateData(realNameApplication);
                    json.put("code", 0);
                    json.put("message", "成功");
                    System.out.println("实名注册表更新" + realNameApplication);
                    return json;
                }
                json.put("code", -1);
                json.put("message", "成功");
                System.out.println("请不要重复认证");
                return json;
            }
            realNameApplication.setCreateAt(System.currentTimeMillis());
            realNameApplication.setUserId(id);
            realNameApplicationMapper3.addRealName(realNameApplication);
            System.out.println(realNameApplication);
            json.put("code", 0);
            json.put("message", "成功");

            return json;
        }
        throw new MyException(-1,"该身份证只能绑定一个账户");
    }


}
