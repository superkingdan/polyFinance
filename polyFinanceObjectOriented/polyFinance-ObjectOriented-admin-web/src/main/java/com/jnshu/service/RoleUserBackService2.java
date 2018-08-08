package com.jnshu.service;

import com.jnshu.entity.RoleUserBack;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/8/7.
 */
@Component(value = "roleUserBackService2")
public interface RoleUserBackService2 {

    Boolean updateRoleUserBack(RoleUserBack roleUserBack) throws Exception;

    //账户删除时使用。
    Boolean deleteRoleUserBackByUser(Long userId) throws Exception;

    //新增用户角色关联记录。
    Long saveRoleUserBack(RoleUserBack roleUserBack) throws Exception;
}
