package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.ContentMapper3;
import com.jnshu.dao3.FeedbackMapper3;
import com.jnshu.dao3.SystemDataMapper3;
import com.jnshu.entity.Content;
import com.jnshu.entity.Feedback;
import com.jnshu.entity.SystemData;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public JSONObject getHelp() throws MyException {
        JSONObject json = new JSONObject();
        List<Content> contents=contentMapper3.findToList(1);
        if (contents==null||contents.size()==0){
            throw new MyException(-1,"没有数据");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data", contents.get(0));
        return json;
    }

    /*关于我们*/
    @Override
    public JSONObject getAbout() throws MyException {
        JSONObject json = new JSONObject();
        List<Content> contents=contentMapper3.findToList(2);
        if (contents==null||contents.size()==0){
            throw new MyException(-1,"没有数据");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data", contents.get(0));
        return json;
    }

    /*反馈*/ //8.13 未push
    @Override
    public JSONObject feedback(long id,String content){
        JSONObject json = new JSONObject();
        Feedback feedback =new Feedback();
        feedback.setCreateAt(System.currentTimeMillis());
        feedback.setUserId(id);
        feedback.setContent(content);
        feedbackMapper3.addFeedback(feedback);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }

    /*更新内容*/
    @Override
    public JSONObject getUpdata() throws MyException {
        JSONObject json = new JSONObject();
        List<SystemData> systemDatas= systemDataMapper3.findByDataName("更新");
        System.out.println(systemDatas);
        if (systemDatas==null||systemDatas.size()==0){
            throw new MyException(-1,"已是最新版本");
        }
        json.put("code",0);
        json.put("message","有更新");
        json.put("data",systemDatas.get(0));
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
