package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.*;
import com.jnshu.dto1.*;
import com.jnshu.entity.*;
import com.jnshu.service1.UserTransactionService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台用户管理相关service
 * @author wangqichao
 */
@Service
@Transactional
public class UserTransactionServiceImpl1 implements UserTransactionService1 {
    @Autowired
    TransactionLogMapper1 transactionLogMapper1;
    @Autowired
    TransactionMapper1 transactionMapper1;
    @Autowired
    ContractMapper1 contractMapper1;
    @Autowired
    SystemDataMapper1 systemDataMapper1;
    @Autowired
    ClaimsMapper1 claimsMapper1;
    @Autowired
    UserMapper1 userMapper1;

    private static final Logger log= LoggerFactory.getLogger(UserTransactionServiceImpl1.class);
    /**
     * 获得用户交易流水列表
     * @param rpo 查询条件
     * @return 查询结果
     */
    @Override
    public Page<TransactionLog> getTransactionLogList(TransactionLogRPO rpo) {
        Page<TransactionLog> page= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        transactionLogMapper1.getTransLogListByRpo(rpo);
        return  page;
    }

    /**
     * 获得用户投资列表
     * @param rpo 查询条件
     * @return 查询结果
     */
    @Override
    public Page<TransactionListBackRO> getTransactionList(TransactionListRPO rpo) {
        Page<TransactionListBackRO> page=PageHelper.startPage(rpo.getPage(),rpo.getSize());
        transactionMapper1.getTransactionListByUserId(rpo);
        return page;
    }

    /**
     * 获得合同详情
     * @param contractCode 合同编号
     * @return 合同具体详情
     */
    @Override
    public ContractRO getContract(String contractCode) {
        ContractRO ro=new ContractRO();
        //设置固定数据
        ro.setContractCode(contractCode);
        ro.setContract(systemDataMapper1.getContractUrl());
        ro.setCompanyCachet(systemDataMapper1.getCompanyCachet());
        //设置合同相关信息
        Contract contract= contractMapper1.getHaveSignContractByCode(contractCode);
        ro.setUserSign(contract.getUserSign());
        ro.setContractCreateAt(contract.getCreateAt());
        //如果已经匹配债权的话，将债权人信息放进去
        if(contract.getCurrentClaimsCode()!=null){
            Claims claims= claimsMapper1.getCreditorInfoByClaimsCode(contract.getCurrentClaimsCode());
            ro.setCreditor(claims.getCreditor());
            ro.setCreditorIdCard(claims.getCreditorIdCard());
        }
        //设置用户信息
        User user= userMapper1.getUserInfoByContractCode(contractCode);
        ro.setUserName(user.getRealName());
        ro.setUserIdCard(user.getIdCard());
        return ro;
    }

    /**
     * 获得债权协议详情信息
     * @param claimsProtocolCode 债权编号
     * @return 债权协议详情
     */
    @Override
    public ClaimsProtocolCodeRO getClaimsProtocolCode(String claimsProtocolCode) {
        ClaimsProtocolCodeRO ro=new ClaimsProtocolCodeRO();
        //设置固定数据
        ro.setClaimsProtocolCode(claimsProtocolCode);
        ro.setContract(systemDataMapper1.getContractUrl());
        ro.setCompanyCachet(systemDataMapper1.getCompanyCachet());
        //设置合同相关信息
        Contract contract= contractMapper1.getClaimsCodeByCode(claimsProtocolCode);
        ro.setClaimsCreateAt(contract.getUpdateAt());
        ro.setUserSign(contract.getUserSign());
        //设置债权人信息
        Claims claims= claimsMapper1.getCreditorInfoByClaimsCode(claimsProtocolCode);
        ro.setCreditor(claims.getCreditor());
        ro.setCreditorIdCard(claims.getCreditorIdCard());
        //设置用户信息
        User user= userMapper1.getUserInfoByContractCode(contract.getContractCode());
        ro.setUserName(user.getRealName());
        ro.setUserIdCard(user.getIdCard());
        return ro;
    }
}
