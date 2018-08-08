package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask7Service {
    /*任务6性质7
     * 新增债权时增加（或是修改债权后修改）一个定时任务，任务时间为债权到期时间，到期后将债权状态修改为已过期
     * */
    void timedTask(TimedTask timedTask);
}
