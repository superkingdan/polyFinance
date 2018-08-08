package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask1Service {
    //   1为回本金，并记录银行流水和消息，标题：正在回款，，
//    然后创建新的定时任务2，将银行返回查询流水号和相关信息填入，时间设置为银行反应时间，默认三小时

    /*任务性质1 回本金*/
    void timedTask0(TimedTask timedTask);

}
