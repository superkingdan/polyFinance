package com.jnshu.service1;


import com.github.pagehelper.Page;
import com.jnshu.dto1.StatisticsSalesListRO;
import com.jnshu.dto1.StatisticsSalesListRPO;
import com.jnshu.dto1.StatisticsSalesRPO;
import com.jnshu.entity.StatisticsSalesRO;

import java.util.List;

public interface StatisticsService {

    Page<StatisticsSalesListRO> getStatisticsSalesList(StatisticsSalesListRPO rpo);

    List<StatisticsSalesRO> getStatisticsSales(StatisticsSalesRPO rpo);

}
