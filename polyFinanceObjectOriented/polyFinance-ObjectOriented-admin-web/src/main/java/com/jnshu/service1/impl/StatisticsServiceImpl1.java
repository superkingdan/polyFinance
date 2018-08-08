package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.ProductMapper1;
import com.jnshu.dao.TransactionMapper1;
import com.jnshu.dto1.StatisticsSalesListRO;
import com.jnshu.dto1.StatisticsSalesListRPO;
import com.jnshu.dto1.StatisticsSalesRPO;
import com.jnshu.entity.StatisticsSalesRO;
import com.jnshu.service1.StatisticsService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 销量统计模块service
 * @author wangqichao
 */
@Service
@Transactional
public class StatisticsServiceImpl1 implements StatisticsService1 {
    @Autowired
    ProductMapper1 productMapper1;
    @Autowired
    TransactionMapper1 transactionMapper1;

    private static final Logger log= LoggerFactory.getLogger(StatisticsServiceImpl1.class);
    /**
     * 获得产品销量统计列表
     * @param rpo 查询条件
     * @return 查询结果
     */
    @Override
    public Page<StatisticsSalesListRO> getStatisticsSalesList(StatisticsSalesListRPO rpo) {
        //首先得到分页的产品id
        Page<StatisticsSalesListRO> page= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        productMapper1.getProductIdByProductNameAndCode(rpo);
        //根据id去循环设置查询到的其他三项属性
        for (StatisticsSalesListRO aPage : page) {
            aPage.setPeopleCounting(transactionMapper1.getPeopleCountingByProductId(aPage));
            aPage.setNumberOfTimes(transactionMapper1.getNumberOfTimesByProductId(aPage));
            aPage.setTotalMoney(transactionMapper1.getSumMoneyByProductId(aPage));
            System.out.println(aPage);
        }
        return page;
    }

    /**
     * 获得产品每天销量明细
     * @param rpo 查询条件
     * @return 查询结果
     */
    @Override
    public List<StatisticsSalesRO> getStatisticsSales(StatisticsSalesRPO rpo) {
        //创建接收对象
        List<StatisticsSalesRO> ros=new ArrayList<>();
        //获得产品创建时间
        long productCreateAt= productMapper1.getProductCreateAtById(rpo.getId());
        //将尾巴去掉
        long startAt=(productCreateAt/(24*3600*1000))*24*3600*1000;
        //获得当前时间
        long currentTime=System.currentTimeMillis();
        //将尾巴去掉
        long endAt=(currentTime/(24*3600*1000))*24*3600*1000;
        //如果时间期限不在产品创建时间到当前时间范围内，直接返回结果
        if((rpo.getDateMin()!=null&&rpo.getDateMin()>endAt)||(rpo.getDateMax()!=null&&rpo.getDateMax()<startAt)){
            StatisticsSalesRO ro=new StatisticsSalesRO();
            ro.setTotal(0);
            ros.add(ro);
            return ros;
        }
        //如果时间下限为null，或者下限小于产品创建时间就用产品创建时间代替
        if (rpo.getDateMin()==null||rpo.getDateMin()<startAt){
           rpo.setDateMin(startAt);
        }
        //如果时间上限为null，或者上限大于当前时间，就用当前时间代替
        if(rpo.getDateMax()==null||rpo.getDateMax()>endAt){
            rpo.setDateMax(endAt);
        }
        //计算总数据数
        int total=(int)(rpo.getDateMax()-rpo.getDateMin())/(24*3600*1000);
        int size=rpo.getSize();
        int page=rpo.getPage();
        //计算总页数
        int totalPage;
        if(total%size!=0){
            totalPage=(total/size)+1;
        }
        else{
            totalPage=total/size;
        }
        //根据当前页和总页数计算开始的时间
        //第一页的第一条一定是最大时间那一天,所以开始的那一条的时间就是最大时间-开始的条数
        //如果是第一页，那么开始时间就是最大时间，然后依次递减一天，如果是第二页，就应该是dateMax-size*1*24*3600*1000
            int startDate = (page - 1) * size;
            //设置当前时间和下一天时间
            rpo.setCurrentDate(rpo.getDateMax() - startDate * 24 * 3600 * 1000);
            rpo.setNextDate(rpo.getCurrentDate() - (startDate - 1) * 24 * 3600 * 1000);
        //当超过一页时，只查size条
        if(totalPage>1) {
            for(int i=0;i<size;i++){
                //创建新的ro对象并且对其赋值
                StatisticsSalesRO ro=new StatisticsSalesRO();
                ro.setPeopleCounting(transactionMapper1.getPeopleCountingByProductIdAndDate(rpo));
                ro.setNumberOfTimes(transactionMapper1.getNumberOfTimesByProductIdAndDate(rpo));
                ro.setTotalMoney(transactionMapper1.getSumMoneyByProductIdAndDate(rpo));
                ro.setDate(rpo.getCurrentDate());
                ro.setTotal(total);
                //将ro对象加到List中去
                ros.add(ro);
                //将当前的时间往前推一天，进行新一轮查找
                rpo.setNextDate(rpo.getCurrentDate());
                rpo.setCurrentDate(rpo.getNextDate()-24*3600*1000);
            }
        }
        //当只有一页时,直接全查出来
        else{
            for(int i=0;i<total;i++){
                //创建新的ro对象并且对其赋值
                StatisticsSalesRO ro=new StatisticsSalesRO();
                ro.setTotal(total);
                ro.setPeopleCounting(transactionMapper1.getPeopleCountingByProductIdAndDate(rpo));
                ro.setNumberOfTimes(transactionMapper1.getNumberOfTimesByProductIdAndDate(rpo));
                ro.setTotalMoney(transactionMapper1.getSumMoneyByProductIdAndDate(rpo));
                ro.setDate(rpo.getCurrentDate());
                //将ro对象加到List中去
                ros.add(ro);
                //将当前的时间往前推一天，进行新一轮查找
                rpo.setNextDate(rpo.getCurrentDate());
                rpo.setCurrentDate(rpo.getNextDate()-24*3600*1000);
            }
        }
        return ros;
    }
}
