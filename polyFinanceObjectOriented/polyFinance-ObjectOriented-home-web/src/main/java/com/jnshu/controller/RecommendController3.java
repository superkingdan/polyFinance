package com.jnshu.controller;


import com.alibaba.fastjson.JSONObject;
import com.jnshu.exception.MyException;
import com.jnshu.service3.RecommendService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 推荐页
 * @return
 */
@Controller
@RequestMapping("/a")
public class RecommendController3 {
    @Autowired
    RecommendService3 recommendService3;
    private static final Logger log= LoggerFactory.getLogger(RecommendController3.class);

    /**
     * banner图 /完成
     * @return banner图信息
     */
    @GetMapping("/content/banner")
    @ResponseBody
    public JSONObject findBack(HttpServletRequest request)throws MyException {

        return recommendService3.findBanner();
    }



}
