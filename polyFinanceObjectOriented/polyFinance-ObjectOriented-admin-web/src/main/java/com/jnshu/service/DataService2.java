package com.jnshu.service;

import com.jnshu.entity.SystemData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */
@Component(value = "dataService2")
public interface DataService2 {

    //参数获取
    List<SystemData> getSystemData() throws Exception;

    //参数更新
    Boolean updateSystemData(SystemData systemData) throws Exception;

    //参数备份
    Integer updateAsBackup() throws Exception;

    //参数还原。
    Integer updataDataFromBackup() throws Exception;

    //版本管理。
    //版本管理--查询。
    List<SystemData> getSystemDataOfVersion() throws Exception;

    //版本参数更新。
    Boolean updateSystemDataOfVersion(SystemData systemData) throws Exception;
}
