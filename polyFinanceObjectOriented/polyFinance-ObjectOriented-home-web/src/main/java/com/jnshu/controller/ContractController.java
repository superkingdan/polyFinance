package com.jnshu.controller;

import com.jnshu.dto1.ContractRO;
import com.jnshu.service1.ContractService;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 查看合同相关接口
 * @author wangqichao
 */
@RestController
public class ContractController {
    private static final Logger log= LoggerFactory.getLogger(ContractController.class);

    @Autowired
    ContractService contractService;

    /**
     * 获得未签署合同原图
     * @param type 合同种类
     * @return 合同url
     */
    @GetMapping(value = "/a/u/contract/unsigned/{type}")
    public Map getContractUnsigned(@PathVariable(value = "type")int type){
       log.info("获得未签署合同");
       String contractUrl=contractService.getContractUnsigned(type);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        map.put("contract",contractUrl);
        return map;
    }

    /**
     * 获得待签署合同信息
     * @return 用户待签署合同信息，code,message,data
     */
    @GetMapping(value = "/a/u/contract/ready-signed")
    public Map getReadyContract(HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        //从cookie获得id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("获取待签署合同，但是cookie中没有uid");
            return map;
        }
        log.info("用户"+id+"获得待签署合同");
        ContractRO ro=contractService.getContractReadySign(id);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }

    /**
     * 获得用户指定合同信息
     * @param id 合同id
     * @return 合同相关信息
     */
    @GetMapping(value = "/a/u/contract/{id}")
    public Map getContract(@PathVariable(value = "id")long id){
        log.info("查看合同id为"+id+"的合同");
        Map<String,Object> map=new HashMap<>();
        ContractRO ro=contractService.getContractById(id);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }
}
