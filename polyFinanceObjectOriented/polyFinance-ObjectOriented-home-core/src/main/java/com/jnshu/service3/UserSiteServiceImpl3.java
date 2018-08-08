package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.ContentMapper3;
import com.jnshu.dao3.FeedbackMapper3;
import com.jnshu.dao3.SystemDataMapper3;
import com.jnshu.entity.Feedback;
import com.jnshu.entity.SystemData;
import com.jnshu.utils3.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserSiteServiceImpl3 implements UserSiteService3 {
    @Autowired
    ContentMapper3 contentMapper3;
    @Autowired
    FeedbackMapper3 feedbackMapper3;
    @Autowired
    SystemDataMapper3 systemDataMapper3;

    /*帮助*/
    @Override
    public JSONObject getHelp() {
        JSONObject json = new JSONObject();
        if (contentMapper3.findToList(1).get(0)==null){
            json.put("code",-1);
            json.put("message","没有数据");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data", contentMapper3.findToList(1).get(0));
        return json;
    }

    /*关于我们*/
    @Override
    public JSONObject getAbout() {
        JSONObject json = new JSONObject();
        if (contentMapper3.findToList(2).get(0)==null){
            json.put("code",-1);
            json.put("message","没有数据");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data", contentMapper3.findToList(2));
        return json;
    }

    /*反馈*/
    @Override
    public JSONObject feedback(long id,String content){
        JSONObject json = new JSONObject();
        Feedback feedback =new Feedback();
        feedback.setCreateAt(System.currentTimeMillis());
        feedback.setUserId(id);
        feedbackMapper3.addFeedback(feedback);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }

    /*更新内容*/
    @Override
    public JSONObject getUpdata() {
        JSONObject json = new JSONObject();
        SystemData systemData= systemDataMapper3.findByDataName("gengxin");
        if (systemData.equals(null)){
            json.put("code",0);
            json.put("message","没有更新");
            json.put("data","已是最新版本");
            return json;
        }
        json.put("code",0);
        json.put("message","有更新");
        json.put("data",systemData);
        return json;
    }

    /*清楚缓存*/
    @Override
    public JSONObject clean(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.delCookie(response,request,"token");
        CookieUtil.delCookie(response,request,"uid");
        JSONObject json = new JSONObject();
        json.put("code",0);
        json.put("message","成功");
        return json;

    }
}
