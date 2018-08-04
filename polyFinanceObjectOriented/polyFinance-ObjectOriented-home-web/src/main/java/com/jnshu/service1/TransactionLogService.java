package com.jnshu.service1;

import com.jnshu.entity.TransactionLog;

import java.util.List;

public interface TransactionLogService {

    List<TransactionLog> getTransactionLogList(long userId);

    TransactionLog getTransactionLogById(long id);
}
