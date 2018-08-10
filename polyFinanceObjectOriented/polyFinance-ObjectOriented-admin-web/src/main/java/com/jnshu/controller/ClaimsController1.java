package com.jnshu.controller;

import com.github.pagehelper.Page;
import com.jnshu.entity.Claims;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ClaimsService1;
import com.jnshu.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 债权管理相关接口，1E
 * @author  wangqichao
 */
@RestController
public class ClaimsController1 {
    private static final Logger log= LoggerFactory.getLogger(ClaimsController1.class);
    @Autowired
    ClaimsService1 claimsService1;
    /**
     * 获得债权列表，无必传参数
     * @param rpo 接收前端查询数据
     * @return 返回参数，包括code,message,债权列表
     */
    @GetMapping(value = "/a/u/claims/list")
    public Map getClaimsList(@ModelAttribute ClaimsListRPO rpo)throws Exception{
        log.info("查询债权列表，条件是"+rpo);
        Page<Claims> claimsPage;
        try {
            claimsPage= claimsService1.getClaimsList(rpo);
        }catch (Exception e){
            log.error("获得债权合同列表，产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        map.put("total",claimsPage.getTotal());
        map.put("size",claimsPage.getPageSize());
        System.out.println(claimsPage);
        map.put("data",claimsPage);
        return map;
    }

    /**
     * 获得具体指定债权
     * @param id 指定债权Id
     * @return 返回参数，包括code,message,指定债权数据
     */
    @GetMapping(value = "/a/u/claims/{id}")
    public  Map getClaims(@PathVariable(value = "id")long id)throws Exception{
        log.info("查询债权详情，债权id为"+id);
        Map<String,Object> map=new HashMap<>();
        Claims claims;
        try{
            claims= claimsService1.getClaimsById(id);
        }catch (Exception e){
            log.error("获得指定债权"+id+"信息产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("data",claims);
        return map;
    }

    /**
     * 修改指定债权
     * @param claims 接收修改内容
     * @param id 指定债权id
     * @return 修改结果，包括code ,message
     * 不需要修改债权代号！！！
     */
    @PutMapping(value = "/a/u/claims/{id}")
    public Map updateClaims(@ModelAttribute Claims claims, @PathVariable(value = "id")long id, HttpServletRequest request)throws Exception{
        System.out.println(claims);
        if(claims.getClaimsCode()==null||claims.getCreditor()==null||claims.getCreditorIdCard()==null||claims.getCreditorPhoneNumber()==null||
                claims.getLendDeadline()==null||claims.getLendStartAt()==null||claims.getLendMoney()==null||claims.getClaimsNature()==null||claims.getClaimsInterestRate()==null||claims.getRemark()==null){
            throw new MyException(10002,"参数不能为空");
        }
        claims.setId(id);
        claims.setUpdateAt(System.currentTimeMillis());
        String updateByS=CookieUtil.getCookieValue(request,"uid");
        if (updateByS!=null) {
            long updateBy = Long.parseLong(updateByS);
            claims.setUpdateBy(updateBy);
        }//如果cookie中没有uid直接报错
        else {
            log.info("修改指定债权，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        log.info("修改债权，修改内容为"+claims);
        Map<String,Object> map=new HashMap<>();
        try {
            claimsService1.updateClaims(claims);
        }catch (Exception e){
            log.error("修改债权产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 新增债权
     * @param claims 接收新增内容
     * @return 新增结果，包括code,message，id看情况返回
     * 债权代号不能重复，需处理异常
     */
    @PostMapping(value = "/a/u/claims")
    public Map addClaims(@ModelAttribute Claims claims, HttpServletRequest request)throws Exception{
        if(claims.getClaimsCode()==null||claims.getCreditor()==null||claims.getCreditorIdCard()==null||claims.getCreditorPhoneNumber()==null||
                claims.getLendDeadline()==null||claims.getLendStartAt()==null||claims.getLendMoney()==null||claims.getClaimsNature()==null||claims.getClaimsInterestRate()==null||claims.getRemark()==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("新增债权，内容为"+claims);
        claims.setCreateAt(System.currentTimeMillis());
        String createByS=CookieUtil.getCookieValue(request,"uid");
        if(createByS!=null) {
            long createBy = Long.parseLong(createByS);
            claims.setCreateBy(createBy);
        }else {
            log.info("新增债权，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        try {
            claimsService1.addClaims(claims);
        }catch (Exception e){
            log.error("新增债权产生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }


}
