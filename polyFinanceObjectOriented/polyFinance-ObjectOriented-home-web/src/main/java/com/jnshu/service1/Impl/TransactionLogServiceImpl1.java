package com.jnshu.service1.Impl;

import com.jnshu.dao.TransactionLogMapper1;
import com.jnshu.entity.TransactionLog;
import com.jnshu.service1.TransactionLogService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionLogServiceImpl1 implements TransactionLogService1 {
    private static final Logger log= LoggerFactory.getLogger(TransactionLogServiceImpl1.class);

    @Autowired
    TransactionLogMapper1 transactionLogMapper1;

    /**
     * 获得指定用户交易流水列表
     * @param userId 用户id
     * @return 用户交易流水列表
     */
    @Override
    public List<TransactionLog> getTransactionLogList(long userId) {
        return transactionLogMapper1.getTransLogLitByUserId(userId);
    }

    /**
     * 获得用户指定交易流水
     * @param id 交易流水id
     * @return 指定交易流水详情
     */
    @Override
    public TransactionLog getTransactionLogById(long id) {
        return transactionLogMapper1.getTransLogById(id);
    }
}
