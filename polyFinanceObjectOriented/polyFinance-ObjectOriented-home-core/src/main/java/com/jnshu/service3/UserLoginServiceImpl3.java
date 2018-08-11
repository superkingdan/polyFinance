package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.jnshu.cache.RedisCacheManager;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.entity.User;
import com.jnshu.utils3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

@Service
public class UserLoginServiceImpl3 implements UserLoginService3 {
    @Autowired
    UserMapper3 userMapper3;
    @Autowired
    RedisCacheManager redisCacheManager;

    private static final Logger log= LoggerFactory.getLogger(UserLoginServiceImpl3.class);
    /*发送验证码*/
    @Override
    public JSONObject setCode(String phoneNumber) {
        JSONObject json =new JSONObject();
        System.out.println("开始判断");

        boolean userp = Verification.regexPhone(phoneNumber);

        if (!userp) {
            json.put("code",-1);
            json.put("message","请输入正确的手机号码");
            return json;
        }
        System.out.println("开始发送");
        /*生成code*/
        String code = VerificationUtil.getVerificationCode();
        /*发送验证码*/
        SendSmsResponse response = null;
        try {
            response = MessagePhone.sendSms(phoneNumber, code);
            redisCacheManager.set(phoneNumber,code,1200);
        } catch (ClientException e) {
            e.printStackTrace();
            json.put("code",0);
            json.put("message","发送失败");
            return json;
        }
        assert response != null;
        System.out.println("code="+response.getCode());
        json.put("code",0);
        json.put("message","验证码已发送");
        return json;
    }
    /*验证*/
    @Override
    public JSONObject verificationCode(String phoneNumber, String code) {
        JSONObject json =new JSONObject();
        System.out.println("开始验证");
        if (redisCacheManager.get(phoneNumber)==null){
            json.put("code",-1);
            json.put("message","验证码无效");
            log.info("验证码无效");
            return json;
        }
        if (!redisCacheManager.get(phoneNumber).equals(code)){
            System.out.println(code);
            System.out.println(redisCacheManager.get(phoneNumber));
            json.put("code",-1);
            json.put("message","验证码错误");
            log.info("验证码错误");
            return json;
        }
            json.put("code",0);
            json.put("message","验证成功");
        return json;
    }
    /*注册*/
    @Override
    public JSONObject registered(User user, String password) {
        JSONObject json =new JSONObject();
        if (userMapper3.findUserByPhone(user.getPhoneNumber())!=null){
            json.put("code",-1);
            json.put("message","该手机已注册");
            return json;
        }
        User user1=getPassword(password); //加密
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setCreateAt(System.currentTimeMillis());

        String oldNumber= userMapper3.getOldUserNumber();
        user1.setUserNumber(UserNumber.NewUserNumber(oldNumber));   //user_number UK+年份后两位+10+000001（六位递增数字） 唯一索引

        userMapper3.addUser(user1);
        json.put("code",0);
        json.put("message","注册成功");
        redisCacheManager.del(user.getPhoneNumber());
        return json;
    }
    /*验证密码格式*/
    @Override
    public JSONObject verificationPas(String password) {
        JSONObject json =new JSONObject();
        boolean confirmP = Verification.regexPassword(password);
        if (!confirmP){
            json.put("code",-1);
            json.put("message","密码格式错误");
        }
        json.put("code",0);
        json.put("message","密码格式正确");
        return json;
    }
    /*登入*/
    @Override
    public JSONObject login(String phoneNumber, String password, HttpServletResponse response) {
        JSONObject json =new JSONObject();
        User user= userMapper3.findUserByPhone(phoneNumber);
        if (password==null){
            json.put("code",-1);
            json.put("message","请输入密码");
            return json;
        }
        if (user == null){
            json.put("code",-1);
            json.put("message","该手机号未注册");
            return json;
        }
        if (!SHA.getSHAwithSalt(password, user.getSalt()).equals(user.getHashKey())){
            json.put("code",-1);
            json.put("message","密码错误,登入失败");
            return json;
        }
        addCookie(user,response);
        json.put("code",0);
        json.put("message","登入成功");
        return json;
    }
    /*添加cookie*/
    @Override
    public void addCookie(User user, HttpServletResponse response) {
        User user1 = userMapper3.findUserByPhone(user.getPhoneNumber());
        try {
            String token = TokenJWT.createToken(user1);
            System.out.println("token=====" + token);
            CookieUtil.addCookie(response, "token", token);
            CookieUtil.addCookie(response,"uid", String.valueOf(user.getId()));
            log.info(new Date() + "添加Cookie成功");
        } catch (Exception e) {
            log.info("生成token失败！");
        }

    }
    /*判断验证*/
    @Override
    public JSONObject verification(User user, String password, String code) {
        if (verificationPas(password).get("code").equals(-1)){
            System.out.println(2);
            return verificationPas(password);
        }
        if (verificationCode(user.getPhoneNumber(),code).get("code").equals(-1)){
            System.out.println(3);
            return verificationCode(user.getPhoneNumber(),code);
        }
        System.out.println(1);
        return registered(user,password);
    }
    /*找回密码*/
    @Override
    public JSONObject findBackPassword(String phoneNumber, String password,String code) {
        JSONObject json =new JSONObject();
        if (verificationPas(password).get("code").equals(-1)){
            return verificationPas(password);
        }
        if (userMapper3.findUserByPhone(phoneNumber)==null){
            json.put("code",-1);
            json.put("message","该账号未注册");
            return json;
        }
        if (verificationCode(phoneNumber,code).get("code").equals(-1)){
            System.out.println("验证失败");
            return verificationCode(phoneNumber,code);
        }
        User user=getPassword(password);
        user.setUpdateAt(System.currentTimeMillis());
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","修改成功");
        log.info("删除缓存");
        redisCacheManager.del(phoneNumber);
        return json;
    }
//加密
    @Override
    public User getPassword(String password) {
        User user=new User();
        //表示通用唯一标识符(UUID)的类。UUID表示一个128位的值
        UUID uuid = UUID.randomUUID();
        String salt = uuid.toString().substring(10);
        //进行SHA加密
        user.setSalt(salt);
        //进行salt加密
        user.setHashKey(SHA.getSHAwithSalt(password, salt));
        return user;
    }

//    修改密码
    @Override
    public JSONObject updataPassword(long id, String newPassword, String password) {
        JSONObject json =new JSONObject();
        boolean confirmP = Verification.regexPassword(newPassword);
        if (!confirmP){
            json.put("code",-1);
            json.put("message","密码格式错误");
            return json;
        }
        User user= userMapper3.findUserById(id);
        if(!SHA.getSHAwithSalt(password, user.getSalt()).equals(user.getHashKey())){
            json.put("code",-1);
            json.put("message","密码错误");
            return json;
        }
        User user1=getPassword(newPassword);
        System.out.println(user1+"`````````````");
        user.setHashKey(user1.getHashKey());
        System.out.println(user.getHashKey()+"`````````````");
        user.setSalt(user1.getSalt());
        System.out.println(user.getSalt()+"`````````````");
        user.setUpdateAt(System.currentTimeMillis());
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","修改成功");
        return json;
    }
//验证密码
    @Override
    public JSONObject verificationPassword(long id, String password) {
        JSONObject json =new JSONObject();
        User user= userMapper3.findUserById(id);
        if(!SHA.getSHAwithSalt(password, user.getSalt()).equals(user.getHashKey())){
            json.put("code",-1);
            json.put("message","密码错误");
            return json;
        }
        json.put("code",0);
        json.put("message","成功");
        return json;
    }


}
