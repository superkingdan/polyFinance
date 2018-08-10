package com.jnshu.dao3;


import com.jnshu.entity.TimedTask;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 定时任务相关sql语句
 */
@Mapper
@Component(value ="timedTaskMapper3")
public interface TimedTaskMapper3 {

     /**
      * 新增定时任务
      */
     @Insert("insert into timed_task (create_at,create_by,status,task_time,money,transaction_id,message_id,claims_id,bank_log,super_id) " +
             "values (#{createAt},#{createBy},#{status},#{taskTime},#{money},#{transactionId},#{messageId},#{claimsId},#{bankLog},#{superId})")
     @Options(useGeneratedKeys=true,keyProperty="id")
     int addTask(TimedTask timedTask);

     /**
      * 根据id查找任务
      */
     @Select("select * from timed_task where id=#{id} ")
     TimedTask findTaskById(long id);

    /**
     * 根据id查找任务
     */
    @Select("select * from timed_task where message_id=#{id} ")
    TimedTask findMessage(long id);

    /**
     * 总定时任务扫描
     */
     @Select("select * from timed_task where status=0 ")
     List<TimedTask> findTaskByStatus();
    /**
     * 总定时任务6扫描
     */
     @Select("select * from timed_task where nature=#{nature} and status=0")
     List<TimedTask> findTaskByNature(int nature);

     /**
      * 修改任务(all)
      */
     @UpdateProvider(type = UpdateTask.class,method = "updateTask")
     boolean updateTask(TimedTask timedTask);
     class  UpdateTask{
         public String updateTask(TimedTask timedTask){
             return new SQL(){{
                 UPDATE("timed_task");
                 if (timedTask.getUpdateAt()!=0){
                     SET("update_at=#{updateAt}");}
                 if (timedTask.getUpdateBy()!=0){
                     SET("update_by=#{updateBy}");}
                 if (timedTask.getStatus()!=0){
                     SET("status=#{status}");}
                 if (timedTask.getTaskTime()!=0){
                     SET("task_time=#{taskTime}");}
                 if (timedTask.getMoney()!=null){
                    SET("money=#{money}");}
                 if (timedTask.getTransactionId()!=0){
                    SET("transaction_id=#{transactionId}");}
                 if (timedTask.getMessageId()!=0){
                     SET("message_id=#{messageId}");}
                 if (timedTask.getClaimsId()!=0){
                     SET("claims_id=#{claimsId}");}
                 if (timedTask.getBankLog()!=null){
                     SET("bank_log=#{bankLog}");}
                 if (timedTask.getSuperId()!=0){
                     SET("super_id=#{superId}");}
                 WHERE("id=#{id}");
             }}.toString();
         }
     }
      /**
       * 删除任务
       */
      @Delete("delete from timed_task where id={id}")
      boolean deleteTask(long id);



}
