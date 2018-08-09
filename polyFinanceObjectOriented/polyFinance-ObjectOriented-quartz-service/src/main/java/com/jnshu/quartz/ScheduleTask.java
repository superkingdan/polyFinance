package com.jnshu.quartz;

import com.jnshu.Service.TimeTask7Service;
import com.jnshu.dao3.ClaimsMapper3;
import com.jnshu.dao3.ContractMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.dao3.TransactionMapper3;
import com.jnshu.entity.Claims;
import com.jnshu.entity.TimedTask;
import com.jnshu.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Component
@EnableScheduling // 该注解必须要加 开启对定时任务的支持
//@Scheduled声明需要执行的定时任务
public class ScheduleTask {
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;
    @Autowired
    TimeTask7Service timeTask7Service;
    @Autowired
    ContractMapper3 contractMapper3;
    @Autowired
    ClaimsMapper3 claimsMapper3;
    @Autowired
    TransactionMapper3 transactionMapper3;

    public void scheduleTest() throws Exception{

        System.err.println("scheduleTest开始定时执行"+System.currentTimeMillis());
        /*获取状态为0的任务组  不包括性质为6*/
        List<TimedTask> timedTasks=timedTaskMapper.findTaskByStatus();
        if (timedTasks.size()>0){
            System.out.println("timedTasks不为0");
            BigDecimal num = new BigDecimal(System.currentTimeMillis());
            BigDecimal num1 = new BigDecimal(3600000);
            BigDecimal result = num.add(num1);
            System.out.println("111=="+timedTasks.get(0)+"`"+String.valueOf(result));
            /*进入任务分发器*/

            for (int i=0;timedTasks.get(i).getTaskTime()<Long.valueOf(String.valueOf(result))&&
                    timedTasks.get(i).getTaskTime()>Long.valueOf(String.valueOf(num));i++){
                System.out.println("进入任务分发器");
                timeTask7Service.timedTask(timedTasks.get(i));

            }}

        /*任务性质为6的任务*/
        List<TimedTask> timedTasks1=timedTaskMapper.findTaskByNature(6);
        System.out.println(timedTasks1);
        if (timedTasks1.size()>0){
            for (int u=0;timedTasks1.size()==u;u++) {
                /*获取对应交易表组*/
                List<Transaction> transactions=new ArrayList<>();
                transactions.add(transactionMapper.findTransaction(timedTasks1.get(u).getTransactionId()));
                /*获取对应债权表组*/
                List<Claims> claims=new ArrayList<>();
                claims.add(claimsMapper.findClaimsById(timedTasks1.get(u).getClaimsId()));
                /*对比时间*/
                if (claims.get(u).getLendEndAt()<=transactions.get(u).getEndAt()){
                    timeTask7Service.timedTask(timedTasks1.get(u));
                }
            }}





    }
}