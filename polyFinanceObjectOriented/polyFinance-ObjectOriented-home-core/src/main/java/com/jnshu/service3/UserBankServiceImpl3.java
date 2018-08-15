package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.BankCardMapper3;
import com.jnshu.dao3.BankMapper3;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.dto3.BankCardList;
import com.jnshu.entity.Bank;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBankServiceImpl3 implements UserBankService3 {
    @Autowired
    UserMapper3 userMapper3;
    @Autowired
    BankCardMapper3 bankCardMapper3;
    @Autowired
    BankMapper3 bankMapper3;

    /*默认银行卡拼接*/
    @Override
    public String defaultCard(long id) {
        BankCard bankCard=bankCardMapper3.findById(id);
        String name =bankMapper3.findBankById(bankCard.getBankId()).getBankName();
        String cardId=bankCard.getBankCard().substring(bankCard.getBankCard().length()-4,bankCard.getBankCard().length());
        String defaultCard=name+"("+cardId+")";
        return defaultCard;
    }
    /*获取银行卡*/ //改
    @Override
    public JSONObject findBankCard(long id) {
        JSONObject json =new JSONObject();
        if (userMapper3.findUserById(id).getRealName()==null){
            json.put("code",1000);
            json.put("message","未实名");
            return json;
        }
        List<BankCardList> bankCardList =bankCardMapper3.findListByUser(id);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",bankCardList);
        return json;
    }

    /*默认银行卡*/ //未push
    @Override
    public JSONObject defaultCardUpdata(long id, long cardId) {
        JSONObject json =new JSONObject();
        BankCard bankCard =bankCardMapper3.findById(cardId);

        if (bankCard==null){
            json.put("code",-1);
            json.put("message","没有银行卡");
            return json;
        }
        System.out.println(bankCard);
//        if (bankCard.getCardOrder()==2){
//            BankCard bankCard1=bankCardMapper3.findBankCardByOrder(id);
//            System.out.println(bankCard1);
//            bankCard.setCardOrder(1);
//            bankCard1.setCardOrder(2);
//            bankCardMapper3.updateData(bankCard);
//            bankCardMapper3.updateData(bankCard1);
//        }
        User user=userMapper3.findUserById(id);
        user.setDefaultCard(cardId);
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
    //添加 8/14修改 未push
    @Override
    public JSONObject addBankCard(BankCardList bankCardList, long id) throws MyException {
        JSONObject json =new JSONObject();

        BankCard bankCard1 =bankCardMapper3.findBankCardByIdCard(bankCardList.getBankCard());
        if (bankCard1!=null){
            throw new MyException(-1,"该银行卡已绑定");
        }
        if (bankCardMapper3.findCountByUser(id)>=2){
            throw new MyException(-1,"绑定银行卡超过两张无法添加");
        }

        BankCard bankCard =new BankCard();
        bankCard.setCreateAt(System.currentTimeMillis());
        bankCard.setCreateBy(id);
        bankCard.setUserId(id);
        bankCard.setBankCard(bankCardList.getBankCard());
        bankCard.setCity(bankCardList.getCity());
        bankCard.setBankPhone(bankCardList.getBankPhone());

        Bank bank=bankMapper3.findBankByName(bankCardList.getBankName());
        if (bank==null){
            throw new MyException(-1,"该银行不存在");
        }
        bankCard.setBankId(bank.getId());
        if (bankCardMapper3.findCountByUser(id)==0){
            bankCard.setCardOrder(1);
        }
        if (bankCardMapper3.findCountByUser(id)==1){
            bankCard.setCardOrder(2);
        }
        bankCardMapper3.addBankCard(bankCard);

        User user=userMapper3.findUserById(id);
        if (user.getDefaultCard()==0){
            user.setDefaultCard(bankCard.getId());
            userMapper3.updateData(user);
        }
        json.put("code",0);
        json.put("message","添加成功");
        return json;
    }

    @Override
    public JSONObject findBank() {
        JSONObject json=new JSONObject();
        List<Bank> banks=bankMapper3.findBank();
        if (banks==null){
            json.put("code",-1);
            json.put("message","没有数据");
        }
        json.put("code",0);
        json.put("message","成功");
        json.put("data",banks);
        return json;
    }



}
