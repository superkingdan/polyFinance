package com.jnshu.controller;

import com.github.pagehelper.Page;
import com.jnshu.dto1.*;
import com.jnshu.entity.*;
import com.jnshu.service1.UserTransactionService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *后台用户管理获得指定用户交易和投资记录相关接口
 * @author wangqichao
 */
@RestController
public class UserTransactionController1 {
    private static final Logger log= LoggerFactory.getLogger(UserTransactionController1.class);

    @Autowired
    UserTransactionService1 userTransactionService1;

    /**
     * 获得指定用户e
     * @param rpo 接收前端分页参数的对象
     * @param id 指定用户id
     * @return 返回参数，code,message,用户流水列表
     */
    @GetMapping(value = "/a/u/user/transaction-log/list/{id}")
    public Map getTransactionLogList(@ModelAttribute TransactionLogRPO rpo, @PathVariable(value = "id")long id){
        rpo.setId(id);
        log.info("查询指定用户"+id+"流水记录，查询条件为"+rpo);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        Page<TransactionLog> logPage= userTransactionService1.getTransactionLogList(rpo);
        map.put("total",logPage.getTotal());
        map.put("size",rpo.getSize());
        map.put("data",logPage);
        return map;
    }

    /**
     * 获得指定用户投资列表
     * @param rpo 接收参数的对象
     * @param id 用户id
     * @return 返回参数，code,message,投资列表
     */
    @GetMapping(value = "/a/u/user/transaction/list/{id}")
    public Map getTransactionList(@ModelAttribute TransactionListRPO rpo, @PathVariable(value = "id")long id){
        rpo.setId(id);
        log.info("查询指定用户"+id+"投资列表");
        Page<TransactionListBackRO> page= userTransactionService1.getTransactionList(rpo);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        map.put("total",page.getTotal());
        map.put("size",rpo.getSize());
        map.put("data",page);
        return map;
    }

    /**
     * 获得用户指定合同信息
     * @param code 合同编号
     * @return 合同相关信息
     */
    @GetMapping(value = "/a/u/user/transaction/contract/{code}")
    public Map getContract(@PathVariable(value = "code")String code){
        log.info("查看合同编号为"+code+"的合同");
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        ContractRO ro= userTransactionService1.getContract(code);
       map.put("data",ro);
       return map;
    }

    /**
     * 获得指定债权转让协议内容
     * @param code 债权转让协议编号
     * @return 返回参数,code,message,协议内容
     */
    @GetMapping(value = "/a/u/user/transaction/claims/{code}")
    public Map getClaimsProtocolCode(@PathVariable(value = "code")String code){
        log.info("查看债权协议编号为"+code+"的债权转让协议");
        Map<String,Object> map=new HashMap<>();
        ClaimsProtocolCodeRO ro= userTransactionService1.getClaimsProtocolCode(code);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }

}
