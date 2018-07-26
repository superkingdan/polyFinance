package com.jnshu.controller2;

import com.jnshu.model.module.ListModule;
import com.jnshu.model.module.Module;
import com.jnshu.model.module.ModuleListInit;
import com.jnshu.model.module.ModuleProduce;
import com.jnshu.model.user.LoginBack;
import com.jnshu.model.user.User;
import com.jnshu.model.user.UserInfo;
import com.jnshu.service.UserService;
import com.jnshu.utils.CAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FrontUserController {

    private CAM cam = new CAM();

    @Autowired
    UserService userService;
    //登录接口
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object backLogin(User user){

        LoginBack loginBack = new LoginBack("admin","超级管理员");
        ModuleListInit moduleListInit=new ModuleListInit();
        List<Module> list = moduleListInit.init();
        ModuleProduce moduleProduce = new ModuleProduce();
        List<ListModule> moduleProduceSuper=moduleProduce.getSuper(list);

        List<Object> objectList = new ArrayList<>();

        objectList.add(cam);
        objectList.add(loginBack);
        objectList.add(moduleProduceSuper);
        return objectList;
    }

    /**
     * 用户管理
     */
    //用户列表
    @RequestMapping(value = "/a/u/users",method = RequestMethod.GET)
    public Object userList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String realName, @RequestParam(required = false) BigDecimal createAt1, @RequestParam(required = false) BigDecimal createAt2, @RequestParam(required = false) String referrerId, @RequestParam(required = false) Integer status){

        List<Object> result = new ArrayList<>();
        List<User> users = userService.getAllUser(pageNum, pageSize);

        result.add(cam);
        result.add("total:"+100);
        result.add("用户列表    接收到的参数："+"pageNum：" + pageNum+"，pageSize："+pageSize+", phoneNumber:"+phoneNumber+ ", realName:"+realName+", createAt1:"+createAt1+", createAt2:"+createAt2+",status:"+status);
        result.add(users);
        return result;
    }

    //用户详情
    @RequestMapping(value = "/a/u/users/{id}",method = RequestMethod.GET)
    public Object userInfo(@PathVariable long id){
        List<Object> result = new ArrayList<>();

            UserInfo user = new UserInfo(id,"UK1810"+000+String.valueOf(id),System.currentTimeMillis()+100000000L,"136989",String.valueOf(id),(int) id%2,"张三","3453245234354"+String.valueOf(id),"1234@qq.com","地球",String.valueOf(879079834),String.valueOf(4353453),(int) id%2,"","",3L,"中国银行","12435345345",67L,"工商银行","7987593875923457932");

        result.add(cam);
        result.add("用户详情    接收到的数据：id=" + String.valueOf(id));
        result.add(user);
        return result;
    }

    //用户列表-冻结/解冻
    @RequestMapping(value = "/a/u/users/{id}/status",method = RequestMethod.PUT)
    public Object userStatus(@PathVariable BigDecimal id, @RequestParam Integer status){

        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("用户列表-冻结/解冻  接收到的数据：id=" + id+ "，status="+status);
        return result;
    }

    //修改手机
    @RequestMapping(value = "/a/u/users/{id}/phone",method = RequestMethod.PUT)
    public Object userPhone(@PathVariable long id, @RequestParam String phoneNumber){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("修改手机    接收到的数据：id=" + String.valueOf(id)+ "，phoneNumber="+phoneNumber);
        return result;
    }

    //修改理财经理
    @RequestMapping(value = "/a/u/users/{id}/referrer",method = RequestMethod.PUT)
    public Object userReferrer(@PathVariable long id, @RequestParam String referrerId){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("修改理财经理  接收到的数据：id=" + String.valueOf(id)+ "，referrerId="+referrerId);
        return result;
    }

    //取消实名
    @RequestMapping(value = "/a/u/users/{id}/realStatus",method = RequestMethod.PUT)
    public Object userRealStatus(@PathVariable long id, @RequestParam String realStatus){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("取消实名  接收到的数据：id=" + String.valueOf(id)+ "，realStatus="+realStatus);
        return result;
    }

    //解绑银行卡
    @RequestMapping(value = "/a/u/users/{id}/bankCard",method = RequestMethod.PUT)
    public Object userBankCard(@PathVariable long id, @RequestParam String bankId){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("解绑银行卡   接收到的数据：id=" + String.valueOf(id)+ "，bankId="+bankId);
        return result;
    }
}
