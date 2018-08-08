package com.jnshu.Service;

import com.jnshu.dao3.ClaimsMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.entity.Claims;
import com.jnshu.entity.TimedTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTask7ServiceImpl implements TimeTask7Service {
    @Autowired
    ClaimsMapper3 claimsMapper3;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;

    @Override
    public void timedTask(TimedTask timedTask) {
        /*获取债权信息*/
        Claims claims= claimsMapper3.findClaimsById(timedTask.getClaimsId());
        claims.setStatus(2);
        claimsMapper3.updateStatus(claims);

        /*修改任务7状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);

    }
}
