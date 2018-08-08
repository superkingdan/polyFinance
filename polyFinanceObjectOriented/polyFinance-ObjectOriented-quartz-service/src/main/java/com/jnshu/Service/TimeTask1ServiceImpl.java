package com.jnshu.Service;

import com.jnshu.dao3.*;
import com.jnshu.entity.Message;
import com.jnshu.entity.Product;
import com.jnshu.entity.TimedTask;
import com.jnshu.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTask1ServiceImpl   implements TimeTask1Service{
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
    public void timedTask0(TimedTask timedTask) {
        /*获取交易单*/
        Transaction transaction= transactionMapper3.findTransaction(timedTask.getTransactionId());
        /*获取产品名*/
        Product product= productMapper3.findByTransactionId(timedTask.getTransactionId());
        /*添加消息表*/
        Message message=new Message();
        message.setCreateAt(System.currentTimeMillis());
        message.setUserId(transaction.getUserId());
        message.setTitle("正在回款");
        message.setTransactionId(transaction.getId());
        message.setIntroduce(product.getProductName()+"正在回款");
        messageMapper3.addMessage(message);
        /*修改交易状态*/
        transaction.setStatus(1);
        transactionMapper3.updateStatus(transaction);
        /*修改任务1状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);
        /*添加回款任务3
         * 任务3，将银行返回查询流水号和相关信息填入，时间设置为银行反应时间，默认三小时
         * */
        String bankLog="111111111";
        Long taskTime = System.currentTimeMillis()+10800000;  //银行反应时间
        TimedTask timedTask1=new TimedTask();
        timedTask1.setStatus(0);
        timedTask1.setBankLog(bankLog);
        timedTask1.setCreateAt(System.currentTimeMillis());
        timedTask1.setTransactionId(transaction.getId());
        timedTask1.setNature(3);
        timedTask1.setSuperId(timedTask.getSuperId());
        timedTask1.setTaskTime(taskTime);
        timedTask1.setMoney(timedTask.getMoney());
        timedTaskMapper3.addTask(timedTask1);
    }


}
