package com.jnshu.controller;

import com.github.pagehelper.Page;
import com.jnshu.dto1.StatisticsSalesListRO;
import com.jnshu.dto1.StatisticsSalesListRPO;
import com.jnshu.entity.StatisticsSalesRO;
import com.jnshu.dto1.StatisticsSalesRPO;
import com.jnshu.service1.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计模块，销量统计使用相关接口
 * @author wangqichao
 */
@RestController
public class StatisticsController {
    private static final Logger log= LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    StatisticsService statisticsService;

    /**
     * 获得销量统计列表
     * @param rpo 查询条件
     * @return 返回参数，code,message,产品销量统计
     */
    @GetMapping(value = "/a/u/statistics/sales/list")
    public Map getStatisticsSalesList(@ModelAttribute StatisticsSalesListRPO rpo){
    log.info("获得销量统计列表,统计条件为"+rpo);
    Page<StatisticsSalesListRO> page=statisticsService.getStatisticsSalesList(rpo);
    Map<String,Object> map=new HashMap<>();
    map.put("code",0);
    map.put("message","success");
    map.put("total",page.getTotal());
    map.put("size",rpo.getSize());
    map.put("data",page);
    return map;
}

    /**
     * 获得指定产品的每日销量
     * @param id 产品id
     * @param rpo 查询条件
     * @return 查询结果,code,messqge,单日销量统计的集合
     */
    @GetMapping(value = "/a/u/statistics/sales/{id}")
    public Map getStatisticsSales(@PathVariable(value = "id")long id, @ModelAttribute StatisticsSalesRPO rpo){
        rpo.setId(id);
        log.info("按条件"+rpo+"查询产品销量");
    List<StatisticsSalesRO> ros=statisticsService.getStatisticsSales(rpo);
    Map<String,Object> map=new HashMap<>();
    map.put("code",0);
    map.put("message","success");
    map.put("total",ros.get(0).getTotal());
    map.put("size",rpo.getSize());
    map.put("data",ros);
    return map;
}

}
