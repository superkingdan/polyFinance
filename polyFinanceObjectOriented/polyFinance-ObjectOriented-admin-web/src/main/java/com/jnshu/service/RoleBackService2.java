package com.jnshu.service;

import com.jnshu.Domain2.DomainModuleBackForLogin;
import com.jnshu.Domain2.DomainRoleBack;
import com.jnshu.Domain2.DomainRoleBackList;
import com.jnshu.entity.RoleBack;
import com.jnshu.entity.RoleModuleBack;
import com.jnshu.entity.RoleUserBack;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/8/7.
 */
@Component(value = "roleBackService2")
public interface RoleBackService2 {

    //账户更新时使用。
    List<DomainRoleBack> getAll() throws Exception;

    //角色列表
    List<DomainRoleBackList> getRoleBacks() throws Exception;

    //通过id获取角色
    String getRoleNameById(Long id) throws Exception;

    //获取角色权限的模块id的list.
    List<Long> getModuleIdListOfRole(Long roleId) throws Exception;

    //通过角色id获取模块信息。模块角色关联表和模块表
    List<DomainModuleBackForLogin> getModuleOfRole(Long id) throws Exception;

    //获取全部模块id，名称和父id。
    List<DomainModuleBackForLogin> getAllModules() throws Exception;

    //根据角色名，查询id。
    Long getRoleIdByRole(String role) throws Exception;

    //新增角色
    Long saveRoleBack(RoleBack roleBack) throws Exception;

    //新增角色模块关联记录。
    Long saveRoleModule(RoleModuleBack roleModuleBack) throws Exception;

    //新增角色用户关联记录。
    Long saveRoleUser(RoleUserBack roleUserBack) throws Exception;

    //更新时删除旧的关联记录。
    Boolean deleteRoleModuleByRoleId(Long roleId) throws Exception;

    //删除角色记录。
    Boolean deleteRoleBackById(Long id) throws Exception;

    //删除用户角色关联记录。
    Boolean deleteRoleUserByRoleId(Long id) throws Exception;
}
