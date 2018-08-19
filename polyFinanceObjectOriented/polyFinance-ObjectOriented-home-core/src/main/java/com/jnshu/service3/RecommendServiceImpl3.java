package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.ContentMapper3;
import com.jnshu.entity.Content;
import com.jnshu.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendServiceImpl3 implements RecommendService3 {
    @Autowired
    ContentMapper3 contentMapper3;

    /*banner*/
    @Override
    public JSONObject findBanner() throws MyException {
        JSONObject json=new JSONObject();
        int type=0;
        List<Content> content= contentMapper3.findToList(type);
        if (content==null){
            throw new MyException(-1,"没有内容");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("date",content);
        return json;
    }
}
