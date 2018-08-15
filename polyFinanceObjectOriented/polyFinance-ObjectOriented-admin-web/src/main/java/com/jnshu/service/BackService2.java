package com.jnshu.service;

import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.UserBack;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/8/6.
 */
@Component(value = "backService2")
public interface BackService2 {

    //账户列表
    List<DomainUserBack> getUserBacksByNameAndRole(UserBackListRPO rpo) throws Exception ;
    List<DomainUserBack> getUserBacksByNameAndRole2(UserBackListRPO rpo) throws Exception;

    //详情
    UserBack getUserBackById(Long id) throws Exception;

    //总数
    Integer getTotal() throws Exception;

    //更新
    Boolean updateUserBack(UserBack userBack) throws Exception;

    //删除
    Boolean deleteById(Long id) throws Exception;

    //新增
    Long saveUserBack(UserBack userBack) throws Exception;

    //获取用户角色id。
    Long getRoleIdByUserId(Long id) throws Exception;

    //通过登录名获取账户信息
    UserBack getUserBackByLoginName(String loginName) throws Exception;
}
