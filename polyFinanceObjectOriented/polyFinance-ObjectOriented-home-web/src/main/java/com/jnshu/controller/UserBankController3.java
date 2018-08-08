package com.jnshu.controller;

import com.alibaba.fastjson.JSONException;
import com.jnshu.dto3.BankCardList;
import com.jnshu.service3.UserBankService3;
import com.jnshu.service3.UserDataService3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户银行卡管理
 * @return
 */
@Controller
@RequestMapping("/a/u")
public class UserBankController3 {
    @Autowired
    UserBankService3 userBankService3;
    @Autowired
    UserDataService3 userDataService3;

    private static final Logger log= LoggerFactory.getLogger(UserBankController3.class);
    /**
     * 用户银行卡页面 /完成
     * @return 银行卡列表
     */
    @GetMapping("/user/bank/{id}")
    @ResponseBody
    public Object userBank(@PathVariable(value="id") long id,
                           HttpServletRequest request) throws JSONException {
        return userBankService3.findBankCard(id);
    }
    /**
     * 添加银行卡页面 /完成
     * @return
     */
    @GetMapping("/userBank/{id}")
    @ResponseBody
    public Object userBankUp(@PathVariable(value="id") long id,
                             HttpServletRequest request) throws JSONException {

        return userDataService3.findUserById(id);
    }
    /**
     * 添加银行卡 /完成
     * @return
     */
    @PostMapping("/userBank/{id}")
    @ResponseBody
    public Object userBankUpdate(@PathVariable(value="id") long id,
                                 @ModelAttribute BankCardList bankCardList,
                                 HttpServletRequest request) {
        return userBankService3.addBankCard(bankCardList,id);
    }
    /**
     * 设置为默认银行卡 /完成
     * @return
     */
    @PostMapping("/defultCard/{id}")
    @ResponseBody
    public Object defultCardUpdate(@PathVariable(value="id") long id,
                                   @RequestParam(value = "cardId")long cardId,
                                   HttpServletRequest request)  {

        return userBankService3.defaultCardUpdata(id,cardId);
    }

}
