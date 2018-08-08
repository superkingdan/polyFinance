package com.jnshu.dao3;

import com.jnshu.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

/**
 * user相关sql语句
 */
@Mapper
@Repository(value ="userMapper3")
public interface UserMapper3 {

    /**
     * 新增用户
     */
    @Insert("insert into user (create_at,create_by,user_number,phone_number,salt,hash_key,referrer_id) " +
            "values (#{createAt},#{createBy},#{userNumber},#{phoneNumber},#{salt},#{hashKey},#{referrerId})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addUser(User user);

    //查找最新用户编号
    @Select("select user_number from user order by create_at desc limit 0,1")
    String getOldUserNumber();

    /**
     * 根据id查找用户详情
     */
    @Select("select * from user where id=#{id} ")
    User findUserById(long id);

    /**
     * 根据手机号查找用户详情
     */
    @Select("select * from user where phone_number=#{phoneNumber} ")
    User findUserByPhone(String phoneNumber);


    /**
     * 修改资料(all)
     */
    @UpdateProvider(type = UpdateData.class,method = "updateData")
    boolean updateData(User user);
    class  UpdateData{
        public String updateData(User user){
            return new SQL(){{
                UPDATE("user");
                if (user.getUpdateAt()!=0){
                    SET("update_at=#{updateAt}");}
                if (user.getUpdateBy()!=0){
                    SET("update_by=#{updateBy}");}
                if (user.getSalt()!=null){
                    SET("salt=#{salt}");}
                if (user.getHashKey()!=null){
                    SET("hash_key=#{hashKey}");}
                if (user.getRealName()!=null){
                    SET("real_name=#{realName}");}
                if (user.getRealStatus()!=0){
                    SET("real_status=#{realStatus}");}
                if (user.getIdCard()!=null){
                    SET("id_card=#{idCard}");}
                if (user.getIsNew()!=0){
                    SET("is_new=#{isNew}");}
                if (user.getEmail()!=null){
                    SET("email=#{email}");}
                if (user.getAddress()!=null){
                    SET("address=#{address}");}
                if (user.getDefaultCard()!=0){
                    SET("default_card=#{defaultCard}");}
                if (user.getGesturePassword()!=0){
                    SET("gesture_password=#{gesturePassword}");}
                if (user.getProperty()!=null){
                    SET("property=#{property}");}
                if (user.getCumulativeIncome()!=null){
                    SET("cumulative_income=#{cumulativeIncome}");}
                if (user.getStatus()!=0){
                    SET("status=#{status}");}
                if (user.getPhoneNumber()!=null){
                    WHERE("phone_number=#{phoneNumber}");
                }   else {WHERE("id=#{id}");}
            }}.toString();
        }
    }
}