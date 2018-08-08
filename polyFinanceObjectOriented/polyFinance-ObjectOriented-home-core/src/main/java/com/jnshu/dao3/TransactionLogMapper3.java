package com.jnshu.dao3;

import com.jnshu.entity.TransactionLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * TransactionLog语句
 */
@Mapper
@Repository(value ="transactionLogMapper3")
public interface TransactionLogMapper3 {

    /**
     * 新增交易流水
     */
    @Insert("insert into transaction_log (create_at,create_by,user_id,product_name,transaction_at,transaction_way,money,bank_log,status) " +
            "values (#{createAt},#{createBy},#{user_id},#{product_name},#{transaction_at},#{transaction_way},#{money},#{bank_log},#{status})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addTransactionLog(TransactionLog transactionLog);
}
