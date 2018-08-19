package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import com.jnshu.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTaskServiceImpl implements TimeTaskService{
    @Autowired
    TimeTask0Service timeTask0Service;
    @Autowired
    TimeTask1Service timeTask1Service;
    @Autowired
    TimeTask2Service timeTask2Service;
    @Autowired
    TimeTask3Service timeTask3Service;
    @Autowired
    TimeTask4Service timeTask4Service;
    @Autowired
    TimeTask5Service timeTask5Service;
    @Autowired
    TimeTask6Service timeTask6Service;
    @Autowired
    TimeTask7Service timeTask7Service;

    @Override
    public void task(TimedTask timedTask) throws Exception {
        System.out.println("id为"+timedTask.getId()+"的任务进入任务分发器");
        if (timedTask.getNature()==0){
            timeTask0Service.timedTask0(timedTask);
        }
        if (timedTask.getNature()==1){
            timeTask1Service.timedTask0(timedTask);
        }
        if (timedTask.getNature()==2){
            timeTask2Service.timedTask2(timedTask);
        }
        if (timedTask.getNature()==3){
            timeTask3Service.timedTask2(timedTask);
        }
        if (timedTask.getNature()==4){
            timeTask4Service.timedTask(timedTask);
        }
        if (timedTask.getNature()==5){
            timeTask5Service.timedTask(timedTask);
        }
        if (timedTask.getNature()==6){
            timeTask6Service.timedTask(timedTask);
        }
        if (timedTask.getNature()==7){
            timeTask7Service.timedTask(timedTask);
        }
    }
}
