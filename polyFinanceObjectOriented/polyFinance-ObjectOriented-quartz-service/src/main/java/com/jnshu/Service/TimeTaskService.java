package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import com.jnshu.exception.MyException;
import org.springframework.stereotype.Component;

@Component
public interface TimeTaskService {
    /*任务分发*/
    void task(TimedTask timedTask) throws Exception;

}
