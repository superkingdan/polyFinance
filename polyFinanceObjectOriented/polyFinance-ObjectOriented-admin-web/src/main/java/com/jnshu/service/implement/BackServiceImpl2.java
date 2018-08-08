package com.jnshu.service.implement;

import com.github.pagehelper.PageHelper;
import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dao2.RoleUserBackMapper2;
import com.jnshu.dao2.UserBackMapper2;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.UserBack;
import com.jnshu.service.BackService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */

@Service
@Component
public class BackServiceImpl2 implements BackService2 {

    @Autowired(required = true)
    UserBackMapper2 userBackMapper2;

    @Autowired
    RoleUserBackMapper2 roleUserBackMapper2;
    //账户列表
    @Override
    public List<DomainUserBack> getUserBacksByNameAndRole(UserBackListRPO rpo) throws Exception {
        PageHelper.startPage(rpo.getPageNum(), rpo.getPageSize());
        return userBackMapper2.getUserBacksByNameAndRole(rpo);
    }

    //账户详情
    @Override
    public UserBack getUserBackById(Long id) throws Exception {
        return userBackMapper2.getUserBackById(id);
    }

    //总数
    @Override
    public Integer getTotal() throws Exception {
        return userBackMapper2.getTotal();
    }

    //更新账户
    @Override
    public Boolean updateUserBack(UserBack userBack) throws Exception {
        return userBackMapper2.updateUserBack(userBack);
    }

    //删除账户
    @Override
    public Boolean deleteById(Long id) throws Exception {
        return userBackMapper2.deleteById(id);
    }

    //新增账户
    @Override
    public Long saveUserBack(UserBack userBack) throws Exception {
        return userBackMapper2.saveUserBack(userBack);
    }

    //获取用户角色id。
    @Override
    public Long getRoleIdByUserId(Long id) throws Exception {
        return roleUserBackMapper2.getRoleIdByUserId(id);
    }
}
