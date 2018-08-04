package com.jnshu.controller;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.service1.PaymentService;
import com.jnshu.service1.TransactionService;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付模块相关接口
 * @author wangqichao
 */
@RestController
public class PaymentController {
    private static final Logger log= LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;
    @Autowired
    TransactionService transactionService;

    /**
     * 获得投资界面用户银行卡信息
     * @return 用户银行卡信息
     */
    @GetMapping(value = "/a/u/r/pay/investment")
    public Map getInvestment(HttpServletRequest request)throws Exception{
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
            log.info("获取用户银行卡，但是cookie中没有uid");
            return map;
        }
        log.info("获得id为"+id+"的用户的银行卡信息");
        List<BankCardRO> ros=paymentService.getInvestment(id);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ros);
        return map;
    }

    /**
     * 添加续投订单
     * @param id 原订单id
     * @param userSign 用户签名
     * @return 返回参数，code,message
     */
    @PostMapping(value = "/a/u/r/pay/continue")
    public Map addRenewal(@RequestParam(value = "id")long id,@RequestParam(value="userSign")String userSign){
        log.info("添加续投订单，原交易id为"+id);
        int a=transactionService.addRenewal(id,userSign);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 创建合同并返回合同id
     * @param userSign 用户签名
     * @return 返回参数，code,message,合同id
     */
    @PostMapping(value = "/a/u/r/pay/contract")
    public Map addUserSign(@RequestParam(value="userSign")String userSign,@RequestParam(value="productId")Long productId,HttpServletRequest request)throws Exception{

        Map<String,Object> map=new HashMap<>();
        //获取用户id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("创建合同，但是cookie中没有uid");
            return map;
        }
        log.info("用户"+id+"签署完合同，开始支付");

        map.put("code",0);
        map.put("message","success");
        map.put("contractId",123456);
        return map;
    }

    /**
     * 获得第三方银行验证码
     * @param phone 手机号
     * @param bankCardId 银行卡id
     * @return code,message
     */
    @PostMapping(value = "/a/u/r/pay/code")
    public Map sendCode(@RequestParam(value="phone")String phone,@RequestParam(value="bankCardId")Long bankCardId){
        long id=1L;
        log.info("用户"+id+"发送银行短信,手机号为"+phone+"，银行卡id为"+bankCardId);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        return map;
    }

    /**
     * 用户支付
     * @param rpo 用户支付信息
     * @return 支付结果,code,message,生成的交易号
     */
    @PostMapping(value = "/a/u/r/pay/payment")
    public Map pay(@ModelAttribute PaymentRPO rpo){
        long id=1L;
        log.info("用户"+id+"支付，支付信息为"+rpo);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        map.put("transactionId",4568752);
        return map;
    }

}
