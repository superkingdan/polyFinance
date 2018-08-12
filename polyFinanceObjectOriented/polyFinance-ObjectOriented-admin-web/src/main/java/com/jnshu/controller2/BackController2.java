package com.jnshu.controller2;

import com.alibaba.fastjson.JSON;
import com.jnshu.Domain2.DomainModuleBackForLogin;
import com.jnshu.Domain2.DomainRoleBack;
import com.jnshu.Domain2.DomainRoleBackList;
import com.jnshu.Domain2.DomainUserBack;
import com.jnshu.dto2.UserBackListRPO;
import com.jnshu.entity.RoleBack;
import com.jnshu.entity.RoleModuleBack;
import com.jnshu.entity.RoleUserBack;
import com.jnshu.entity.UserBack;
import com.jnshu.exception.MyException;
import com.jnshu.service.BackService2;
import com.jnshu.service.RoleBackService2;
import com.jnshu.service.RoleUserBackService2;
import com.jnshu.utils.BackControllerVerify;
import com.jnshu.utils.DESUtil;
import com.jnshu.utils.RandomSalt;
import com.jnshu.utils.TokenUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//后台管理
@RestController
public class BackController2 {

    private static Logger logger = Logger.getLogger(BackController2.class);

    private static TokenUtil tokenUtil = new TokenUtil();

    @Autowired
    BackService2 backService2;

    @Autowired
    RoleBackService2 roleBackService2;

    @Autowired
    RoleUserBackService2 roleUserBackService2;

    @RequestMapping(value = "/a/u/managers",method = RequestMethod.GET)
    public Map<String, Object> getManagers(@ModelAttribute UserBackListRPO rpo, HttpServletRequest request, HttpServletResponse response){

        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据map。
        Map<String, Object> result = new HashMap<>();

        List<DomainUserBack> userBacks =null;
        Integer total = null;
        try {
            userBacks = backService2.getUserBacksByNameAndRole(rpo);
            total = backService2.getTotal();
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器在获取账户列表时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--账户列表。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+rpo);
            return result;
        }

        if ((("").equals(rpo.getLoginName()) && ("").equals(rpo.getRole())) &&null == userBacks){
            result.put("code",-1);
            result.put("message","错误。获取到账户列表。");
            return result;
        }

        if ((!("").equals(rpo.getLoginName()) || !("").equals(rpo.getRole())) && null == userBacks){
            result.put("code",0);
            result.put("message","当前条件，没有获取到账户列表。");
            return result;
        }

        result.put("code",0);
        result.put("message","成功获取账户列表。");
        result.put("total",userBacks.size());
        logger.info("后台 后台管理--账户列表成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+rpo);
        result.put("data", userBacks);
        return result;
    }

