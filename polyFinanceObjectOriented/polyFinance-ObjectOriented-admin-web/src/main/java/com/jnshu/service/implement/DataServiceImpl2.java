package com.jnshu.service.implement;

import com.jnshu.dao2.DataMapper2;
import com.jnshu.entity.SystemData;
import com.jnshu.service.DataService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */

@Service
@Component
public class DataServiceImpl2 implements DataService2 {

    @Autowired
    DataMapper2 dataMapper2;
    //参数获取。
    @Override
    public List<SystemData> getSystemData() throws Exception {
        return dataMapper2.getSystemData();
    }

    //参数更新
    @Override
    public Boolean updateSystemData(SystemData systemData) throws Exception {
        return dataMapper2.updateSystemData(systemData);
    }

    //参数备份。
    @Override
    public Integer updateAsBackup() throws Exception {
        return dataMapper2.updateAsBackup();
    }

    //参数还原
    @Override
    public Integer updataDataFromBackup() throws Exception {
        return dataMapper2.updataDataFromBackup();
    }

    //版本管理--查询
    @Override
    public List<SystemData> getSystemDataOfVersion() throws Exception {
        return dataMapper2.getSystemDataOfVersion();
    }

    @Override
    public Boolean updateSystemDataOfVersion(SystemData systemData) throws Exception {
        return dataMapper2.updateSystemDataOfVersion(systemData);
    }
}
