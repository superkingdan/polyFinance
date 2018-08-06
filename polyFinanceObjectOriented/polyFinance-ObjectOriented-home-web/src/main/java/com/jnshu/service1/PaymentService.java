package com.jnshu.service1;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.entity.User;

import java.util.List;

public interface PaymentService {

    List<BankCardRO> getInvestment(long id);

    Long addContract(String userSign,long productId,long userId);

    Long addPayTransactionLog(PaymentRPO rpo);

    User getUserInfo(long userId);

    BankCard getBankCard(long bankCardId);
}
