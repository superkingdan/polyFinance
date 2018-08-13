package com.jnshu.service1;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PaymentService1 {

    List<BankCardRO> getInvestment(long id)throws Exception;

    Long addContract(String userSign,long productId,long userId)throws Exception;

    Long addPayTransactionLog(PaymentRPO rpo)throws Exception;

    User getUserInfo(long userId)throws Exception;

    BankCard getBankCard(long bankCardId)throws Exception;

    Long updateTransactionLog(long transactionLogId,String bankLog)throws Exception;

    String updateContract(long contractId)throws Exception;

    Long addTransaction(long transactionLogId,String contractCode)throws Exception;

    Long getTransactionIdByContractId(long contractId)throws  Exception;

//    Boolean judge(long id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)throws Exception;
}
