package com.jnshu.dao3;

import com.jnshu.entity.RealNameApplication;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 实名验证相关sql语句
 */
@Mapper
@Repository(value ="realNameApplicationMapper3")
public interface RealNameApplicationMapper3 {
    /**
     * 新增
     */
    @Insert("insert into real_name_application (create_at,create_by,user_id,real_name,id_card,front_card,reverse_card,application_status,is_first)" +
            "values (#{createAt},#{createBy},#{userId},#{realName},#{idCard},#{frontCard},#{reverseCard},#{applicationStatus},#{isFirst})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    void addRealName(RealNameApplication realNameApplication);

    @Select("select * from real_name_application where user_id=#{id}")
    RealNameApplication findByUserId(long id);

    @Select("select * from real_name_application where id_card=#{idCard} and application_status=1")
    List<RealNameApplication> findIdCard(String idCard);

    /**
     * 修改资料(all)
     */
    @UpdateProvider(type = UpdateData.class,method = "updateData")
    boolean updateData(RealNameApplication realNameApplication);
    class  UpdateData{
        public String updateData(RealNameApplication realNameApplication){
            return new SQL(){{
                UPDATE("real_name_application");
                if (realNameApplication.getUpdateAt()!=0){
                    SET("update_at=#{updateAt}");}
                if (realNameApplication.getUpdateBy()!=0){
                    SET("update_by=#{updateBy}");}
                if (realNameApplication.getFrontCard()!=null){
                    SET("front_card=#{frontCard}");}
                if (realNameApplication.getReverseCard()!=null){
                    SET("reverse_card=#{reverseCard}");}
                if (realNameApplication.getRealName()!=null){
                    SET("real_name=#{realName}");}
                if (realNameApplication.getIdCard()!=null){
                    SET("id_card=#{idCard}");}

                if (realNameApplication.getIsFirst()!=0){
                    SET("is_first=#{isFirst}");}
                if (realNameApplication.getRefuseReason()!=null){
                    SET("refuse_reason=#{refuseReason}");}
                    SET("application_status=#{applicationStatus}");
                    WHERE("id=#{id}");
            }}.toString();
        }
    }
}
