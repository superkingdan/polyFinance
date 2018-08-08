package com.jnshu.service1;

import com.jnshu.dto1.TransactionListRO;
import com.jnshu.dto1.TransactionRO;

import java.util.List;

public interface TransactionService1 {
    List<TransactionListRO> getContinueInvList(long id);

    int addRenewal(long oldId,String userSign);

    List<TransactionListRO> getTransactionListRO(long userId,int status);

    TransactionRO getTransactionById(long id);
}
