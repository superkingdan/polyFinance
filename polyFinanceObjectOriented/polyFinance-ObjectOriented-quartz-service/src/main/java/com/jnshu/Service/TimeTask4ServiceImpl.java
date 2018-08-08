package com.jnshu.Service;

import com.jnshu.dao3.*;
import com.jnshu.entity.Message;
import com.jnshu.entity.Product;
import com.jnshu.entity.TimedTask;
import com.jnshu.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTask4ServiceImpl implements TimeTask4Service {
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


    @Override
    public void timedTask(TimedTask timedTask) {
        /*续投*/
        /*获取交易单*/
        Transaction transaction= transactionMapper3.findTransaction(timedTask.getTransactionId());
        /*获取产品名*/
        Product product= productMapper3.findByTransactionId(timedTask.getTransactionId());
        /*添加消息表*/
        Message message=new Message();
        message.setCreateAt(System.currentTimeMillis());
        message.setUserId(transaction.getUserId());
        message.setTitle("即将到期");
        message.setTransactionId(transaction.getId());
        message.setIntroduce(product.getProductName()+"即将到期");
        messageMapper3.addMessage(message);
        /*修改状态为可续投*/
        transaction.setRenuwalStatus(1);
        transactionMapper3.updateRenuwalStatus(transaction);
        /*修改任务1状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);
    }
}
