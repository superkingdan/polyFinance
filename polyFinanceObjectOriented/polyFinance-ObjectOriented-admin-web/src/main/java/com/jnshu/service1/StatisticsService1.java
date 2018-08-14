package com.jnshu.service1;


import com.github.pagehelper.Page;
import com.jnshu.dto1.StatisticsSalesListRO;
import com.jnshu.dto1.StatisticsSalesListRPO;
import com.jnshu.dto1.StatisticsSalesRPO;
import com.jnshu.entity.StatisticsSalesRO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "statisticsService1")
public interface StatisticsService1 {

    Page<StatisticsSalesListRO> getStatisticsSalesList(StatisticsSalesListRPO rpo)throws Exception;

    List<StatisticsSalesRO> getStatisticsSales(StatisticsSalesRPO rpo)throws Exception;

}
