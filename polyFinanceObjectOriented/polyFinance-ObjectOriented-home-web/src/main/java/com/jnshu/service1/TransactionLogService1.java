package com.jnshu.service1;

import com.jnshu.entity.TransactionLog;

import java.util.List;

public interface TransactionLogService1 {

    List<TransactionLog> getTransactionLogList(long userId) throws Exception;

    TransactionLog getTransactionLogById(long id)throws Exception;
}
