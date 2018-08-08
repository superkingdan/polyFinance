package com.jnshu.dao3;

import com.jnshu.entity.SystemData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * SystemData相关sql语句
 */
@Mapper
@Repository(value ="systemDataMapper3")
public interface SystemDataMapper3 {
    /**
     * 根据id查找用户详情
     */
    @Select("select * from system_data where id=#{id} ")
    SystemData findDataById(long id);
    /**
     * 根据dataName查找
     */
    @Select("select * from system_data where data_name='${value}' ")
    SystemData findByDataName(String dataName);

}
