package com.jnshu.dao;

import com.jnshu.entity.TimedTask;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 定时任务相关SQL
 * @author  wangqichao
 */
@Mapper
@Component(value = "timedTaskMapper1")
public interface TimedTaskMapper1 {
    //删除续投订单原定时任务
    @Delete("delete from timed_task where transaction_id=#{transactionId} and nature=1")
    int deleteOldTransReturn(long transactionId);

    //修改原订单的本息一次还定时任务为只还利息
    @Update("update timed_task set update_at=#{updateAt},update_by=#{updateBy},nature=#{nature},money=#{money} where transaction_id=#{transactionId} and nature=1")
    int updateOldTransByTransId(TimedTask timedTask);

    //添加定时任务
    @Insert("insert into timed_task (create_at,create_by,status,task_time,nature,money,contract_id,claims_id,message_id,transaction_id) values (#{createAt},#{createBy},#{status},#{taskTime},#{nature},#{money},#{contractId},#{claimsId},#{messageId},#{transactionId})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addTaskedTime(TimedTask timedTask);

    //获取原定时任务时间
    @Select("select task_time from timed_task where claims_id=#{ClaimsId}")
    Long getOldTaskTimeByClaimsId(long claimsId);

    //获取原定时任务最后一次返本的金额
    @Select("select money from timed_task where transaction_id=#{transactionId} and nature=1")
    String getOldMoneyByTransactionId(long transactionId);

    //修改原定时任务时间
    @Update("update timed_task set update_at=#{updateAt},update_by=#{updateBy},task_time=#{taskTime} where claims_id=#{claimsId}")
    int updateTaskTimeByClaimsId(TimedTask timedTask);
}
