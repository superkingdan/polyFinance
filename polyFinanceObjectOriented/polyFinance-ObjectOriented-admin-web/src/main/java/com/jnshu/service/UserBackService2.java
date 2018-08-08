package com.jnshu.service;



import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.UserBack;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component(value = "userBackService2")
public interface UserBackService2 {

    //后台账户验证。
    Object verifyUserBack(UserBack userBack, HttpServletRequest request, HttpServletResponse response) throws Exception;
    //账户列表
    List<DomainUserBack> getUserBackList(UserBackListRPO rpo) throws Exception;

    //新增账户
    Long saveUserBack(UserBack userBack) throws Exception;

}