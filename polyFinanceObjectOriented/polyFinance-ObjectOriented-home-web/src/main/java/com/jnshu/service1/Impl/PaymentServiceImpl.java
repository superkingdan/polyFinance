package com.jnshu.service1.Impl;

import com.jnshu.dao.*;
import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.*;
import com.jnshu.service1.PaymentService;
import com.jnshu.utils.TransString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 支付相关service
 * @author wangqichao
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private static final Logger log= LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    BankCardMapper bankCardMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    BankMapper bankMapper;
    @Autowired
    TransactionLogMapper transactionLogMapper;
    @Autowired
    UserMapper userMapper;


    /**
     * 获得用户银行卡信息
     * @param id 用户id
     * @return 用户银行卡信息
     */
    @Override
    public List<BankCardRO> getInvestment(long id) {
        log.info("获取用户"+id+"的银行卡信息");
        return bankCardMapper.getBankCardInfoByUserId(id);
    }

    /**
     *根据用户签名，用户id和产品id生成新的合同
     * @param userSign 用户签名
     * @param productId 产品id
     * @param userId 用户id
     * @return 合同id
     */
    @Override
    public Long addContract(String userSign, long productId, long userId) {
        //查找最新合同号
        String newestContractCode=contractMapper.getNewestContractCode();
        System.out.println("最新合同号为"+newestContractCode);
        //查找产品id对应的productCode
        String productCode=productMapper.getProductById(productId).getProductCode();
        //生成合同编号
        String contractCode= TransString.transContractCode(newestContractCode,productCode);
        System.out.println("新合同编号为"+contractCode);
        Contract contract=new Contract();
        contract.setCreateAt(System.currentTimeMillis());
        contract.setCreateBy(userId);
        contract.setUserSign(userSign);
        contract.setContractCode(contractCode);
        contract.setIsPay(Contract.IS_PAY_NO);
        contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_NO);
        //添加到合同表中去
        contractMapper.addContractCode(contract);
        return contract.getId();
    }

    /**
     * 生成交易流水
     * @param rpo 需要的信息
     * @return 生成的流水id
     */
    @Override
    public Long addPayTransactionLog(PaymentRPO rpo) {
        //创建新的交易流水对象
        TransactionLog transactionLog=new TransactionLog();
        transactionLog.setCreateAt(System.currentTimeMillis());
        transactionLog.setCreateBy(rpo.getUserId());
        transactionLog.setUserId(rpo.getUserId());
        transactionLog.setContractId(rpo.getContractId());
        //查询产品名
        String productName=productMapper.getProductNameByProductId(rpo.getProductId());
        transactionLog.setProductName(productName);
        transactionLog.setTransactionAt(System.currentTimeMillis());
        transactionLog.setMoney(rpo.getMoney());
        transactionLog.setStatus(TransactionLog.STATUS_PAY_FAIL);
        BankCard card=bankCardMapper.getBankIdById(rpo.getBankCardId());
        String bankName=bankMapper.getBankNameById(card.getBankId());
        String transactionWay=bankName+","+card.getBankCard();
        transactionLog.setTransactionWay(transactionWay);
        //插入交易流水
        int a=transactionLogMapper.addTransactionLog(transactionLog);
        return transactionLog.getId();
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户真实信息
     */
    @Override
    public User getUserInfo(long userId) {
        return userMapper.getUserRealInfo(userId);
    }

    /**
     * 获取用户银行卡信息
     * @param bankCardId 银行卡id
     * @return 银行卡信息
     */
    @Override
    public BankCard getBankCard(long bankCardId) {
        return bankCardMapper.getBankIdById(bankCardId);
    }
}
