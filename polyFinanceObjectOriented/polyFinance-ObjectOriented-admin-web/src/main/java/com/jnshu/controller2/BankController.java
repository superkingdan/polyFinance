package com.jnshu.controller2;

import com.jnshu.model.bankandbandcard.Bank;
import com.jnshu.model.bankandbandcard.BankInfo;
import com.jnshu.utils.CAM;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BankController {

    private CAM cam = new CAM();
    //银行列表
    @RequestMapping(value = "/a/u/banks",method = RequestMethod.GET)
    public List<Object> getBanks(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String bankName,
                                 @RequestParam(required = false) String loginName,
                                 @RequestParam(required = false) Integer updateAt1,
                                 @RequestParam(required = false) Integer updateAt2,
                                 @RequestParam(required = false) String singleLimited1,
                                 @RequestParam(required = false) String singleLimited2,
                                 @RequestParam(required = false) String dayLimited1,
                                 @RequestParam(required = false) String dayLimited2){
        List<Object> result = new ArrayList<>();
        List<Bank> banks = new ArrayList<>();

        for (int i=1;i<pageSize;i++){
            Bank bank = new Bank();
            bank.setId((long) i);
            bank.setBankName("中国银行");
            bank.setPaymentId("73453945398"+String.valueOf(pageSize));
            bank.setWithdrawalld("IEU32985749");
            bank.setSingleLimited("50000.0");
            bank.setDayLimited("600000");
            bank.setLoginName("police");
            bank.setUpdateBy(System.currentTimeMillis());
            banks.add(bank);
        }

        result.add(cam);
        Map<String,Integer> s = new HashMap<>();
        s.put("total", 100);
        result.add(banks);
        result.add("银行列表  接收到的参数：pageNum="
                +String.valueOf(pageNum)+",pageSize="
                +String.valueOf(pageSize)
                +",bankName="+bankName+",loginName="
                +loginName+",updateAt1="+updateAt1
                +",updateAt2="+updateAt2+",singleLimited1="
                +singleLimited1+"singLimited2="
                +singleLimited2+",dayLimited1="
                +dayLimited1+",dayLimited2="+dayLimited2);
        return result;
    }

    //银行详情
    @RequestMapping(value = "/a/u/banks/{id}",method = RequestMethod.GET)
    public List<Object> getBnkInfo(@PathVariable long id){
        List<Object> result = new ArrayList<>();

        BankInfo bankInfo = new BankInfo();
        bankInfo.setId(id);
        bankInfo.setBankName("农业银行");
        bankInfo.setPaymentId("YOI7985435904");
        bankInfo.setWithdrawalId("IEW59435920374");
        bankInfo.setSingleLimited("70000");
        bankInfo.setDayLimited("800000");
        bankInfo.setIcon("werw.");
        result.add(cam);
        result.add(bankInfo);
        return result;
    }

    //银行信息更新
    @RequestMapping(value = "/a/u/banks/{id}",method = RequestMethod.PUT)
    public List<Object> updateBnkInfo(@PathVariable long id,
                                      @RequestParam(required = false) String bankName,
                                      @RequestParam(required = false) String paymentId,
                                      @RequestParam(required = false) String withdrawalld,
                                      @RequestParam(required = false) String singleLimited,
                                      @RequestParam(required = false) String dayLimited,
                                      @RequestParam(required = false) String icon){
        List<Object> result = new ArrayList<>();
        result.add(cam);
        result.add("更新银行记录  接收到的参数：id="+id+
                "bankName="+bankName +
                ",paymenId="+paymentId +
                ",withdrawalld="+withdrawalld +
                ",singleLimited="+singleLimited +
                ", dayLimited="+ dayLimited +
                ", icon="+icon);
        return result;
    }

    //银行信息新增
    @RequestMapping(value = "/a/u/banks/new",method = RequestMethod.POST)
    public List<Object> saveBnkInfo( @RequestParam(required = false) String bankName,
                                     @RequestParam(required = false) String paymentId,
                                     @RequestParam(required = false) String withdrawalld,
                                     @RequestParam(required = false) String singleLimited,
                                     @RequestParam(required = false) String dayLimited,
                                     @RequestParam(required = false) String icon){
        List<Object> result = new ArrayList<>();

        BankInfo bankInfo = new BankInfo();
        bankInfo.setBankName(bankName);
        bankInfo.setPaymentId(paymentId);
        bankInfo.setWithdrawalId(withdrawalld);
        bankInfo.setSingleLimited(singleLimited);
        bankInfo.setDayLimited(dayLimited);
        bankInfo.setIcon(icon);
        result.add(cam);
        result.add("新增银行记录  接收到的参数：bank="+
                bankInfo);
        return result;
    }
}
