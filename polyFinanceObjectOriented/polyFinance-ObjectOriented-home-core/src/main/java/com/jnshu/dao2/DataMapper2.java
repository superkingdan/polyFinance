package com.jnshu.dao2;

import com.jnshu.entity.SystemData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value ="dataMapper2")
public interface DataMapper2 {

    //参数设置--更新
    @Update("update system_data set data_value=#{dataValue} ,update_at=#{updateAt}, update_by=#{updateBy} where data_name=#{dataName} and not(data_value = #{dataValue})")
    Boolean updateSystemData(SystemData systemData) throws Exception;

    //参数设置--获取
    @Select("select data_name, data_value from system_data where id <5 and id >0")
    List<SystemData> getSystemData() throws Exception;

    //参数设置--更新时，备份.
    @Update("update system_data a, system_data b set a.data_value=b.data_value where a.data_name =concat(b.data_name,'2') and not(a.data_value=b.data_value) ")
    Integer updateAsBackup() throws Exception;

    //参数设置--还原。
    @Update("update system_data a, system_data b set a.data_value=b.data_value where concat(a.data_name,'2') = b.data_name and not(a.data_value=b.data_value)")
    Integer updataDataFromBackup() throws Exception;

    //版本管理--查询。
    @Select("select data_name, data_value from system_data where id<13 and id>8")
    List<SystemData> getSystemDataOfVersion() throws Exception;

    //版本参数更新。
    @Update("update system_data set data_value=#{dataValue} ,update_at=#{updateAt}, update_by=#{updateBy} where data_name=#{dataName} and not(data_value = #{dataValue})")
    Boolean updateSystemDataOfVersion(SystemData systemData) throws Exception;
}
