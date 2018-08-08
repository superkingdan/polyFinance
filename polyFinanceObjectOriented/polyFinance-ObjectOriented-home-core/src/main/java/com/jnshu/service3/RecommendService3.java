package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component(value = "recommendService3")
public interface RecommendService3 {
//    banner图
    JSONObject findBanner();
}