    //账户列表-详情
    @RequestMapping(value = "/a/u/managers/{id}",method = RequestMethod.GET)
    public Map<String, Object> getManager(
            @PathVariable long id, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据map。
        Map<String, Object> result = new HashMap<>();

        if (("").equals(id)){
            result.put("code",-1);
            result.put("message","id不能为空。");
            return result;
        }

        //id对应账户信息。
        UserBack userBack = null;
        Long roleId = null;
        //当前角色列表。
        List<DomainRoleBack> roleList = null;
        try {
            userBack = backService2.getUserBackById(id);
            roleId = backService2.getRoleIdByUserId(id);
            roleList = roleBackService2.getAll();
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器在获取账户详情时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--账户详情失败。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
            return result;
        }

        if (null == userBack){
            result.put("code",-1);
            result.put("message","当前id不存在对象。");
            return result;
        }

        if (null == roleList){
            result.put("code",-1);
            result.put("message","角色列表为空？");
            return result;
        }

        userBack.setHashKey(null);
        userBack.setSalt(null);
        result.put("code",0);
        result.put("message","查询成功。");
        logger.info("后台 后台管理--账户详情成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
        result.put("userBack",userBack);
        result.put("roleId",roleId);
        result.put("roleList",roleList);
        return result;
    }

    //账户列表-更新。更换角色和手机号。
    @RequestMapping(value = "/a/u/managers/{id}",method = RequestMethod.PUT)
    public Map<String, Object> updateManager(
            @PathVariable Long id,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Long roleId,
            HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据map。
        Map<String, Object> result = new HashMap<>();

        //参数校验。
        if (id<1 ){
            result.put("code",-1);
            result.put("message","错误参数");
            return result;
        }

        if (null != roleId && ("").equals(roleId)){
            result.put("code",-1);
            result.put("message","错误参数");
            return result;
        }

        //手机号不为空，更新user_back表手机号。
        UserBack userBack = new UserBack();
        if (null != phoneNumber && !("").equals(phoneNumber)){
            try {
                userBack = backService2.getUserBackById(id);
                if (null == userBack){
                    result.put("code",-1);
                    result.put("message","错误id");
                    return result;
                }

                userBack.setUpdateAt(System.currentTimeMillis());
                userBack.setUpdateBy((Long) account.get("uid"));
                userBack.setPhoneNumber(phoneNumber);

                Boolean x = false;
                x = backService2.updateUserBack(userBack);
                if (!x){
                    result.put("code",-1);
                    result.put("message","手机号更新失败。");
                    return result;
                }

                result.put("code",0);
                result.put("message","账户手机号更新成功。");
                logger.info("后台 后台管理--账户: "+id+"更新手机号成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
            } catch (Exception e) {
                result.put("code",-1);
                result.put("message","服务器错误。");
                result.put("errorMessage","服务器更新账户手机号时出错。");
                e.printStackTrace();
                logger.info("后台 后台管理--更新账户手机号时失败。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
                return result;
            }
        }

        //更新角色。
        if (null != roleId && !roleId.equals("")){
            //判断角色id
            Boolean x = false;
            try {
                List<DomainRoleBack> roleBacks = roleBackService2.getAll();
                for (DomainRoleBack roleBack : roleBacks){
                    if (roleBack.getId() == roleId){
                        x =true;
                        break;
                    }
                }

                Boolean y = false;
                if (x){
                    RoleUserBack roleUserBack = new RoleUserBack();
                    roleUserBack.setUpdateAt(System.currentTimeMillis());
                    roleUserBack.setUpdateBy((Long) account.get("uid"));
                    roleUserBack.setUserId(id);
                    roleUserBack.setRoleId(roleId);
                    Long userId = id;
                    y = roleUserBackService2.updateRoleUserBack(roleUserBack);

                    if (!y){
                        result.put("code",-1);
                        result.put("message","账户角色更新失败。");
                        return result;
                    }

                    result.put("code2",0);
                    result.put("message2","账户角色更新成功。");
                    logger.info("后台 后台管理--账户: "+id+"更新角色成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id+", 角色id: "+roleId);
                }

                if (!x){
                    result.put("code",-1);
                    result.put("message","角色id不存在。");
                    return result;
                }
            } catch (Exception e) {
                result.put("code",-1);
                result.put("message","服务器错误。");
                result.put("errorMessage","服务器更新账户角色时出错。");
                e.printStackTrace();
                logger.info("后台 后台管理--更新账户角色时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id+", 角色id: "+roleId);
                return result;
            }
        }

        return  result;
    }

    //账户列表-删除。
    @RequestMapping(value = "/a/u/managers/{id}",method = RequestMethod.DELETE)
    public Map<String, Object>  deleteManager(
            @PathVariable Long id,
            HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据map。
        Map<String, Object> result = new HashMap<>();

        //参数校验。

        if (id <1){
            result.put("code",0);
            result.put("message","账户id错误。");
            return result;
        }

        Boolean del = false;
        try {
            del = backService2.deleteById(id);
            if (!del){
                result.put("code", -1);
                result.put("message","id对应账户可能不存在。");
                return result;
            }

            result.put("code",0);
            result.put("message","账户删除成功。");

            Boolean del2 = false;
            del2 = roleUserBackService2.deleteRoleUserBackByUser(id);
            if (!del2){
                result.put("code", -1);
                result.put("message","id对应账户可能不存在。");
                return result;
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器删除账户或删除关联表时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--删除账户或删除关联表时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
            return result;
        }

        logger.info("后台 后台管理--删除账户和删除关联表成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);

        return result;
    }
    //账户新增
    @RequestMapping(value = "/a/u/managers/new"
            , method = RequestMethod.POST)
    public Map<String, Object>  saveManager(
            @RequestParam(required = false) String loginName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String hashKey,
            @RequestParam(required = false) String hashKey2,
            @RequestParam(required = false) Long roleId, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        //参数验证。
        if ((null == loginName || ("").equals(loginName)) && (null == phoneNumber || ("").equals(phoneNumber)) && (null == hashKey || ("").equals(hashKey)) && (null == request || ("").equals(request))){
            result.put("code3",-1);
            result.put("message3","loginName, phoneNumber, hashKey, roleId可能有空值。");
            return result;
        }
        //生成随机盐。
        RandomSalt randomSalt = new RandomSalt();
        String salt = randomSalt.getSalt(16);

        //生成hashkey。
        DESUtil desUtil = new DESUtil();

        String hashKey22 = null;
        try {
            hashKey2 = desUtil.encrypt(hashKey,salt);

            UserBack userBack = new UserBack();
            userBack.setCreateAt(System.currentTimeMillis());
            userBack.setCreateBy((Long) account.get("uid"));
            userBack.setLoginName(loginName);
            userBack.setPhoneNumber(phoneNumber);
            userBack.setSalt(salt);
            userBack.setHashKey(hashKey2);

            backService2.saveUserBack(userBack);


            Long userId = null;

            if (null !=(Long) userBack.getId()){
                userId= userBack.getId();
                RoleUserBack roleUserBack = new RoleUserBack();
                roleUserBack.setCreateAt(System.currentTimeMillis());
                roleUserBack.setCreateBy((Long) account.get("uid"));
                roleUserBack.setUserId(userId);
                roleUserBack.setRoleId(roleId);

                Long nid = roleUserBackService2.saveRoleUserBack(roleUserBack);
                if (null == nid){
                    result.put("code3",-1);
                    result.put("message3","新增用户角色关联失败。");
                    return result;
                }

                result.put("code3",0);
                result.put("message3","新增用户角色关联成功。");
            }
            logger.info("后台 后台管理--新增账户时成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： ");
        } catch (UnsupportedEncodingException e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器删除账户或删除关联表时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--新增账户时生成盐出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器新增账户时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--新增账户时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： ");
            return result;
        }

        result.put("code",0);
        result.put("message", "账户新增成功。");
        return result;
    }

    //更新密码
    @RequestMapping(value = "/a/u/roles/passwd",method = RequestMethod.PUT)
    public Map<String,Object>  updatePassword(@RequestParam(required = false) String hashKey,@RequestParam(required = false) String hashKey2,@RequestParam(required = false) String hashKey3,HttpServletRequest request, HttpServletResponse response) {
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        //新密码不能小于8位。
        if (null == hashKey || ("").equals(hashKey) || hashKey.length() < 8){
            result.put("code",-1);
            result.put("message","密码不能小于八位。");
            return result;
        }

        //生成hashkey。
        DESUtil desUtil = new DESUtil();

        //从数据库获取盐。
        UserBack userBack = null;
        try {
            userBack = backService2.getUserBackById((Long) account.get("uid"));
            String hashKey22 =desUtil.encrypt(hashKey, userBack.getSalt());
            userBack.setHashKey(hashKey2);
            userBack.setUpdateBy((Long) account.get("uid"));
            userBack.setUpdateAt(System.currentTimeMillis());

            //更新
            Boolean x = false;
            x = backService2.updateUserBack(userBack);

            if (!x){
                result.put("code",-1);
                result.put("message","密码更新失败。");
                return result;
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器更新账户密码时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--更新账户密码时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： ");
            return result;
        }

        result.put("code",0);
        result.put("message","密码更新成功。");
        return result;
    }

    //角色管理-角色列表
    @RequestMapping(value = "/a/u/roles",method = RequestMethod.GET)
    public Map<String, Object>  getRoles(
            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        List<DomainRoleBackList> roleBacks = null;

        try {
            roleBacks = roleBackService2.getRoleBacks();
            if (null == roleBacks){
                result.put("code",-1);
                result.put("message","角色列表为空。");
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器获取角色列表时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--获取角色列表时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }

        result.put("code",0);
        result.put("message","角色列表获取成功。");
        result.put("roleList",roleBacks);
        return result;
    }

    //角色管理-详情
    @RequestMapping(value = "/a/u/roles/{id}",method = RequestMethod.GET)
    public Map<String, Object>  getRole(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        if (id <1){
            result.put("code",-1);
            result.put("message","id错误。");
            return result;
        }
        String role = null;
        try {
            role = roleBackService2.getRoleNameById(id);
            if (null == role){
                result.put("code",-1);
                result.put("message","对应id没有记录。");
                return result;
            }

            //获取角色权限模块的id列表。
            List<Long> moduleIds = null;
            moduleIds = roleBackService2.getModuleIdListOfRole(id);

            if (null == moduleIds){
                result.put("code",-1);
                result.put("message","角色没有任何模块权限。");
                return result;
            }

            result.put("code",0);
            result.put("message","角色详情获取成功。");
            result.put("role",role);
            result.put("moduleIds",moduleIds);
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器获取角色详情时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--获取角色详情时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }

        logger.info("后台 后台管理--获取角色详情成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //角色管理-新增
    @RequestMapping(value = "/a/u/roles/new",method = RequestMethod.POST)
    public Map<String, Object> saveRole(@RequestParam(required = false) String role, @RequestParam(required = false) String moduleIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        //参数验证。
        try {
            result = BackControllerVerify.roleInsertReceive(role,moduleIds);
            if (null != result){
                return result;
            }
        } catch (Exception e) {
            throw new MyException(-1111, "参数异常。");
        }

        //将角色对应的模块id list 转化为List。
        List<Long> inputModuleIds = JSON.parseArray(moduleIds,Long.class);

        System.out.println("*(*(*(*(*(*(*(*(*(");
        System.out.println(inputModuleIds);
        System.out.println(inputModuleIds.toString());
        System.out.println(inputModuleIds.get(1));

        //新增角色。
        //查询是否已经存在。
        Long roleId = null;
        try {
            roleId = roleBackService2.getRoleIdByRole(role);
            List<Long> list = roleBackService2.getAllModuleIds();

            //验证角色是否已经存在，同时验证传进来的模块id是否是数据库中的数据。
            Map<String, Object> veri =null;
            try {
                 veri = BackControllerVerify.roleInsert(role,moduleIds,roleId,list,account);
            }catch (Exception e){
                throw new MyException(-1111, "参数异常。");
            }

            if (veri != null){
                return veri;
            }

            //新增角色。
            RoleBack roleBack = new RoleBack();
            roleBack.setCreateAt(System.currentTimeMillis());
            roleBack.setCreateBy((Long) account.get("uid"));
            roleBack.setRole(role);

            roleBackService2.saveRoleBack(roleBack);
            roleId = roleBack.getId();
            if (roleId == null){
                result.put("code",-1);
                result.put("message","角色新增出错。");
                return result;
            }

            //角色模块关联表。
            for (Long moduleId : inputModuleIds){
                RoleModuleBack roleModuleBack = new RoleModuleBack();
                roleModuleBack.setCreateAt(System.currentTimeMillis());
                roleModuleBack.setCreateBy((Long) account.get("uid"));
                roleModuleBack.setModuleId(moduleId);
                roleModuleBack.setRoleId(roleId);

                //新增角色模块关联记录。
                roleBackService2.saveRoleModule(roleModuleBack);
                if (null ==(Long) roleModuleBack.getModuleId()){
                    result.put("code"+moduleId,-1);
                    result.put("message"+moduleId,"模块id为："+moduleId+"记录新增失败。");
                }
            }

            result.put("code",0);
            result.put("message","角色新增成功。");
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器角色新增时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--角色新增时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }
        logger.info("后台 后台管理--角色新增成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //角色管理-更新
    @RequestMapping(value = "/a/u/roles/{id}",method = RequestMethod.PUT)
    public Map<String, Object>  updateRole(@PathVariable(required = false) Long id, @RequestParam(required = false) String moduleIds, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        //验证参数。
        if (null == id || id < 1){
            result.put("code",-1);
            result.put("message","id不能小于1");
            return result;
        }

        if ((null == moduleIds || ("").equals(moduleIds))) {
            result.put("code",-1);
            result.put("message","moduleIds不能为空或没有值。");
            return result;
        }


        Boolean x = false;
        try {

            //验证moduleIds。
            List<Long> inputModuleIds = JSON.parseArray(moduleIds,Long.class);
            List<Long> list = roleBackService2.getAllModuleIds();

            if (list == null){
                result.put("code",-1);
                result.put("message","获取模块列表出错。");
                logger.info("后台 后台管理--角色新增时获取全部模块id列表出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            }

            for (Long id1 :inputModuleIds){
                if (!list.contains(id1)){
                    result.put("code",-1);
                    result.put("message","部分 模块id错误。");
                    return result;
                }
            }

            //验证不重复。
            for (Long id1 : inputModuleIds){
                int times = 0;
                for (Long id2 : inputModuleIds){
                    if (id == id2){
                        times++;
                    }
                }

                if (times > 1){
                    result.put("code",-1);
                    result.put("message","模块 id不能重复。");
                    return result;
                }
            }

            //删除旧的角色模块旧的记录。
            x = roleBackService2.deleteRoleModuleByRoleId(id);

            if (!x){
                result.put("code",-1);
                result.put("message","角色更新时删除旧关联记录时出错。");
                return result;
            }

            System.out.println(moduleIds);
            //角色模块关联表。
            for (Long moduleId : inputModuleIds){
                RoleModuleBack roleModuleBack = new RoleModuleBack();
                roleModuleBack.setCreateAt(System.currentTimeMillis());
                roleModuleBack.setCreateBy((Long) account.get("uid"));
                roleModuleBack.setModuleId(moduleId);
                roleModuleBack.setRoleId(id);

                roleBackService2.saveRoleModule(roleModuleBack);
                moduleId = roleModuleBack.getModuleId();
                if (null == moduleId){
                    result.put("code"+moduleId,-1);
                    result.put("message"+moduleId,"模块id为："+moduleId+"关联记录新增失败。");
                }
            }

            result.put("code",0);
            result.put("message","角色更新成功。");
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器角色更新时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--角色更新时出错（删除旧的角色模块关联记录或者是新增时出错。）。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }
        logger.info("后台 后台管理--角色更新成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));

        return result;
    }

    //角色管理-删除
    @RequestMapping(value = "/a/u/roles/{id}",method = RequestMethod.DELETE)
    public Map<String, Object>  deleteRole(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response){
        //登录用户信息。
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //返回数据List。改成map。
        Map<String, Object> result = new HashMap<>();

        if (id<1){
            result.put("code",-1);
            result.put("message","id错误。");
        }

        Boolean x = false;
        try {
            //删除角色记录。
            x = roleBackService2.deleteRoleBackById(id);

            if (!x){
                result.put("code1",-1);
                result.put("message1","id错误或者角色记录已经删除。");
            }

            //删除角色模块管理记录。
            x = roleBackService2.deleteRoleModuleByRoleId(id);
            if (!x){
                result.put("code2",-1);
                result.put("message2","id错误或者角色模块关联记录已经删除。");
            }

            //删除角色用户关联记录。
            x = roleBackService2.deleteRoleUserByRoleId(id);
            if (!x){
                result.put("code3",-1);
                result.put("message3","id错误或者角色用户关联记录已经删除。");
            }
            result.put("code",0);
            result.put("message","角色记录删除成功。");
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器角色删除时出错。");
            e.printStackTrace();
            logger.info("后台 后台管理--角色删除时出错（删除角色模块关联记录或者是删除据说记录时出错。）。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }
        logger.info("后台 后台管理--角色删除时成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }
//
//
//
//    //模块管理-模块列表
//    @RequestMapping(value = "/a/u/modules",method = RequestMethod.GET)
//    public List<Object> getModules(
//            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
//            @RequestParam(required = false,defaultValue = "10") Integer pageSize){
//        List<Object> result = new ArrayList<>();
//        List<Map> modules = new ArrayList<>();
//        for (int s=1;s<pageSize;s++){
//            Map<String,Object> module = new HashMap<>();
//            module.put("id", s);
//            module.put("moduleName", "用户管理");
//            module.put("moduleUrl", "www.d");
//            module.put("superId", s+8);
//            module.put("moduleType", "WEB");
//            module.put("updateAt", System.currentTimeMillis());
//            module.put("id2", 3);
//            module.put("createAt",System.currentTimeMillis() );
//            module.put("id3", 9);
//            modules.add(module);
//        }
//        result.add(cam);
//        Map<String,Integer> t= new HashMap<>();
//        t.put("total", 200);
//        result.add(modules);
//        return result;
//    }
//
//    //模块管理-模块详情
//    @RequestMapping(value = "/a/u/modules/{id}",method = RequestMethod.GET)
//    public List<Object> getModule(@PathVariable Long id){
//        List<Object> result = new ArrayList<>();
//            Map<String,Object> module = new HashMap<>();
//            module.put("id", id);
//            module.put("moduleName", "用户管理");
//            module.put("moduleUrl", "www.d");
//            module.put("superId", id+8);
//            module.put("moduleType", "WEB");
//            module.put("updateAt", System.currentTimeMillis());
//            module.put("id2", 3);
//            module.put("createAt",System.currentTimeMillis() );
//            module.put("id3", 9);
//        result.add(cam);
//        result.add(module);
//        return result;
//    }
//
//    //模块管理-模块更新
//    @RequestMapping(value = "/a/u/modules/{id}",method = RequestMethod.PUT)
//    public List<Object> updateModule(@PathVariable Long id){
//        List<Object> result = new ArrayList<>();
//        result.add(cam);
//        return result;
//    }
//
//    //模块管理-模块删除
//    @RequestMapping(value = "/a/u/modules/{id}",method = RequestMethod.DELETE)
//    public List<Object> deleteModule(@PathVariable Long id){
//        List<Object> result = new ArrayList<>();
//        result.add(cam);
//        return result;
//    }
//
//    //模块管理-模块新增
//    @RequestMapping(value = "/a/u/modules/new",method = RequestMethod.POST)
//    public List<Object> saveModule(
//            @RequestParam(required = false) String moduleName,
//            @RequestParam(required = false) String menuId,
//            @RequestParam(required = false) String moduleUrl,
//            @RequestParam(required = false) String superId,
//            @RequestParam(required = false) String moduleType
//    ){
//        List<Object> result = new ArrayList<>();
//        result.add(cam);
//        return result;
//    }
}
