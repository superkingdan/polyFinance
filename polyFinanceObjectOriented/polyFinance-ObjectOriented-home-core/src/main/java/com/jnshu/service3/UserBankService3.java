package com.jnshu.service3;


import com.alibaba.fastjson.JSONObject;
import com.jnshu.dto3.BankCardList;
import org.springframework.stereotype.Component;

@Component(value = "userBankService3")
public interface UserBankService3 {
    /*默认银行卡 根据银行卡id*/
    String defaultCard(long id);
    /*获取银行卡-整合后*/
    JSONObject findBankCard(long id);
    /*设置默认银行卡*/
    JSONObject defaultCardUpdata(long id, long cardId);
    /*添加银行卡*/
    JSONObject addBankCard(BankCardList bankCardList, long id);
    /*获取银行*/
    JSONObject findBank();

}
