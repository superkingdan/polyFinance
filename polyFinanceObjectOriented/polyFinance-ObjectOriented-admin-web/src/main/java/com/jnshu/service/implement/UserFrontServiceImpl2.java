package com.jnshu.service.implement;

import com.github.pagehelper.PageHelper;
import com.jnshu.Domain2.DomainUserFront;
import com.jnshu.Domain2.DomainUserFrontDetail;
import com.jnshu.Domain2.UserBankCard;
import com.jnshu.dao2.UserFrontMapper2;
import com.jnshu.dto2.UserFrontListRPO;
import com.jnshu.entity.User;
import com.jnshu.service.UserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Component(value = "userService2")
public class UserFrontServiceImpl2 implements UserService2 {

    @Autowired
    UserFrontMapper2 userFrontMapper2;
    //用户列表
    @Override
    public List<DomainUserFront> getAllUser(int pageNum, int pageSize, UserFrontListRPO userFrontListRPO) throws Exception {
        List<DomainUserFront> users = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        users= userFrontMapper2.getUserFrontList(userFrontListRPO);
        return users;
    }

    @Override
    public List<DomainUserFront> getAllUser2(UserFrontListRPO userFrontListRPO) throws Exception {
        List<DomainUserFront> users = new ArrayList<>();
        users= userFrontMapper2.getUserFrontList(userFrontListRPO);
        return users;
    }
    //用户列表--总数
    @Override
    public Integer getCount() throws Exception {
        return userFrontMapper2.getTotal();
    }

    //用户详情--id
    @Override
    public DomainUserFrontDetail getUserFrontById(Long id) throws Exception{
        return userFrontMapper2.getUserFrontDetailById(id);
    }

    //获取用户详情时同时获取银行卡
    @Override
    public List<UserBankCard> getUserFrontBankCardsById(Long id) throws Exception {
        return userFrontMapper2.getUserFrontBankCardsById(id);
    }

    //用户冻结-解冻
    @Override
    public Boolean updateUserStatus(User user) throws Exception {
        return userFrontMapper2.updateUserStatus(user);
    }

    //更新用户手机号
    @Override
    public Boolean updateUserPhone(User user) throws Exception {
        return userFrontMapper2.updateUserFrontPhone(user);
    }

    //更换用户产品经理。
    @Override
    public Boolean updateUserFrontReferrerId(User user) throws Exception {
        return userFrontMapper2.updateUserFrontReferrerId(user);
    }

    //取消用户实名，同时删除银行卡
    @Override
    public Boolean updateUserFrontRealStatus(User user) throws Exception {
        return userFrontMapper2.updateUserFrontRealStatus(user);
    }

    @Override
    public Integer deleteUserBankCard(Long id) throws Exception{
        return userFrontMapper2.deleteUserBankCard(id);
    }

    //用户详情--解绑银行卡
    @Override
    public Boolean untiedUserBankCard(Long id, Long bankId) throws Exception {
        return userFrontMapper2.untiedUserBankCard(id, bankId);
    }

    @Override
    public Boolean updateUserDefaultBankCard(User user) throws Exception{
        return userFrontMapper2.updateUserDefaultBankCard(user);
    }
}
