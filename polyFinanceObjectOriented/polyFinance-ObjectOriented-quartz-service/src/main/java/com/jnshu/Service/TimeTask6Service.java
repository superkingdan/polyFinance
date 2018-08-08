package com.jnshu.Service;

import com.jnshu.entity.TimedTask;
import org.springframework.stereotype.Component;

@Component
public interface TimeTask6Service {
    /*任务6性质6
    * 债权匹配时，合同时间>债权时间的情况，修改合同表的状态为未匹配，此时此合同会重新出现在待匹配列表
    * 合同时间为交易单子结束时间 大雨 债权时间
    * */
    void timedTask(TimedTask timedTask);
}
