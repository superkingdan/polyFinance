package com.jnshu.service.implement;

import com.jnshu.dao2.RoleUserBackMapper2;
import com.jnshu.entity.RoleUserBack;
import com.jnshu.service.RoleUserBackService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/7.
 */

@Service
@Component
public class RoleUserBackServiceImpl2 implements RoleUserBackService2 {

    @Autowired
    RoleUserBackMapper2 roleUserBackMapper2;
    @Override
    public Boolean updateRoleUserBack(RoleUserBack roleUserBack) throws Exception {
        return roleUserBackMapper2.updateRoleUserBack(roleUserBack);
    }

    //账户删除使用。删除角色用户管理记录。
    @Override
    public Boolean deleteRoleUserBackByUser(Long userId) throws Exception {
        return roleUserBackMapper2.deleteRoleUserBackByUser(userId);
    }

    //新增用户角色关联记录。
    @Override
    public Long saveRoleUserBack(RoleUserBack roleUserBack) throws Exception {
        return roleUserBackMapper2.saveRoleUserBack(roleUserBack);
    }
}
