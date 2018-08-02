package com.jnshu.service1;

import com.github.pagehelper.Page;
import com.jnshu.dto1.TransactionListBackRO;
import com.jnshu.dto1.TransactionListRPO;
import com.jnshu.dto1.TransactionLogRPO;
import com.jnshu.entity.ClaimsProtocolCodeRO;
import com.jnshu.entity.ContractRO;
import com.jnshu.entity.TransactionLog;

import java.util.List;

public interface UserTransactionService {
    Page<TransactionLog> getTransactionLogList(TransactionLogRPO rpo);

    Page<TransactionListBackRO> getTransactionList(TransactionListRPO rpo);

    ContractRO getContract(String contractCode);

    ClaimsProtocolCodeRO getClaimsProtocolCode(String claimsProtocolCode);
}
