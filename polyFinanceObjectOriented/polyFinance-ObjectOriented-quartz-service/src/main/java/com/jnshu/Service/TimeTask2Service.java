package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask2Service {
    /*任务2性质0 回息是否成功*/
    void timedTask2(TimedTask timedTask);
}
