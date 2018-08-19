package com.jnshu.Service;

import com.jnshu.dao3.*;
import com.jnshu.entity.*;
import com.jnshu.exception.MyException;
import com.jnshu.service3.UserBankService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TimeTask2ServiceImpl  implements TimeTask2Service{
    @Autowired
    ProductMapper3 productMapper3;
    @Autowired
    TransactionMapper3 transactionMapper3;
    @Autowired
    MessageMapper3 messageMapper3;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;
    @Autowired
    TransactionLogMapper3 transactionLogMapper3;
    @Autowired
    BankCardMapper3 bankCardMapper3;
    @Autowired
    UserMapper3 userMapper3;
    @Autowired
    UserBankService3 userBankService3;


    @Override
    public void timedTask2(TimedTask timedTask) throws MyException {


        /*定时任务0 子定时任务2*/
        timedTask.getBankLog();
        /*获取交易单*/
        Transaction transaction= transactionMapper3.findTransaction(timedTask.getTransactionId());
        /*获取产品名*/
        Product product= productMapper3.findByTransactionId(timedTask.getTransactionId());
        /*查询是否成功*/
        /*预备位*/
        Long time = null;//银行交易时间
        String bankCard;
        Long id=transaction.getUserId();
        User user= userMapper3.findUserById(id);
        bankCard= userBankService3.defaultCard(user.getDefaultCard());

        TransactionLog transactionLog=new TransactionLog();
        transactionLog.setBankLog(timedTask.getBankLog());
        transactionLog.setCreateAt(System.currentTimeMillis());
        transactionLog.setProductName(product.getProductName());
        transactionLog.setTransactionAt(time);
        transactionLog.setTransactionWay(bankCard);
        transactionLog.setMoney(timedTask.getMoney());

        /*添加消息表*/
        Message message=new Message();
        message.setCreateAt(System.currentTimeMillis());
        message.setUserId(transaction.getUserId());

        message.setTransactionId(transaction.getId());




        if ("成功"=="成功"){
            /*添加任务流水 回息成功*/
            transactionLog.setStatus(4);
            transactionLogMapper3.addTransactionLog(transactionLog);
            /*添加消息表*/
            message.setTitle("返息成功");
            message.setIntroduce(product.getProductName()+"返息成功");
            messageMapper3.addMessage(message);
            /*修改交易*/
            BigDecimal num = new BigDecimal(transaction.getReturned());
            BigDecimal num1 = new BigDecimal(transaction.getNotReturn());
            BigDecimal num2 = new BigDecimal(timedTask.getMoney());
            BigDecimal result = num.add(num2);
            transaction.setReturned(String.valueOf(result));
            BigDecimal result1 = num1.subtract(num2);
            transaction.setNotReturn(String.valueOf(result1));
            transactionMapper3.updateStatus(transaction);
            /*修改用户收益*/
            BigDecimal num3 = new BigDecimal(user.getCumulativeIncome());
            BigDecimal result2 = num3.add(num2);
            user.setCumulativeIncome(user.getCumulativeIncome()+timedTask.getMoney());
            userMapper3.updateData(user);
        }else {
            /*根据父id获取定时任务*/
            TimedTask timedTask1= timedTaskMapper3.findTaskById(timedTask.getSuperId());
            /*修改任务1状态*/
            timedTask1.setStatus(0);
            /*增加三天*/
            timedTask1.setTaskTime(timedTask1.getTaskTime()+259200000);
            timedTaskMapper3.updateTask(timedTask1);
            /*添加消息表*/
            message.setTitle("返息失败");
            message.setIntroduce(product.getProductName()+"返息失败");
            messageMapper3.addMessage(message);
            /*添加任务流水 回息失败*/
            transactionLog.setStatus(5);
            transactionLogMapper3.addTransactionLog(transactionLog);
        }
        /*修改任务1状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);


    }
}
