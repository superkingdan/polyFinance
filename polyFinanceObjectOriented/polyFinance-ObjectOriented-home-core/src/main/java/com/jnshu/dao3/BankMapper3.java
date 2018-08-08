package com.jnshu.dao3;

import com.jnshu.entity.Bank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * bank相关sql语句
 */
@Mapper
@Repository(value ="bankMapper3")
public interface BankMapper3 {
   //    根据银行id查找名字
      @Select("select * from bank where id=#{id} ")
      Bank findBankById(long id);
   //    根据银行名字查找id
      @Select("select * from bank where bank_name=#{bankName} ")
      Bank findBankByName(String bankName);

   @Select("select bank_name,icon from bank ")
   List<Bank> findBank();

}
