package com.jnshu.dao3;

import com.jnshu.entity.RealNameApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 实名验证相关sql语句
 */
@Mapper
@Repository(value ="realNameApplicationMapper3")
public interface RealNameApplicationMapper3 {
    /**
     * 新增
     */
    @Insert("insert into real_name_applicatiom (create_at,create_by,user_id,real_name,id_card,front_card,reverse_card,application_status,is_first,) " +
            "values (#{createAt},#{createBy},#{id},#{realName},#{idCard},#{frontCard},#{reverseCard},#{applicationStatus},#{isFirst})")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int addRealName(RealNameApplication realNameApplication);

    @Select("select * from real_name_applicatiom where user_id=#{id}")
    RealNameApplication findByUserId(long id);
}
