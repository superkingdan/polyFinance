package com.jnshu.controller2;

import com.jnshu.model.userApplication.UserApplication;
import com.jnshu.model.userApplication.UserApplicationInfo;
import com.jnshu.service.UserApplicationService;
import com.jnshu.utils.CAM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//实名认证
@RestController
public class UserApplicationController {
    private CAM cam = new CAM();
    @Autowired
    UserApplicationService userApplicationService;

    //实名列表
    @RequestMapping(value = "/a/u/applications")
    public Object getApplications(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String phoneNumber, @RequestParam(required = false) String realName, @RequestParam(required = false) BigDecimal createAt1, @RequestParam(required = false) BigDecimal createAt2, @RequestParam(required = false) String userNumber, @RequestParam(required = false) BigDecimal applicationStatus){
        List<Object> result = new ArrayList<>();
        List<UserApplication> userApplications = userApplicationService.getAllUser(pageNum, pageSize);
        result.add(cam);
        Map<String,Integer> s = new HashMap<>();
        s.put("total", 100);
        result.add(s);
        result.add(userApplications);
        result.add("实名列表  接收到的参数："+",phoneNumber："+phoneNumber+", realNumber:"+realName+", createAt1:"+createAt1+", createAt2:"+createAt2+"，userNumber"+userNumber+",applictionStatus:"+String.valueOf(applicationStatus));
        return result;
    }

    //实名详情
    @RequestMapping(value = "/a/u/applications/{id}" ,method = RequestMethod.GET)
    public Object getApplicationInfo(@PathVariable long id){
        UserApplicationInfo user = new UserApplicationInfo(id,"3453245234354"+id,System.currentTimeMillis()+100000000L,"1369894564","张三",String.valueOf(879079834),"geim@163.com",(int) (id%2),"拒绝","","");
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add(user);
        return result;
    }

    //取消实名
    @RequestMapping(value = "/a/u/applications/{id}/cancel",method = RequestMethod.PUT)
    public Object handAppliStatus(@PathVariable long id, @RequestParam int applicationStatus){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("取消实名  接收的参数：id="+String.valueOf(id)+", applicationStatus="+String.valueOf(applicationStatus));
        return result;
    }

    //审核申请
    @RequestMapping(value = "/a/u/applications/{id}/verify",method = RequestMethod.PUT)
    public Object handAppli(@PathVariable long id, @RequestParam int applicationStatus, @RequestParam(required = false) String refuseReason){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("实名申请  接收的参数：id="+String.valueOf(id)+", applicationStatus="+String.valueOf(applicationStatus) + ", refuseReason="+refuseReason);
        return result;
    }

}