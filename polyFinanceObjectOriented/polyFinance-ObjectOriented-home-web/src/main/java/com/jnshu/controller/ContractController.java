package com.jnshu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 查看合同相关接口
 * @author wangqichao
 */
@RestController
public class ContractController {
    private static final Logger log= LoggerFactory.getLogger(ContractController.class);

    @GetMapping(value = "/a/u/contract/unsigned/{type}")
    public Map getContractUnsigned(@PathVariable(value = "type")int type){
       log.info("获得未签署合同");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        map.put("contract","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/840.png");
        return map;
    }

    @GetMapping(value = " /a/u/contract/ready-signed")
    public Map getReadyContract(){
        long id=1L;
        log.info("用户"+id+"获得待签署合同");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        map.put("contract","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/840.png");
        map.put("userName","王大锤");
        map.put("userIdCard","330512196908195487");
        map.put("companyCachet","https://jnshuphoto.oss-cn-hangzhou.aliyuncs.com/headphoto/842.png");
        return map;
    }
}
