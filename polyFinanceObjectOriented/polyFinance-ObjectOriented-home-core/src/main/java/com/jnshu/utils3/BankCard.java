package com.jnshu.utils3;

import com.jnshu.dao3.BankCardMapper3;
import com.jnshu.dao3.BankMapper3;
import org.springframework.beans.factory.annotation.Autowired;

public class BankCard {
    @Autowired
    BankCardMapper3 bankCardMapper3;
    @Autowired
    BankMapper3 bankMapper3;


    public  String bankCard(long id){
        com.jnshu.entity.BankCard bankCard= bankCardMapper3.findById(id);
        String name = bankMapper3.findBankById(bankCard.getBankId()).getBankName();
        String cardId=bankCard.getBankCard().substring(bankCard.getBankCard().length()-4,bankCard.getBankCard().length());
        String defaultCard=name+"("+cardId+")";
        return defaultCard;
    }
}
