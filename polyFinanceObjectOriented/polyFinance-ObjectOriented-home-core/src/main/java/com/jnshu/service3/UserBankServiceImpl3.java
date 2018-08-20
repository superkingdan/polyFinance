package com.jnshu.service3;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.BankCardMapper3;
import com.jnshu.dao3.BankMapper3;
import com.jnshu.dao3.RealNameApplicationMapper3;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.dto3.BankCardList;
import com.jnshu.entity.Bank;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.RealNameApplication;
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
    @Autowired
    RealNameApplicationMapper3 realNameApplicationMapper3;

    /*默认银行卡拼接*/
    @Override
    public String defaultCard(long id) throws MyException {
        BankCard bankCard=bankCardMapper3.findById(id);
        if (bankCard==null) {
            throw new MyException(-1, "没有默认银行卡");
        }
        Bank bank=bankMapper3.findBankById(bankCard.getBankId());
        if (bank==null){
            throw new MyException(-1, "该银行不存在");
        }
        String name =bank.getBankName();
        String cardId=bankCard.getBankCard().substring(bankCard.getBankCard().length()-4,bankCard.getBankCard().length());
        String defaultCard=name+"("+cardId+")";
        return defaultCard;
    }
    /*获取银行卡*/
    @Override
    public JSONObject findBankCard(long id) throws MyException {
        JSONObject json =new JSONObject();
        User user=userMapper3.findUserById(id);
        if (user==null){
            throw new MyException(-1,"该用户不存在");
        }
        if (user.getRealStatus()==0) {
            RealNameApplication realNameApplication = realNameApplicationMapper3.findByUserId(id);
            if (realNameApplication == null) {
                throw new MyException(1000, "未实名");
            }
            if (realNameApplication.getApplicationStatus() == 0) {
                throw new MyException(1002, "请耐心等待实名通过");
            }
            throw new MyException(1000, "未实名");
        }
        List<BankCardList> bankCardList =bankCardMapper3.findListByUser(id);
        json.put("code",0);
        json.put("message","成功");
        json.put("data",bankCardList);
        return json;
    }

    /*默认银行卡*/
    @Override
    public JSONObject defaultCardUpdata(long id, long cardId) {
        JSONObject json =new JSONObject();
        BankCard bankCard =bankCardMapper3.findById(cardId);

        if (bankCard==null){
            json.put("code",-1);
            json.put("message","没有银行卡");
            return json;
        }
        User user=userMapper3.findUserById(id);
        user.setDefaultCard(cardId);
        userMapper3.updateData(user);
        json.put("code",0);
        json.put("message","成功");
        return json;
    }
    //添加
    @Override
    public JSONObject addBankCard(BankCardList bankCardList, long id) throws MyException {
        JSONObject json =new JSONObject();

        BankCard bankCard1 =bankCardMapper3.findBankCardByIdCard(bankCardList.getBankCard());
        if (bankCardMapper3.findCountByUser(id)>=2){
            throw new MyException(-1,"绑定银行卡超过两张无法添加");
        }
        if (bankCard1!=null){
            throw new MyException(-1,"该银行卡已绑定");
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
            if (bankCardMapper3.findBankCardBySingle(id).getCardOrder()==1){
                bankCard.setCardOrder(2);
            }
            if (bankCardMapper3.findBankCardBySingle(id).getCardOrder()==2){
                bankCard.setCardOrder(1);
            }
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
