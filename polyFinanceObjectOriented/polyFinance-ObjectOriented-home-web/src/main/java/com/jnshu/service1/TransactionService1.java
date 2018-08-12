package com.jnshu.service1;

import com.jnshu.dto1.TransactionListRO;
import com.jnshu.dto1.TransactionRO;

import java.util.List;

public interface TransactionService1 {
    List<TransactionListRO> getContinueInvList(long id)throws Exception;

    long addRenewal(long oldId,String userSign)throws Exception;

    List<TransactionListRO> getTransactionListRO(long userId,int status)throws Exception;

    TransactionRO getTransactionById(long id)throws Exception;
}
