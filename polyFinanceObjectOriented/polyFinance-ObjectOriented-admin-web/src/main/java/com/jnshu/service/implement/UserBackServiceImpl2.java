package com.jnshu.service.implement;

import com.github.pagehelper.PageHelper;
import com.jnshu.Domain2.DomainModuleBackForLogin;
import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dao2.RoleBackMapper2;
import com.jnshu.dao2.RoleModuleBackMapper2;
import com.jnshu.dao2.UserBackMapper2;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.RoleBack;
import com.jnshu.entity.UserBack;
import com.jnshu.service.UserBackService2;
import com.jnshu.utils.CAM;
import com.jnshu.utils.DESUtil;
import com.jnshu.utils.TokenUtil;
import com.jnshu.utils.formodule.ModuleBackTree;
import com.jnshu.utils.formodule.ModuleBackTreeInit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@Service
@Component
public class UserBackServiceImpl2 implements UserBackService2 {

    private static Logger logger = Logger.getLogger(UserBackServiceImpl2.class);

    @Autowired
    UserBackMapper2 userBackMapper2;
    @Autowired
    RoleBackMapper2 roleBackMapper2;
    @Autowired
    RoleModuleBackMapper2 roleModuleBackMapper2;

    @Override
    public Object verifyUserBack(UserBack userBack, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Object> result = new ArrayList<>();
        Map<String,Object> v = new HashMap<>();
        CAM cam = new CAM();
        //通过用户名获得数据库中用户信息。
        UserBack userBackData = userBackMapper2.getUserBackByLoginName(userBack.getLoginName());
        if (null == userBackData){
            cam.setCode(-1);
            cam.setMessage("账户不存在");
            result.add(cam);
            return result;
        }

        //判断密码是否正确。
        DESUtil desUtil = new DESUtil();
        String pw = userBackData.getHashKey();
        String enLoginPassword = desUtil.encrypt(userBack.getHashKey(), userBackData.getSalt());
        if (pw.equals(enLoginPassword)){
            //获得角色名
            RoleBack role = roleBackMapper2.getRoleByUserId(userBackData.getId());
            //获得模块权限。
            List<DomainModuleBackForLogin> moduleBacks = roleModuleBackMapper2.getModuleOfRole(role.getId());
            List<ModuleBackTree> returnModules= new ModuleBackTreeInit().get(moduleBacks);

            cam.setCode(0);
            cam.setMessage("登录成功");
            v.put("uid", userBackData.getId());
            v.put("loginName", userBackData.getLoginName());
            v.put("role", role.getRole());

            result.add(cam);
            result.add(v);
            result.add(returnModules);

            //添加token和uid的cookie
            TokenUtil tokenUtil = new TokenUtil();
            String token = tokenUtil.createToken(userBackData.getId(),userBackData.getLoginName(), role.getRole());

            //uid的cookie。
            Cookie cookie= tokenUtil.createCookie(userBackData.getId());
            response.addCookie(cookie);

            //token的cookie。
            Cookie cookie1 = tokenUtil.createCookie2(token);
            response.addCookie(cookie1);


            logger.info("时间："+new Timestamp(new Date().getTime())+"。后台账户："+userBack.getLoginName()+", 管理角色："+role.getRole()+"。对应模块权限： "+"\n"+returnModules.toString());
            return result;
        }

        Map<String,Object> cam1 = new HashMap<>();
        cam1.put("code",10001);
        cam1.put("message","请重新登录。");
        return cam1;
    }


    @Override
    public List<DomainUserBack> getUserBackList(UserBackListRPO rpo) throws Exception {
        PageHelper.startPage(rpo.getPageNum(), rpo.getPageSize());
        List<DomainUserBack> userBacks = userBackMapper2.getUserBacksByNameAndRole(rpo);
        return userBacks;
    }

    @Override
    public Long saveUserBack(UserBack userBack) throws Exception {
        //新增账户，需要新增user_back,role_user_back,role_back三张表。
        Long createDate = System.currentTimeMillis();
        userBack.setCreateAt(createDate);
        userBack.setUpdateAt(createDate);
        //获得id作为createBy,updateBy

        //生成salt，并保存到userBack.salt中。

        //用salt加密userBack.getHashKey后再保存到userBack.hashKey中。

        //保存到数据库
        userBackMapper2.saveUserBack(userBack);


        return userBack.getId();
    }
}
