package com.jnshu.controller;

import com.jnshu.dao.TransactionMapper;
import com.jnshu.dto1.TransactionListRO;
import com.jnshu.dto1.TransactionRO;
import com.jnshu.service1.TransactionService;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资信息相关接口
 * @author wangqichao
 */
@RestController
public class TransactionController {
    private static final Logger log= LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionService transactionService;
    /**
     * 获得续投列表
     * @return 返回参数,code,message,续投
     */
    @GetMapping(value = "/a/u/transaction/list/continue")
    public Map getContinueInvList(HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        //注意要判断续投时间
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
            log.info("获取用户可续投列表，但是cookie中没有uid");
            return map;
        }
        log.info("获得用户"+id+"的可续投列表");
        map.put("code",0);
        map.put("message","success");
        List<TransactionListRO> ros=transactionService.getContinueInvList(id);
        map.put("data",ros);
        return map;
    }

    /**
     * 获得用户投资列表
     * @param status 投资状态
     * @return 返回参数，code,message,投资列表
     */
    @GetMapping(value = "/a/u/transaction/list/{status}")
    public Map getTransactionList(@PathVariable(value = "status")Integer status,HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("获得用户投资列表，但是cookie中没有uid");
            return map;
        }

        log.info("获得用户:{ }的投资列表列表，投资状态为:{ }",id,status);
        map.put("code",0);
        map.put("message","success");
        List<TransactionListRO> ros=transactionService.getTransactionListRO(id,status);
        map.put("data",ros);
        return map;
    }

    /**
     * 获得用户投资详情
     * @param id 交易id
     * @return code,message,交易详情data
     */
    @GetMapping(value = "/a/u/transaction/{id}")
    public Map getTransaction(@PathVariable(value = "id")long id){
        log.info("获得用户的具体投资详情，交易id为"+id);
        Map<String,Object> map=new HashMap<>();
        TransactionRO ro=transactionService.getTransactionById(id);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }
}
