package com.jnshu.Service;

import com.jnshu.dao3.MessageMapper3;
import com.jnshu.dao3.TimedTaskMapper3;
import com.jnshu.entity.Message;
import com.jnshu.entity.TimedTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTask5ServiceImpl implements TimeTask5Service {
    @Autowired
    MessageMapper3 messageMapper3;
    @Autowired
    TimedTaskMapper3 timedTaskMapper3;

    @Override
    public void timedTask(TimedTask timedTask) {
        /*修改状态为已发送*/
        Message message = messageMapper3.findById(timedTask.getMessageId());
        message.setIsSent(0);
        messageMapper3.updateMessage(message);
        /*修改任务1状态*/
        timedTask.setStatus(1);
        timedTaskMapper3.updateTask(timedTask);
    }
}
