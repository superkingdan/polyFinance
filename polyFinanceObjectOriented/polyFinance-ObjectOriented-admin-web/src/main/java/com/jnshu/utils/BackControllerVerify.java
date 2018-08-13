package com.jnshu.utils;

import com.alibaba.fastjson.JSON;
import com.jnshu.controller2.BackController2;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/9.
 */
public class BackControllerVerify {

    private static Logger logger = Logger.getLogger(BackControllerVerify.class);

    public static Map<String, Object> roleInsertReceive(String role,String moduleIds) throws Exception{
        Map<String, Object> result = new HashMap<>();
        if ((null == role || ("").equals(role)) || (null == moduleIds || ("").equals(moduleIds))){
            result.put("code",-1);
            result.put("message","role和moduleIds不能为空。");
            return result;
        }
        return null;
    }

    public static Map<String, Object> roleInsert(String role, String moduleIds,Long roleId, List<Long> list,Map<String, Object> account) throws Exception{
        Map<String, Object> result = new HashMap<>();

        //判断moduleIds格式是否正确。
        if (!moduleIds.contains("[") || !moduleIds.contains("]")){
            result.put("code",-1);
            result.put("message","moduleIds格式不对，需要数组[]。");
            return result;
        }
        //将角色对应的模块id list 转化为List。
        List<Long> inputModuleIds = JSON.parseArray(moduleIds,Long.class);

        System.out.println("*(*(*(*(*(*(*(*(*(");
        System.out.println(inputModuleIds);
        System.out.println(inputModuleIds.toString());
        System.out.println(inputModuleIds.get(1));

        //验证角色是否存在。
        if (null != roleId){
            result.put("code",-1);
            result.put("message","角色名已存在，换一个。");
            return result;
        }

        if (list == null){
            result.put("code",-1);
            result.put("message","获取模块列表出错。");
            logger.info("后台 后台管理--角色新增时获取全部模块id列表出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        }

        for (Long id :inputModuleIds){
            if (!list.contains(id)){
                result.put("code",-1);
                result.put("message","部分模块id错误。");
                return result;
            }
        }

        //验证不重复。
        for (Long id : inputModuleIds){
            int times = 0;
            for (Long id2 : inputModuleIds){
                if ((long) id == id2){
                    times++;
                }
            }

            if (times > 1){
                result.put("code",-1);
                result.put("message","模块id不能重复。");
                return result;
            }
        }

        return null;
    }
}
