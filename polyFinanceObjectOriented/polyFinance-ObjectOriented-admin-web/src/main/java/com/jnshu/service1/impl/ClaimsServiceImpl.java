package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.ClaimsMapper;
import com.jnshu.dao.SystemDataMapper;
import com.jnshu.dao.TimedTaskMapper;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.entity.Claims;
import com.jnshu.entity.TimedTask;
import com.jnshu.service1.ClaimsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 债权相关service
 * @author wangqichao
 */
@Service
//开启事务
@Transactional
public class ClaimsServiceImpl implements ClaimsService{
    @Autowired
    ClaimsMapper claimsMapper;
    @Autowired
    TimedTaskMapper timedTaskMapper;
    @Autowired
    SystemDataMapper systemDataMapper;

    private static final Logger log= LoggerFactory.getLogger(ClaimsServiceImpl.class);

    /**
     * 获得债权列表
     * @param rpo 债权查询分页条件
     * @return 债权列表
     */
    @Override
    public Page<Claims> getClaimsList(ClaimsListRPO rpo){

        Page<Claims> claimsPage= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        claimsMapper.getClaimsListByRpo(rpo);
//        System.out.println(claimsPage.getResult());
        return claimsPage;
    }

    /**
     * 获得债权详情
     * @param id 债权id
     * @return 指定id债权详情
     */
    @Override
    public Claims getClaimsById(long id) {

        return claimsMapper.getClaimsById(id);
    }

    /**
     * 修改债权信息，债权代号不可修改，债权出借期限，出借日期修改后需修改定时任务，
     * 债权出借金额修改后需修改剩余待匹配金额
     * @param claims 新债权信息
     * @return 修改结果，0代表失败，大于0代表成功
     */
    @Override
    public int updateClaims(Claims claims) {
        Claims oldClaims=claimsMapper.getClaimsMatchingById(claims.getId());
        //获取旧过期时间
        long oldLendEndAt=oldClaims.getLendEndAt();
        //计算新过期时间
        long newLendEndAt=claims.getLendStartAt()+claims.getLendDeadline()*30*24*3600*1000;
        //设置新过期时间
        claims.setLendEndAt(newLendEndAt);
        //获取旧定时任务时间
        long oldTaskTime=timedTaskMapper.getOldTaskTimeByClaimsId(claims.getId());
        //获取旧出借金额
        BigDecimal oldLendMoney=new BigDecimal(oldClaims.getLendMoney());
//        System.out.println(oldLendMoney);
        //获取旧待匹配金额
        BigDecimal oldRemanentMoney=new BigDecimal(oldClaims.getRemanentMoney());
        //获取新出借金额
        BigDecimal newLendMoney=new BigDecimal(claims.getLendMoney());
        //计算新待匹配金额
        String newRemanentMoney=(newLendMoney.add(oldRemanentMoney).subtract(oldLendMoney)).toString();
//        System.out.println(newRemanentMoney);
        //设置新待匹配金额
        claims.setRemanentMoney(newRemanentMoney);
        //更新债权
        int a=claimsMapper.updateClaims(claims);
        //计算新定时任务时间
        long newTaskTime=newLendEndAt-oldLendEndAt+oldTaskTime;
        TimedTask timedTask=new TimedTask();
        timedTask.setClaimsId(claims.getId());
        timedTask.setTaskTime(newTaskTime);
        timedTask.setUpdateAt(System.currentTimeMillis());
        timedTask.setUpdateBy(claims.getUpdateBy());
        //更新定时任务时间
        int b=timedTaskMapper.updateTaskTimeByClaimsId(timedTask);
        return a*b;
    }

    /**
     * 新增债权，需要手动设置待匹配金额，过期时间，状态
     * 需要新增定时任务
     * @param claims 新增的债权详情
     * @return 新增结果
     */
    @Override
    public int addClaims(Claims claims) {
        //设置待匹配金额
        claims.setRemanentMoney(claims.getLendMoney());
        //设置到期时间
        long lendEndAt=claims.getLendStartAt()+claims.getLendDeadline()*30*24*3600*1000;
        claims.setLendEndAt(lendEndAt);
        //设置债权状态为未使用
        claims.setStatus(Claims.STATUS_NOT_USE);
        //新增债权
        int a=claimsMapper.addClaims(claims);
        //获取新增债权id
        long claimsId=claims.getId();
        log.info("新增债权，id为"+claimsId);
        //新建定时任务
        TimedTask timedTask=new TimedTask();
        //获取债权到期天数设置,单位天,转化为long
        BigDecimal creditorDay=new BigDecimal(systemDataMapper.getCreditorDay());
        long creditorDayLong=creditorDay.longValue();
        //计算定时任务时间
        long taskTime=lendEndAt-creditorDayLong*24*3600*1000;
        timedTask.setTaskTime(taskTime);
        timedTask.setClaimsId(claimsId);
        timedTask.setNature(TimedTask.NATURE_CLAIMS);
        timedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
        timedTask.setCreateBy(claims.getCreateBy());
        timedTask.setCreateAt(System.currentTimeMillis());
        //插入定时任务
        int b=timedTaskMapper.addTaskedTime(timedTask);
        return a*b;
    }
}
