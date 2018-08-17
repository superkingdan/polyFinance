package com.jnshu.controller2;

import com.jnshu.Domain2.DomainUserFront;
import com.jnshu.Domain2.DomainUserFrontDetail;
import com.jnshu.Domain2.UserBankCard;
import com.jnshu.dto2.UserFrontListRPO;
import com.jnshu.service.UserService2;
import com.jnshu.utils.CAM;
import com.jnshu.utils.TokenUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FrontUserController2 {

    private static Logger logger = Logger.getLogger(FrontUserController2.class);

    private static TokenUtil tokenUtil = new TokenUtil();
    @Autowired
    UserService2 userService2;

    /**
     * 用户管理
     */
    //用户列表
    @RequestMapping(value = "/a/u/users",method = RequestMethod.GET)
    public Object userList(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize,
                           @ModelAttribute UserFrontListRPO userFrontListRPO,
                           HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);

        //返回数据List。
        List<Object> result = new ArrayList<>();

        List<DomainUserFront> users = null;
        Integer total =null;

        try {
            users = userService2.getAllUser(pageNum, pageSize,userFrontListRPO);
            total = userService2.getAllUser2(userFrontListRPO).size();
            logger.info("后台 业务管理--用户列表。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+userFrontListRPO);
        } catch (Exception e) {
            cam.setCode(-1);
            cam.setErrorMessage("获取用户列表失败。");
            logger.info("获取用户列表失败。账户id="+account.get("uid"));
            e.printStackTrace();
            result.add(cam);
            return result;
        }

        Map<String,Integer> s = new HashMap<>();
        s.put("total", total);
        s.put("pageNum", pageNum);
        s.put("pageSize", pageSize);
        result.add(s);

        if (0 == users.size()){
            cam.setCode(-1);
            cam.setErrorMessage("没有符合条件记录。");
            result.add(cam);
            return result;
        }
        result.add(cam);
        result.add(users);
        return result;
    }

    //用户详情
    @RequestMapping(value = "/a/u/users/{id}",method = RequestMethod.GET)
    public Object userInfo(@PathVariable long id, HttpServletRequest request,HttpServletResponse response){
        CAM cam = new CAM();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        List<Object> result = new ArrayList<>();
        DomainUserFrontDetail user =null;
        try {
             user = userService2.getUserFrontById(id);
            if (null == user){
                cam.setCode(-1);
                cam.setErrorMessage("id超出范围。");
                result.add(cam);
            }
        } catch (Exception e) {
            cam.setCode(-1);
            cam.setErrorMessage("后端获取id="+id+", 时出错。请重试。");
            logger.info("用户详情。服务器获取id="+id+"详情出错错误。账户id="+account.get("uid")+", 被操作用户id="+id);
            e.printStackTrace();
            return result;
        }

        //获取银行卡
        List<UserBankCard> bankCards=null;
        try {
            bankCards = userService2.getUserFrontBankCardsById(id);
            if (0 == bankCards.size()){
                cam.setMessage("该用户没有银行卡");
            }
            result.add(cam);
        } catch (Exception e) {
            cam.setErrorMessage("服务器获取id="+id+"银行卡出错错误");
            logger.info("用户详情。服务器获取id="+id+"银行卡出错错误。账户id="+account.get("uid")+", 被操作用户id="+id);
            e.printStackTrace();
            result.add(cam);
            return result;
        }

        result.add(user);
        result.add(bankCards);
        logger.info("后台 业务管理--用户详情。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数： "+id);
        return result;
    }

    //用户列表-冻结/解冻
    @RequestMapping(value = "/a/u/users/{id}/status",method = RequestMethod.PUT)
    public Object userStatus(@PathVariable BigDecimal id, @RequestParam(required = false) Integer status,HttpServletRequest request,HttpServletResponse response){

        CAM cam = new CAM();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);
        List<Object> result = new ArrayList<>();


        //参数验证
        if (null == status || status.equals(0)){
            cam.setCode(-1);
            cam.setErrorMessage("status 不能为空。");
            result.add(cam);
            return result;
        }

        if (0 != status && 1 !=status){
            cam.setCode(-1);
            cam.setErrorMessage("status值错误。");
            result.add(cam);
            return result;
        }

        //业务处理
        com.jnshu.entity.User user = new com.jnshu.entity.User();
        user.setId(id.longValue());
        user.setStatus(status);
        user.setUpdateBy((Long) account.get("uid"));
        user.setUpdateAt(System.currentTimeMillis());
        String sta = null;
        try {
            if (userService2.updateUserStatus(user)){
                if (status ==0) {
                    cam.setMessage("解冻成功。");
                    sta = "解冻";
                }
                if (status ==1){
                    cam.setMessage("冻结成功。");
                    sta = "冻结";
                }
            }else {
                cam.setCode(-1);
                cam.setErrorMessage("id对应记录不存在或已经是要修改的状态。");
            }
            logger.info("后台 业务管理--用户冻结-解冻。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。请求参数id= "+id+"，操作="+sta+"成功。");

            result.add(cam);
            return result;

        } catch (Exception e) {
            cam.setCode(-1);
            cam.setErrorMessage("服务器错误。");
            logger.info("用户详情--"+sta+"失败。服务器出错。账户id="+account.get("uid")+", 被操作用户id="+id);
            result.add(cam);
            e.printStackTrace();
            return result;
        }
    }

    //修改手机
    @RequestMapping(value = "/a/u/users/{id}/phone",method = RequestMethod.PUT)
    public Object userPhone(@PathVariable long id, @RequestParam String phoneNumber,HttpServletRequest request, HttpServletResponse response){
        CAM cam = new CAM();
        List<Object> result = new ArrayList<>();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //参数验证。
        if (null == phoneNumber){
            cam.setCode(-1);
            cam.setErrorMessage("手机号不能为空。");
        }

        if (!phoneNumber.matches("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")){
            cam.setCode(-1);
            cam.setMessage("null");
            cam.setErrorMessage("请正确填写手机号。");
        }

        //业务处理
        com.jnshu.entity.User user = new com.jnshu.entity.User();
        user.setId(id);
        user.setPhoneNumber(phoneNumber);
        user.setUpdateBy((Long) account.get("uid"));
        user.setUpdateAt(System.currentTimeMillis());

        try {

            Boolean x =userService2.updateUserPhone(user);
            if (!x){
                cam.setCode(-1);
                cam.setMessage("修改失败。");
                cam.setErrorMessage("id对应记录不存在或手机号相同");
                result.add(cam);
                return result;
            }
        } catch (Exception e) {
            cam.setErrorMessage("服务器修改手机号出错。");
            e.printStackTrace();
            result.add(cam);
            logger.info("服务器修改手机号出错。账户id="+account.get("uid")+", 被操作用户id="+id);
            return result;
        }

        cam.setMessage("手机号修改成功。");
        result.add(cam);
        logger.info("后台 业务管理--用户详情-修改手机。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。修改成功。请求参数id= "+id+"，phoneNumber="+phoneNumber +"，修改成功。");
        return result;
    }

    //修改理财经理
    @RequestMapping(value = "/a/u/users/{id}/referrer",method = RequestMethod.PUT)
    public Object userReferrer(@PathVariable long id, @RequestParam(required = false) String referrerId,HttpServletRequest request, HttpServletResponse response){
        CAM cam = new CAM();
        List<Object> result = new ArrayList<>();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //参数验证
        if (null == referrerId || ("").equals(referrerId)){
            cam.setCode(-1);
            cam.setErrorMessage("referrerId 不能为空。");
            result.add(cam);
            return result;
        }

        //业务处理
        com.jnshu.entity.User user = new com.jnshu.entity.User();
        user.setId(id);
        user.setReferrerId(referrerId);
        user.setUpdateBy((Long) account.get("uid"));
        user.setUpdateAt(System.currentTimeMillis());
        Boolean x =false;
        try {
            x = userService2.updateUserFrontReferrerId(user);
            System.out.println(user);
            System.out.println(x);

            if (x){
                cam.setMessage("修改成功。");
            }

            if(!x){
                cam.setCode(-1);
                cam.setErrorMessage("修改失败。理财经理工号相同。或id对应记录不存在。");
                logger.info("后台 业务管理--用户详情-更换理财经理。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。"+"用户id="+id+", 的理财经理工号修改失败。");
                result.add(cam);
            }
        } catch (Exception e) {
            cam.setErrorMessage("服务器修改理财经理出错。");
            logger.info("服务器修改理财经理出错。账户id="+account.get("uid")+", 被操作用户id="+id);
            e.printStackTrace();
        }


        result.add(cam);
        logger.info("后台 业务管理--用户详情-更换理财经理。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。"+"用户id="+id+", 的理财经理工号修改成功。");
        return result;
    }

    //取消实名
    @RequestMapping(value = "/a/u/users/{id}/realStatus",method = RequestMethod.PUT)
    public Object userRealStatus(@PathVariable long id, @RequestParam(required = false) Integer realStatus,HttpServletRequest request, HttpServletResponse response){
        CAM cam = new CAM();
        List<Object> result = new ArrayList<>();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //参数验证
        if (null == realStatus || ("").equals(realStatus)){
            cam.setCode(-1);
            cam.setErrorMessage("realStatus不能为空。");
            result.add(cam);
            return result;
        }

        if (0 != realStatus){
            cam.setCode(-1);
            cam.setErrorMessage("非法参数。滚蛋。");
            result.add(cam);
            return result;
        }

        //业务处理
        com.jnshu.entity.User user = new com.jnshu.entity.User();
        user.setId(id);
        user.setRealStatus(realStatus);
        user.setUpdateBy((Long) account.get("uid"));
        user.setUpdateAt(System.currentTimeMillis());

        Boolean cancelReal = false;
        try {
            System.out.println(user);
            System.out.println(userService2);
            cancelReal = userService2.updateUserFrontRealStatus(user);
            if (cancelReal){
                cam.setMessage("取消实名成功。");
                result.add(cam);
                logger.info("后台 业务管理--用户详情-取消实名。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。"+"用户id="+id+", 实名取消成功。");
            }else {
                cam.setMessage("id不存在或者本身未实名。");
                result.add(cam);
            }
        } catch (Exception e) {
            cam.setErrorMessage("服务器取消用户实名出错。");
            logger.info("服务器取消用户实名出错。账户id="+account.get("uid")+", 被操作用户id="+id);
            e.printStackTrace();
            result.add(cam);
            return result;
        }

        if (cancelReal){
            try {
                Integer deleteCardNum = userService2.deleteUserBankCard(user.getId());
                CAM cam1 = new CAM();
                if (0 == deleteCardNum){
                    cam1.setMessage("当前用户没有银行卡。");
                }else {
                    cam1.setMessage("删除"+deleteCardNum+"张银行卡。");
                    logger.info("后台 业务管理--用户详情-取消实名。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。"+"用户id="+id+", 实名取消成功。"+"并取消银行卡。");
                }
                result.add(cam1);
            } catch (Exception e) {
                cam.setCode(-1);
                cam.setMessage("服务器错误。");
                cam.setErrorMessage("取消实名时，服务器删除id:"+id + "用户的银行卡时出错。");
                logger.info("取消实名时，服务器删除id:"+id + "用户的银行卡时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。");
                e.printStackTrace();
                result.add(cam);
                return result;
            }
        }
        return result;
    }

    /**
     * 解绑银行卡
     * @param id
     * @param defaultCard
     * @param bankId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/a/u/users/{id}/bankCard",method = RequestMethod.PUT)
    public Object userBankCard(@PathVariable long id,@RequestParam(required = false) Long defaultCard , @RequestParam(required = false) Long bankId,HttpServletRequest request, HttpServletResponse response){
        CAM cam = new CAM();
        List<Object> result = new ArrayList<>();
        Map<String, Object> account = new HashMap<>();
        account = tokenUtil.getAccount(request);

        //参数验证
        if ((null == defaultCard || ("").equals(defaultCard)) || (null == bankId || ("").equals(bankId))){
            cam.setCode(-1);
            cam.setErrorMessage("bankId或defaultCard不能为空。");
            result.add(cam);
            return result;
        }

        //业务处理
        //获得用户信息，供后面使用。
        DomainUserFrontDetail user =null;
        try {
            user = userService2.getUserFrontById(id);
            if (null == user){
                cam.setCode(-1);
                cam.setMessage("id错误");
                cam.setErrorMessage("没有当前用户。");
                result.add(cam);
                return result;
            }
        } catch (Exception e) {
            cam.setCode(-1);
            cam.setMessage("服务器错误。");
            cam.setErrorMessage("解绑银行卡时，服务器获取用户id为："+id + "信息时出错。");
            logger.info("解绑银行卡时，服务器删除id:"+id + "用户的银行卡时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。");
            e.printStackTrace();
            result.add(cam);
            return result;
        }

        System.out.println(user.getDefaultCard());
        System.out.println(defaultCard);
        System.out.println((long) defaultCard != (long) (user.getDefaultCard()));

        //比较数据库用户默认银行卡和前端传来的id是否相同。如果不同直接返回。
        if (user.getDefaultCard() == null){
            cam.setCode(-1);
            cam.setMessage("defaultCard为空。");
            cam.setErrorMessage("当前用户没有默认银行卡。");
            result.add(cam);
            return result;
        }

        if ((long) defaultCard != (long) (user.getDefaultCard())){
            cam.setCode(-1);
            cam.setMessage("defaultCard错误");
            cam.setErrorMessage("非当前用户默认银行卡。");
            result.add(cam);
            return result;
        }

        //获取用户全部银行卡，如果是为0，直接返回。如果为1，需要设置默认银行卡为空。如果为2，需要按情况设置默认银行卡。
        List<UserBankCard> bankCards=null;
        //新的默认银行卡id。
        Long newDefaultBankCardId = null;
        try {
            bankCards = userService2.getUserFrontBankCardsById(id);
            if (0 == bankCards.size()){
                CAM cam1 = new CAM();
                cam.setCode(-1);
                cam.setMessage("该用户没有绑定银行卡。");
                result.add(cam);
                return result;
            }

            if (1 == bankCards.size()){
                newDefaultBankCardId = null;
            }

            if (2 == bankCards.size()){
                for (UserBankCard card : bankCards){
                    //设置新的默认银行卡为不被删除的银行卡id。至于是不是与原来的相同，不关注。
                    if ((long) bankId != card.getCardId()){
                        newDefaultBankCardId = card.getCardId();
                    }
                }
            }

            if (2 < bankCards.size()){
                CAM cam1 = new CAM();
                cam.setCode(-1);
                cam.setMessage("严重错误，用户银行卡超过2张。");
                result.add(cam);
                return result;
            }

            System.out.println("newDefaultBankCardId："+newDefaultBankCardId);
        } catch (Exception e) {
            cam.setErrorMessage("服务器获取id="+id+"银行卡出错错误");
            logger.info("解绑银行卡。服务器获取id="+id+"银行卡出错错误。账户id="+account.get("uid")+", 被操作用户id="+id);
            e.printStackTrace();
            result.add(cam);
            return result;
        }

        //解绑银行卡结果。只对银行卡表做删除操作。
        Boolean up = false;
        try {
            up = userService2.untiedUserBankCard(id, bankId);
            System.out.println(up);
            if (!up){
                cam.setCode(-1);
                cam.setMessage("解绑失败");
                cam.setErrorMessage("银行卡或许已经不存。");
                result.add(cam);
                return result;
            }else {
                CAM cam2 = new CAM();
                cam2.setCode(0);
                cam2.setMessage("解绑成功");
                result.add(cam2);
                logger.info("解绑银行卡成功。用户id="+id+"，的银行卡id="+bankId+"被当前账户id="+account.get("uid")+"，登录名="+account.get("loginName")+", 角色为："+account.get("role")+"，的后台账户解绑。");
            }
        } catch (Exception e) {
            e.printStackTrace();
            cam.setCode(-1);
            cam.setMessage("服务器错误。");
            cam.setErrorMessage("解绑银行卡时服务器发生错误。");
            logger.info("解绑银行卡时，服务器删除id:"+id + "用户的银行卡时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。");
            result.add(cam);

        }


        //如果要删掉的银行卡是默认银行卡，需要重新设置默认银行卡。
        if ((long) bankId == user.getDefaultCard()){
            com.jnshu.entity.User user1 = new com.jnshu.entity.User();
            user1.setUpdateBy((Long) account.get("uid"));
            user1.setUpdateAt(System.currentTimeMillis());
            user1.setId(id);

            user1.setDefaultCard(newDefaultBankCardId);
            System.out.println("更新默认银行卡》》》》"+user1);
            try {
                if (userService2.updateUserDefaultBankCard(user1)){
                    cam.setCode(0);
                    cam.setMessage("解绑后更新默认银行卡成功。");
                    result.add(cam);
                    logger.info("解绑后更新默认银行卡成功。用户id="+id+"，的银行卡id="+bankId+"被当前账户id="+account.get("uid")+"，登录名="+account.get("loginName")+", 角色为："+account.get("role")+"，的后台账户解绑。并成功更新默认银行卡。");
                }
            } catch (Exception e) {
                CAM cam1 = new CAM();
                cam1.setCode(-1);
                cam1.setMessage("服务器错误。");
                cam1.setErrorMessage("解绑银行卡后更新默认银行卡时服务器发生错误。");
                logger.info("解绑银行卡后更新默认银行卡时，服务器更新默认银行卡id:"+id + "时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role")+"。");
                result.add(cam1);
            }
        }

        return result;
    }
}
