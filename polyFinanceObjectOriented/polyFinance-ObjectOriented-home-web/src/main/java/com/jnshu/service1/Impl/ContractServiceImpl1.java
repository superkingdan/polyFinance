package com.jnshu.service1.Impl;

import com.jnshu.dao.ClaimsMapper1;
import com.jnshu.dao.ContractMapper1;
import com.jnshu.dao.SystemDataMapper1;
import com.jnshu.dao.UserMapper1;
import com.jnshu.dto1.ContractRO;
import com.jnshu.entity.Claims;
import com.jnshu.entity.Contract;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ContractService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 合同相关service,2E
 * @author  wangqichao
 */
@Service
@Transactional
public class ContractServiceImpl1 implements ContractService1 {
    private static final Logger log= LoggerFactory.getLogger(ContractServiceImpl1.class);
    @Autowired
    SystemDataMapper1 systemDataMapper1;
    @Autowired
    UserMapper1 userMapper1;
    @Autowired
    ContractMapper1 contractMapper1;
    @Autowired
    ClaimsMapper1 claimsMapper1;

    /**
     * 获得指定类型的合同原图
     * @param type 指定的合同类型
     * @return 原图url
     */
    @Override
    public String getContractUnsigned(int type) throws Exception {
        log.info("获得类型为"+type+"的待签署合同");
        String url="";
        try{
            url=systemDataMapper1.getContractUrl();
        }catch (Exception e){
            throw new MyException(-1,"请求合同时发生错误");
        }
        return url;
    }

    /**
     * 获得待签署合同
     * @param id 用户id
     * @return 合同相关信息
     */
    @Override
    public ContractRO getContractReadySign(long id) throws Exception{
        log.info("获得用户"+id+"待签署合同的信息");
        ContractRO ro=new ContractRO();
        try{
            ro.setContract(systemDataMapper1.getContractUrl());
        }catch (Exception e){
            throw new MyException(-1,"请求合同时发生错误");
        }
        User user;
        try{
            user= userMapper1.getUserRealInfo(id);
        ro.setUserName(user.getRealName());
        ro.setUserIdCard(user.getIdCard());
        }catch (Exception e){
            throw new MyException(-1,"获得用户信息时发生错误");
        }
        return ro;
    }

    /**
     * 获得用户已签署合同
     * @param id 合同id
     * @return 合同相关信息
     */
    @Override
    public ContractRO getContractById(long id) throws Exception{
        log.info("获得指定id为"+id+"的合同");
        //创建返回合同对象
        ContractRO ro=new ContractRO();
        //查询合同原图
       try{
           ro.setContract(systemDataMapper1.getContractUrl());
       }catch (Exception e){
           throw new MyException(-1,"获得合同失败");
       }
        //查询公司公章
        try{
           ro.setCompanyCachet(systemDataMapper1.getCompanyCachet());
        }catch (Exception e){
            throw new MyException(-1,"获得公章失败");
        }
        //查询用户相关信息
        Contract contract;
        try{
            contract= contractMapper1.getHaveSignContractById(id);
            ro.setContractCode(contract.getContractCode());
            ro.setContractCreateAt(contract.getCreateAt());
             ro.setUserSign(contract.getUserSign());
        }catch (Exception e){
            throw new MyException(-1,"获得已签署信息失败");
        }
        User user;
        try{
            user= userMapper1.getUserInfoByContractCode(contract.getContractCode());
            ro.setUserName(user.getRealName());
            ro.setUserIdCard(user.getIdCard());
        }catch (Exception e){
            throw new MyException(-1,"获得用户信息失败");
        }
        //获得并设置债权人相关信息
        if(contract.getCurrentClaimsCode()!=null) {
            Claims claims;
            try {
                claims = claimsMapper1.getCreditorInfoByClaimsCode(contract.getCurrentClaimsCode());
                ro.setCreditor(claims.getCreditor());
                ro.setCreditorIdCard(claims.getCreditorIdCard());
            }catch (Exception e){
                throw new MyException(-1,"获得债权信息失败");
            }
        }
        System.out.println(ro);
        return ro;
    }
}
