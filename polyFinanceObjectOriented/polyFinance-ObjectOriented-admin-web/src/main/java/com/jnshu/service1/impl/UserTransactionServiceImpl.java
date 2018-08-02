package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.*;
import com.jnshu.dto1.TransactionListBackRO;
import com.jnshu.dto1.TransactionListRPO;
import com.jnshu.dto1.TransactionLogRPO;
import com.jnshu.entity.*;
import com.jnshu.service1.UserTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台用户管理相关service
 * @author wangqichao
 */
@Service
@Transactional
public class UserTransactionServiceImpl implements UserTransactionService{
    @Autowired
    TransactionLogMapper transactionLogMapper;
    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    SystemDataMapper systemDataMapper;
    @Autowired
    ClaimsMapper claimsMapper;
    @Autowired
    UserMapper userMapper;

    private static final Logger log= LoggerFactory.getLogger(UserTransactionServiceImpl.class);
    /**
     * 获得用户交易流水列表
     * @param rpo 查询条件
     * @return 查询结果
     */
    @Override
    public Page<TransactionLog> getTransactionLogList(TransactionLogRPO rpo) {
        Page<TransactionLog> page= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        transactionLogMapper.getTransLogListByRpo(rpo);
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
        transactionMapper.getTransactionListByUserId(rpo);
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
        ro.setContract(systemDataMapper.getContractUrl());
        ro.setCompanyCachet(systemDataMapper.getCompanyCachet());
        //设置合同相关信息
        Contract contract=contractMapper.getHaveSignContractByCode(contractCode);
        ro.setUserSign(contract.getUserSign());
        ro.setContractCreateAt(contract.getCreateAt());
        //如果已经匹配债权的话，将债权人信息放进去
        if(contract.getCurrentClaimsCode()!=null){
            Claims claims=claimsMapper.getCreditorInfoByClaimsCode(contract.getCurrentClaimsCode());
            ro.setCreditor(claims.getCreditor());
            ro.setCreditorIdCard(claims.getCreditorIdCard());
        }
        //设置用户信息
        User user=userMapper.getUserInfoByContractCode(contractCode);
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
        ro.setContract(systemDataMapper.getContractUrl());
        ro.setCompanyCachet(systemDataMapper.getCompanyCachet());
        //设置合同相关信息
        Contract contract=contractMapper.getClaimsCodeByCode(claimsProtocolCode);
        ro.setClaimsCreateAt(contract.getUpdateAt());
        ro.setUserSign(contract.getUserSign());
        //设置债权人信息
        Claims claims=claimsMapper.getCreditorInfoByClaimsCode(claimsProtocolCode);
        ro.setCreditor(claims.getCreditor());
        ro.setCreditorIdCard(claims.getCreditorIdCard());
        //设置用户信息
        User user=userMapper.getUserInfoByContractCode(contract.getContractCode());
        ro.setUserName(user.getRealName());
        ro.setUserIdCard(user.getIdCard());
        return ro;
    }
}
