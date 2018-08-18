package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.ClaimsMapper1;
import com.jnshu.dao.SystemDataMapper1;
import com.jnshu.dao.TimedTaskMapper1;
import com.jnshu.dto1.ClaimsListRPO;
import com.jnshu.entity.Claims;
import com.jnshu.entity.TimedTask;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ClaimsService1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 债权相关service
 * @author wangqichao
 */
@Service
//开启事务
@Transactional
public class ClaimsServiceImpl1 implements ClaimsService1 {
    @Autowired
    ClaimsMapper1 claimsMapper1;
    @Autowired
    TimedTaskMapper1 timedTaskMapper1;
    @Autowired
    SystemDataMapper1 systemDataMapper1;

    private static final Logger log= LoggerFactory.getLogger(ClaimsServiceImpl1.class);

    /**
     * 获得债权列表
     * @param rpo 债权查询分页条件
     * @return 债权列表
     */
    @Override
    public Page<Claims> getClaimsList(ClaimsListRPO rpo)throws Exception{
        log.info("获得债权列表");
        Page<Claims> claimsPage= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        try{
            claimsMapper1.getClaimsListByRpo(rpo);
        }catch (Exception e){
            throw new MyException(-1,"获得债权列表失败");
        }
        return claimsPage;
    }

    /**
     * 获得债权详情
     * @param id 债权id
     * @return 指定id债权详情
     */
    @Override
    public Claims getClaimsById(long id) throws Exception{
        log.info("获取指定债权"+id+"详情");
        Claims claims;
        try{
            claims=claimsMapper1.getClaimsById(id);
        }catch (Exception e){
            throw new MyException(-1,"获取债权"+id+"失败");
        }
        return claims;
    }

    /**
     * 修改债权信息，债权代号不可修改，债权出借期限，出借日期修改后需修改定时任务，
     * 债权出借金额修改后需修改剩余待匹配金额
     * @param claims 新债权信息
     * @return 修改结果，0代表失败，大于0代表成功
     */
    @Override
    public int updateClaims(Claims claims) throws Exception{
        log.info("修改债权"+claims.getId());
        try {
            Claims oldClaims;
            try {
                oldClaims = claimsMapper1.getClaimsMatchingById(claims.getId());
            } catch (Exception e) {
                throw new MyException(-1, "获取旧债权失败");
            }
            if (oldClaims == null) {
                throw new MyException(-1, "旧债权信息不存在");
            }
            //获取旧过期时间
            long oldLendEndAt = oldClaims.getLendEndAt();
            //计算新过期时间
            Calendar startAtC = Calendar.getInstance();
            startAtC.clear();
            startAtC.setTimeInMillis(claims.getLendStartAt());
            startAtC.add(Calendar.MONTH, claims.getLendDeadline());
            long newLendEndAt = startAtC.getTimeInMillis();
            //设置新过期时间
            claims.setLendEndAt(newLendEndAt);
            //获取旧定时任务时间
            long oldTaskTime;
            try {
                oldTaskTime = timedTaskMapper1.getOldTaskTimeByClaimsId(claims.getId());
            } catch (Exception e) {
                throw new MyException(-1, "旧债权定时任务信息不存在");
            }
            //获取旧出借金额
            BigDecimal oldLendMoney = new BigDecimal(oldClaims.getLendMoney());
            //获取旧待匹配金额
            BigDecimal oldRemanentMoney = new BigDecimal(oldClaims.getRemanentMoney());
            //获取新出借金额
            BigDecimal newLendMoney = new BigDecimal(claims.getLendMoney());
            //计算新待匹配金额
            String newRemanentMoney = (newLendMoney.add(oldRemanentMoney).subtract(oldLendMoney)).toString();
            //设置新待匹配金额
            claims.setRemanentMoney(newRemanentMoney);
            //更新债权
            try {
                claimsMapper1.updateClaims(claims);
            } catch (Exception e) {
                throw new MyException(-1, "更新债权失败");
            }
            //计算新定时任务时间
            long newTaskTime = newLendEndAt - oldLendEndAt + oldTaskTime;
            TimedTask timedTask = new TimedTask();
            timedTask.setClaimsId(claims.getId());
            timedTask.setTaskTime(newTaskTime);
            timedTask.setUpdateAt(System.currentTimeMillis());
            timedTask.setUpdateBy(claims.getUpdateBy());
            //更新定时任务时间
            try {
                timedTaskMapper1.updateTaskTimeByClaimsId(timedTask);
            } catch (Exception e) {
                throw new MyException(-1, "更新债权定时任务失败");
            }
            return 1;
        }catch (Exception e){
            throw new MyException(-1,"更新债权未知错误");
        }
    }

    /**
     * 新增债权，需要手动设置待匹配金额，过期时间，状态
     * 需要新增定时任务
     * @param claims 新增的债权详情
     * @return 新增结果
     */
    @Override
    public int addClaims(Claims claims) throws Exception{
        log.info("新增债权，其信息为："+claims);
        try {
            //设置待匹配金额
            claims.setRemanentMoney(claims.getLendMoney());
            //设置到期时间
            Calendar lendEndAtC = Calendar.getInstance();
            lendEndAtC.clear();
            lendEndAtC.setTimeInMillis(claims.getLendStartAt());
            lendEndAtC.add(Calendar.MONTH, claims.getLendDeadline());
            long lendEndAt = lendEndAtC.getTimeInMillis();
            claims.setLendEndAt(lendEndAt);
            //设置债权状态为未使用
            claims.setStatus(Claims.STATUS_NOT_USE);
            //新增债权
            try {
                claimsMapper1.addClaims(claims);
            } catch (Exception e) {
                throw new MyException(-1, "新增债权失败");
            }
            //获取新增债权id
            long claimsId = claims.getId();
            log.info("新增债权，id为" + claimsId);
            //新建定时任务
            TimedTask timedTask = new TimedTask();
            //获取债权到期天数设置,单位天,转化为long
            BigDecimal creditorDay;
            try {
                creditorDay = new BigDecimal(systemDataMapper1.getCreditorDay());
            } catch (Exception e) {
                throw new MyException(-1, "获得债权到期天数失败");
            }
            long creditorDayLong = creditorDay.longValue();
            //计算定时任务时间
            long taskTime = lendEndAt - creditorDayLong * 24 * 3600 * 1000;
            timedTask.setTaskTime(taskTime);
            timedTask.setClaimsId(claimsId);
            timedTask.setNature(TimedTask.NATURE_CLAIMS);
            timedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            timedTask.setCreateBy(claims.getCreateBy());
            timedTask.setCreateAt(System.currentTimeMillis());
            //插入定时任务
            try {
                timedTaskMapper1.addTaskedTime(timedTask);
            } catch (Exception e) {
                throw new MyException(-1, "新增债权失败");
            }
            return 1;
        }catch (Exception e){
            throw new MyException(-1,"新增债权失败");
        }
    }
}
