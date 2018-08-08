package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.ContentMapper3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendServiceImpl3 implements RecommendService3 {
    @Autowired
    ContentMapper3 contentMapper3;

    /*banner*/
    @Override
    public JSONObject findBanner() {
        JSONObject json=new JSONObject();
        json.put("code",0);
        json.put("message","成功");
        json.put("date",contentMapper3.findToList(0));
        return json;
    }
}
