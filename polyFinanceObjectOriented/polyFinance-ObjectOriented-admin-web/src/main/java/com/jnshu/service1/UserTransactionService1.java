package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.TransactionListBackRO;
import com.jnshu.dto1.TransactionListRPO;
import com.jnshu.dto1.TransactionLogRPO;
import com.jnshu.entity.ClaimsProtocolCodeRO;
import com.jnshu.dto1.ContractRO;
import com.jnshu.entity.TransactionLog;
import org.springframework.stereotype.Component;

@Component(value = "userTransactionService1")
public interface UserTransactionService1 {
    Page<TransactionLog> getTransactionLogList(TransactionLogRPO rpo);

    Page<TransactionListBackRO> getTransactionList(TransactionListRPO rpo);

    ContractRO getContract(String contractCode)throws Exception;

    ClaimsProtocolCodeRO getClaimsProtocolCode(String claimsProtocolCode);
}
