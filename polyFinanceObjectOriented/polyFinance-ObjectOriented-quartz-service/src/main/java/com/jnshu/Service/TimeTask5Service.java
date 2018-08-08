package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask5Service {
    /*任务5性质5 定时消息改为已发送*/
    void timedTask(TimedTask timedTask);
}
