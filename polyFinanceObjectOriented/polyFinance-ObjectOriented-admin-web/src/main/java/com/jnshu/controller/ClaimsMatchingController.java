package com.jnshu.controller;

import com.jnshu.entity.Claims;
import com.jnshu.entity.ClaimsMatchingRO;
import com.jnshu.entity.ClaimsMatchingRPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债权匹配相关接口
 */
@RestController
public class ClaimsMatchingController {
    private static final Logger log= LoggerFactory.getLogger(ClaimsMatchingController.class);

    @GetMapping(value = "/a/u/claims/matching/list/{id}")
    public Map getClaimsMatchingList(@ModelAttribute ClaimsMatchingRPO rpo, @PathVariable(value = "id")long id){
        rpo.setId(id);
        log.info("查找指定债权"+id+"匹配信息");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        map.put("total",10086);
        map.put("size",rpo.getSize());
        Claims claims=new Claims();
        claims.setClaimsCode("ZXC");
        claims.setLendStartAt(System.currentTimeMillis());
        claims.setLendEndAt(System.currentTimeMillis()+12*24*30*3600*1000L);
        claims.setLendMoney("1000000");
        claims.setRemanentMoney("50000");
        map.put("data1",claims);
        List<ClaimsMatchingRO> claimsMatchingROES=new ArrayList<>();
        for (int i=0;i<rpo.getSize();i++) {
            ClaimsMatchingRO matchingRO = new ClaimsMatchingRO();
            matchingRO.setContractCode("UKZXC180004"+i+"");
            matchingRO.setClaimsProtocolCode("UKZQ180004"+i+"");
            matchingRO.setUserName("诸葛亮");
            matchingRO.setProductName("三国绝版");
            matchingRO.setStartAt(System.currentTimeMillis());
            matchingRO.setMoney("150000"+i*1000+"");
            claimsMatchingROES.add(matchingRO);
        }
        map.put("data2",claimsMatchingROES);
        return map;
    }
}
