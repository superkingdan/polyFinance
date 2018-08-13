package com.jnshu.service.implement;

import com.jnshu.Domain2.DomainModuleBackForLogin;
import com.jnshu.Domain2.DomainRoleBack;
import com.jnshu.Domain2.DomainRoleBackList;
import com.jnshu.dao2.ModuleBackMapper2;
import com.jnshu.dao2.RoleBackMapper2;
import com.jnshu.dao2.RoleModuleBackMapper2;
import com.jnshu.dao2.RoleUserBackMapper2;
import com.jnshu.entity.RoleBack;
import com.jnshu.entity.RoleModuleBack;
import com.jnshu.entity.RoleUserBack;
import com.jnshu.service.RoleBackService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */

@Service
@Component
public class RoleBackServiceImpl2 implements RoleBackService2 {

    @Autowired
    RoleBackMapper2 roleBackMapper2;

    @Autowired
    RoleModuleBackMapper2 roleModuleBackMapper2;

    @Autowired
    ModuleBackMapper2 moduleBackMapper2;

    @Autowired
    RoleUserBackMapper2 roleUserBackMapper2;

    //账户更新时使用。
    @Override
    public List<DomainRoleBack> getAll() throws Exception {
        return roleBackMapper2.getAll();
    }

    //角色列表
    @Override
    public List<DomainRoleBackList> getRoleBacks() throws Exception {
        return roleBackMapper2.getRoleBacks();
    }

    //通过id获取角色名。
    @Override
    public String getRoleNameById(Long id) throws Exception {
        return roleBackMapper2.getRoleNameById(id);
    }

    //获取角色权限的模块id的list.
    @Override
    public List<Long> getModuleIdListOfRole(Long roleId) throws Exception {
        return roleModuleBackMapper2.getModuleIdListOfRole(roleId);
    }

    //获取全部的模块id的list.
    @Override
    public List<Long> getAllModuleIds() throws Exception {
        return moduleBackMapper2.getAllModuleIds();
    }

    //通过角色id获取模块信息。模块角色关联表和模块表
    @Override
    public List<DomainModuleBackForLogin> getModuleOfRole(Long id) throws Exception {
        return roleModuleBackMapper2.getModuleOfRole(id);
    }

    //获取全部模块id，名称和父id。
    @Override
    public List<DomainModuleBackForLogin> getAllModules() throws Exception {
        return moduleBackMapper2.getAllModules();
    }

    //角色新增
    @Override
    public Long saveRoleBack(RoleBack roleBack) throws Exception {
        return roleBackMapper2.saveRoleBack(roleBack);
    }

    //根据角色名，查询id。
    @Override
    public Long getRoleIdByRole(String role) throws Exception {
        return roleBackMapper2.getRoleIdByRole(role);
    }

    //新增角色模块关联记录。
    @Override
    public Long saveRoleModule(RoleModuleBack roleModuleBack) throws Exception {
        return roleModuleBackMapper2.savERoleModule(roleModuleBack);
    }

    //新增角色用户关联记录。
    @Override
    public Long saveRoleUser(RoleUserBack roleUserBack) throws Exception {
        return roleUserBackMapper2.saveRoleUserBack(roleUserBack);
    }

    //删除模块角色管理表记录。
    @Override
    public Boolean deleteRoleModuleByRoleId(Long roleId) throws Exception {
        return roleModuleBackMapper2.deleteByRoleId(roleId);
    }

    @Override
    //获得角色关联的模块id list。
    public List<Long> getRoleModuleIdList(Long roleId) throws Exception{
        return roleModuleBackMapper2.getRoleModuleIdList(roleId);
    }

    //删除角色记录。
    @Override
    public Boolean deleteRoleBackById(Long id) throws Exception {
        return roleBackMapper2.deleteRoleBack(id);
    }

    //删除角色用户关联记录。
    @Override
    public Boolean deleteRoleUserByRoleId(Long id) throws Exception {
        return roleUserBackMapper2.deleteRoleUserBackByRole(id);
    }
}
