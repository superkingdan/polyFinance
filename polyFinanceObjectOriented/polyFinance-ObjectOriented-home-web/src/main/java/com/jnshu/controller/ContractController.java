package com.jnshu.controller;

import com.jnshu.dto1.ContractRO;
import com.jnshu.exception.MyException;
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
 * 查看合同相关接口,1E
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
    public Map getContractUnsigned(@PathVariable(value = "type")int type)throws Exception{
       log.info("获得未签署合同,合同种类为："+type);
        Map<String,Object> map=new HashMap<>();
        if(type>6||type<0){
            log.error("请求合同类型有误");
            throw new MyException(10040,"type is error");
        }
        String contractUrl="";
        try {
            contractUrl = contractService.getContractUnsigned(type);
        }catch (Exception e){
            log.error("请求合同时发生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"unknown error");
        }
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
            log.info("获取待签署合同，但是cookie中没有uid");
            throw new MyException(10001,"there is no uid in cookie");
        }
        log.info("用户"+id+"获得待签署合同");
        ContractRO ro;
        try {
            ro = contractService.getContractReadySign(id);
        }catch (Exception e)
        {
            log.error("用户"+id+"获得待签署合同，产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"unknown error");
        }
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
    public Map getContract(@PathVariable(value = "id")long id)throws Exception{
        log.info("查看合同id为"+id+"的合同");
        ContractRO ro;
        Map<String,Object> map=new HashMap<>();
        try {
            ro=contractService.getContractById(id);
        }catch (Exception e){
            log.info("查看合同id为"+id+"的合同,产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"unknown error");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }
}
