package com.jnshu.service.implement;

import com.github.pagehelper.PageHelper;
import com.jnshu.Domain2.DomainApplication;
import com.jnshu.dao2.ApplicationMapper2;
import com.jnshu.dto2.ApplicationListRPO;
import com.jnshu.entity.RealNameApplication;
import com.jnshu.service.UserApplicationService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class UserApplicationServiceImpl2 implements UserApplicationService2 {

    @Autowired
    ApplicationMapper2 applicationMapper2;

    @Override
    public List<DomainApplication> getAllUser(Integer pageNum, Integer pageSize, ApplicationListRPO rpo) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        return applicationMapper2.getApplicationList(rpo);
    }

    @Override
    public List<DomainApplication> getAllUser2(ApplicationListRPO rpo) throws Exception {
        return applicationMapper2.getApplicationList(rpo);
    }

    //获得总数。
    @Override
    public Integer getCount() throws Exception {
        return applicationMapper2.getTotal();
    }

    //获得实名详情
    @Override
    public DomainApplication getApplicationById(Long id) throws Exception {
        return applicationMapper2.getApplicationById(id);
    }

    //取消实名
    @Override
    public Boolean cancelApplicationStatus(RealNameApplication realNameApplication) throws Exception {
        return applicationMapper2.cancelApplicationStatus(realNameApplication);
    }

    @Override
    public Boolean cancelApplicationStatus2(Long userId) throws Exception{
        return applicationMapper2.cancelApplicationStatus2(userId);
    }
    //审核实名申请。
    @Override
    public Boolean reviewApplication(RealNameApplication realNameApplication) throws Exception {
        return applicationMapper2.reviewApplication(realNameApplication);
    }

    //更新user表
    @Override
    public Boolean updateUserFrontAfterApplication(Long id) throws Exception {
        return applicationMapper2.updateUserFrontAfterApplication(id);
    }
}
