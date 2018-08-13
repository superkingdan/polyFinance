package com.jnshu.controller;

import com.github.pagehelper.Page;
import com.jnshu.entity.Claims;
import com.jnshu.dto1.ClaimsMatchingRO;
import com.jnshu.dto1.ClaimsMatchingRPO;
import com.jnshu.dto1.ContractMatchingRO;
import com.jnshu.entity.ClaimsMatching;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ClaimsMatchingService1;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 债权匹配相关接口，2E
 */
@RestController
public class ClaimsMatchingController1 {
    private static final Logger log= LoggerFactory.getLogger(ClaimsMatchingController1.class);
@Autowired
ClaimsMatchingService1 claimsMatchingService1;
    /**
     * 获得指定债权匹配列表
     * @param rpo 接收传过来的查询数据
     * @param id 需要查询的债权id
     * @return 返回数据，包括code,message,债权信息，匹配的合同信息
     */

    @GetMapping(value = "/a/u/claims/matching/list/{id}")
    public Map getClaimsMatchingList(@ModelAttribute ClaimsMatchingRPO rpo, @PathVariable(value = "id")long id)throws Exception{
        rpo.setId(id);
        log.info("查找指定债权"+id+"匹配信息");
        Map<String,Object> map=new HashMap<>();
        Claims claims= claimsMatchingService1.getClaimsInfoById(id);
        map.put("data1",claims);
        Page<ClaimsMatchingRO> claimsMatchingROPage= claimsMatchingService1.getClaimsMatchingListByRpo(rpo);
        map.put("total",claimsMatchingROPage.getTotal());
        map.put("size",rpo.getSize());
        map.put("data2",claimsMatchingROPage);
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 获得指定id债权的推荐匹配合同列表
     * @param id 指定债权Id
     * @return 返回参数，包括code,message,推荐合同列表（10条）
     */
    @GetMapping(value = "/a/u/claims/matching/setout/{id}")
    public Map getRecommendContractList(@PathVariable(value = "id")long id)throws Exception{
        log.info("获得指定债权"+id+"的适宜匹配合同列表");
        Map<String,Object> map=new HashMap<>();
        List<ContractMatchingRO> matchingROList = claimsMatchingService1.getClaimsMatchingListById(id);
        map.put("code",0);
        map.put("message","success");
        map.put("data",matchingROList);
        return map;
    }

    /**
     * 保存匹配的债权信息
     * @param id 债权id
     * @param contractCode 合同编号
     * @return 保存结果，code,message
     */
    @PostMapping(value = "/a/u/claims/matching/save")
    public Map saveMatching(@RequestParam(value = "id",required = false)Long id, @RequestParam(value = "contractCode",required = false)String contractCode, HttpServletRequest request)throws Exception{
        if(id==null||contractCode==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("保存匹配信息，债权id为"+id+"，合同编号为"+contractCode);
        ClaimsMatching claimsMatching=new ClaimsMatching();
        claimsMatching.setClaimsId(id);
        claimsMatching.setContractCode(contractCode);
        String createByS= CookieUtil.getCookieValue(request,"uid");
        long createBy = Long.parseLong(createByS);
        claimsMatching.setCreateBy(createBy);
        claimsMatchingService1.saveClaimsMatching(claimsMatching);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }
}
