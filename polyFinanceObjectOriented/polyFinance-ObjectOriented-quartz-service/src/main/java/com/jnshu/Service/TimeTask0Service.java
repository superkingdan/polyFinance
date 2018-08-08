package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask0Service {
//    0为回息，并记录银行返回流水和消息，标题：正在返息，
//    然后创建新的定时任务2，将银行返回查询流水号和相关信息填入，时间设置为银行反应时间，默认三小时

    /*任务性质0 回息*/
    void timedTask0(TimedTask timedTask);



}
