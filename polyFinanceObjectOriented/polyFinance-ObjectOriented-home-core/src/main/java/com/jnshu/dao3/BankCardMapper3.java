package com.jnshu.dao3;


import com.jnshu.dto3.BankCardList;
import com.jnshu.entity.BankCard;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * bankcard相关sql语句
 */
@Mapper
@Repository(value ="bankCardMapper3")
public interface BankCardMapper3 {
//    获取银行卡信息:select * bank_card where user_id=#{id}
//    添加银行卡：insert into bank_card() values()
//    点击银行卡 把他变更为默认
      /**
       * 根据userid获取整合过的银行卡信息
       */
      @Select("select bank_card.id,bank_card.bank_card,bank.bank_name,bank.icon," +
              "bank.single_limited,bank.day_limited from bank_card " +
              "inner join bank on bank_card.bank_id=bank.id where bank_card.user_id=#{id} order by bank_card.card_order")
      List<BankCardList> findListByUser(long id);
      /**
       * 根据userid获取银行卡
       */
      @Select("select * from bank_card where user_id=#{id} order by card_order desc")
      List<BankCard> findBankCardByUser(long id);

      /**
       * 根据userid获取银行卡
       */
      @Select("select * from bank_card where user_id=#{id} and card_order=1")
      BankCard findBankCardByOrder(long id);

     /**
      * 根据卡号获取银行卡
      */
     @Select("select * from bank_card  where bank_card=#{bankCard}")
     BankCard findBankCardByIdCard(String bankCard);
    /**
        * 根据id获取银行卡数量
        */
       @Select("select count(*) from bank_card where user_id=#{id} ")
       int findCountByUser(long id);
       /**
        * 根据id获取银行卡
        */
       @Select("select * from bank_card where id=#{id}")
       BankCard findById(long id);
       /**
        * 添加银行卡银行卡
        */
       @Insert("insert into bank_card (create_at,create_by,bank_card,bank_id,bank_phone,user_id,city,card_order) " +
               "values (#{createAt},#{createBy},#{bankCard},#{bankId},#{bankPhone},#{userId},#{city},#{cardOrder})")
       @Options(useGeneratedKeys=true,keyProperty="id")
       int addBankCard(BankCard bankCard);

       /**
        * 设置银行卡顺序
        */
       @Update("update bank_card set card_order=#{cardOrder} where id=#{id}")
       boolean updateOder(BankCard bankCard);

        /**
         * 修改银行卡 备用
         */
        @UpdateProvider(type = UpdateData.class,method = "updateData")
        boolean updateData(BankCard bankCard);
        class  UpdateData{
            public String updateData(BankCard bankCard){
            return new SQL(){{
                UPDATE("bank_card");
                if (bankCard.getUpdateAt()!=0){
                    SET("update_at=#{updateAt}");}
                if (bankCard.getUpdateBy()!=0){
                    SET("update_by=#{updateBy}");}
                if (bankCard.getUpdateAt()!=0){
                    SET("update_at=#{updateAt}");}
                if (bankCard.getCreateBy()!=0){
                    SET("create_by=#{createBy}");}
                if (bankCard.getBankCard()!=null){
                    SET("bank_card=#{bankCard}");}
                if (bankCard.getBankPhone()!=null){
                    SET("bank_phone=#{bankPhone}");}
                if (bankCard.getUserId()!=0){
                    SET("user_id=#{userId}");}
                if (bankCard.getCity()!=null){
                    SET("city=#{city}");}
                if (bankCard.getCardOrder()!=0){
                    SET("card_order=#{cardOrder}");}
                if (bankCard.getUserId()!=0){
                    WHERE("user_id=#{id}");
                }   else {WHERE("id=#{id}");}
            }}.toString();
        }}

}
