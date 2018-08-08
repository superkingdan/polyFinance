package com.jnshu.dao3;

import com.jnshu.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Transaction相关sql语句
 */
@Mapper
@Repository(value ="transactionMapper3")
public interface TransactionMapper3 {

    /**
     * 根据交易id获取交易
     */
    @Select("Select * from transaction where id=#{id}")
    Transaction findTransaction(long id);


    /**
     *  修改续投状态(为超过续投时间)
     */
    @Update("Update transaction set renuwal_status=#{renuwalStatus} where id=#{transactionId}")
    boolean updateRenuwalStatus(Transaction transaction);

    /**
     *  修改交易状态(为超过续投时间)
     */
    @Update("Update transaction set status=#{status},returned=#{returned},not_return=#{notReturn} where id=#{transactionId}")
    boolean updateStatus(Transaction transaction);





}
