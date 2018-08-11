package com.jnshu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理重定向
 * @author wangqichao
 */
@RestController
public class RedirectController {
    private static final Logger log= LoggerFactory.getLogger(RedirectController.class);

    /**
     * 重定向到未登录接口，返回code码和message
     * @return
     */
    @GetMapping(value = "/a/unlogin")
    public Map unLogin(){
        Map<String,Object> map=new HashMap<>();
        map.put("code",10001);
        map.put("message","未登录，请重新登陆");
        return map;
    }

}
