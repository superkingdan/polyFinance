package com.jnshu.Service;

import com.jnshu.dao3.ContractMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.entity.Contract;
import com.jnshu.entity.TimedTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTask6ServiceImpl implements TimeTask6Service{
    @Autowired
    ContractMapper3 contractMapper3;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;
    @Override
    public void timedTask(TimedTask timedTask) {
        /*获取合同*/
        Contract contract= contractMapper3.findById(timedTask.getContractId());
        /*修改合同表状态为未匹配*/
        contract.setIsMatchingClaims(0);
        /*修改任务1状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);

    }
}
