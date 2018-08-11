package com.jnshu.service1;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PaymentService1 {

    List<BankCardRO> getInvestment(long id);

    Long addContract(String userSign,long productId,long userId)throws Exception;

    Long addPayTransactionLog(PaymentRPO rpo);

    User getUserInfo(long userId);

    BankCard getBankCard(long bankCardId);

    Long updateTransactionLog(long transactionLogId);

    String updateContract(long contractId);

    Long addTransaction(long transactionLogId,String contractCode);

    Long getTransactionIdByContractId(long contractId);

//    Boolean judge(long id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)throws Exception;
}
