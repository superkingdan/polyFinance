package com.jnshu.dao;

import com.jnshu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *用户表相关SQL
 * @author wangqichao
 */
@Mapper
@Component(value = "userMapper")
public interface UserMapper {
    //获得用户真实信息
    @Select("select real_name,id_card from user where id=#{userId}")
    User getUserRealInfo(long userId);

    //获得用户资产
    @Select("select property from user where id=#{userId} ")
    String getPropertyById(long userId);

    //修改用户资产
    @Update("update user set property=#{property} where id=#{id} ")
    int updatePropertyById(User user);

    //查询默认银行卡id
    @Select("select default_card from user where id=#{user_id}")
    Long getDefaultCardById(long userId);

    //通过合同code获得用户真实信息
    @Select("select user.real_name,user.id_card from user inner join transaction on user.id=transaction.user_id where transaction.contract_code=#{contractCode}")
    User getUserInfoByContractCode(String contractCode);

    //通过姓名查找用户id
    @Select("select id from user where real_name=#{userName}")
    Long[] getUserIdByName(String userName);

    //通过id查找用户姓名
    @Select("select real_name from user where id=#{userId}")
    String getUserNameById(long userId);


}
