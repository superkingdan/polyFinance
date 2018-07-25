package com.jnshu.controller;

import com.jnshu.entity.Claims;
import com.jnshu.entity.ClaimsListRPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债权管理相关接口
 * @author  wangqichao
 */
@RestController
public class ClaimsController {
    private static final Logger log= LoggerFactory.getLogger(ClaimsController.class);

    @GetMapping(value = "/a/u/claims/list")
    public Map getClaimsList(@ModelAttribute ClaimsListRPO object){
        log.info("查询债权列表，条件是"+object);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        map.put("total",10086);
        map.put("size",object.getSize());
        List<Claims> claimsList=new ArrayList<Claims>();
        for(int i=0;i<object.getSize();i++){
            Claims claims=new Claims();
            claims.setId(1000L+i);
            claims.setClaimsCode("XTR");
            claims.setCreditor("小旋风");
            claims.setCreditorPhoneNumber("13555557777");
            claims.setCreditorIdCard("660523199001011234");
            claims.setLendDeadline(12);
            claims.setLendStartAt(System.currentTimeMillis());
            claims.setLendEndAt(System.currentTimeMillis()+12*30*24*3600*1000L);
            claims.setLendMoney("100000");
            claims.setStatus(1);
            claims.setRemanentMoney("50000");
            claimsList.add(claims);
        }
        map.put("data",claimsList);
        return map;
    }

    @GetMapping(value = "/a/u/claims/{id}")
    public  Map getClaims(@PathVariable(value = "id")long id){
        log.info("查询债权详情，债权id为"+id);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        Claims claims=new Claims();
        claims.setId(id);
        claims.setClaimsCode("XTR");
        claims.setCreditor("小旋风");
        claims.setCreditorPhoneNumber("13555557777");
        claims.setCreditorIdCard("660523199001011234");
        claims.setLendDeadline(12);
        claims.setLendStartAt(System.currentTimeMillis());
        claims.setLendEndAt(System.currentTimeMillis()+12*30*24*3600*1000L);
        claims.setLendMoney("100000");
        claims.setClaimsNature("自然");
        claims.setClaimsInterestRate("0.18");
        claims.setRemark("无");
        claims.setRemanentMoney("50000");
        map.put("data",claims);
        return map;
    }

    @PutMapping(value = "/a/u/claims/{id}")
    public Map updateClaims(@ModelAttribute Claims claims,@PathVariable(value = "id")long id){
        claims.setId(id);
        log.info("修改债权，修改内容为"+claims);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        return map;
    }

    @PostMapping(value = "/a/u/claims")
    public Map addClaims(@ModelAttribute Claims claims){
        log.info("新增债权，内容为"+claims);
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        log.info("新增债权id为"+1L);
        return map;
    }


}
