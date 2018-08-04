package com.jnshu.service1;

import com.jnshu.dto1.BankCardRO;

import java.util.List;

public interface PaymentService {

    List<BankCardRO> getInvestment(long id);

    Long addContract(String userSign,long productId,long userId);
}
