package com.jnshu.controller;

import com.jnshu.entity.ContinuedInvestmentListRO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 获得续投列表
     * @return 返回参数,code,message,续投
     */
    @GetMapping(value = "/a/u/transaction/list/continue")
    public Map getContinueInvList(){
        //注意要判断续投时间
        long id=1L;
        log.info("获得用户"+id+"的可续投列表");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10000);
        map.put("message","ok");
        List<ContinuedInvestmentListRO> ros=new ArrayList<>();
        for(int i=0;i<10;i++){
            ContinuedInvestmentListRO ro=new ContinuedInvestmentListRO();
            ro.setId(1245+i);
            ro.setProductName("金玉满堂");
            ro.setInterestRate("0.18");
            ro.setMoney("50000");
            ro.setMark(2);
            ro.setStartAt(System.currentTimeMillis());
            ro.setEndAt(System.currentTimeMillis()+6*30*24*3600*1000L);
            ros.add(ro);
        }
        map.put("data",ros);
        return map;
    }
}
