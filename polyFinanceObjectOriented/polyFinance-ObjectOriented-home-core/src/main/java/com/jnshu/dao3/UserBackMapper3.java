package com.jnshu.dao3;

import com.jnshu.entity.UserBack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * userBack相关sql语句
 */
@Mapper
@Repository(value ="UserBackMapper3")
public interface UserBackMapper3 {
    @Select("select *  from user_back where login_name like \"%\"#{loginName}\"%\"")
    UserBack findByName(String loginName);
}
