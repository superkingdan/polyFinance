package com.jnshu.service1.Impl;

import com.jnshu.dao.TransactionLogMapper1;
import com.jnshu.entity.TransactionLog;
import com.jnshu.exception.MyException;
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
    public List<TransactionLog> getTransactionLogList(long userId) throws Exception {
        log.info("获得指定用户"+userId+"交易流水列表");
        List<TransactionLog> list;
        try{
            list=transactionLogMapper1.getTransLogLitByUserId(userId);
        }catch (Exception e){
            throw new MyException(-1,"获得交易流水出错");
        }
        return list;
    }

    /**
     * 获得用户指定交易流水
     * @param id 交易流水id
     * @return 指定交易流水详情
     */
    @Override
    public TransactionLog getTransactionLogById(long id) throws Exception{
        log.info("获得用户指定交易"+id+"流水");
        TransactionLog transactionLog;
        try{
            transactionLog=transactionLogMapper1.getTransLogById(id);
        }catch (Exception e){
            throw new MyException(-1,"获得用户指定交易流水失败");
        }
        return transactionLog;
    }
}
