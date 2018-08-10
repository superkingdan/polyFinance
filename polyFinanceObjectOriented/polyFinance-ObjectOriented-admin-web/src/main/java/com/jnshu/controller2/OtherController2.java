package com.jnshu.controller2;

import com.jnshu.Domain2.DomainFeedBackDetail;
import com.jnshu.dto2.FeedbackRPO;
import com.jnshu.entity.SystemData;
import com.jnshu.service.DataService2;
import com.jnshu.service.FeedbackService2;
import com.jnshu.utils.CAM;
import com.jnshu.utils.TokenUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OtherController2 {
    private static Logger logger = Logger.getLogger(BankController2.class);

    private static TokenUtil tokenUtil = new TokenUtil();

    @Autowired
    DataService2 dataService2;

    @Autowired
    FeedbackService2 feedbackService2;

    //参数设置-参数获取
    @RequestMapping(value = "/a/u/datas",method = RequestMethod.GET)
    public Map<String, Object> getDatas(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);

        //返回数据List。改为map。
        Map<String, Object> result = new HashMap<>();
//        List<Object> result = new ArrayList<>();

        List<SystemData> systemDataList = new ArrayList<>();
        try {
            systemDataList = dataService2.getSystemData();
            if (null == systemDataList){
                result.put("code",-1);
                result.put("message","获取参数时 数据库 错误。");
                return result;
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","获取参数时 服务器 错误。");
            e.printStackTrace();
            logger.info("后台 运营管理--参数设置--获取参数时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }

        //组建键值对。
        Map<String,String> dataMap = new HashMap<>();
        for (SystemData systemData: systemDataList){
            dataMap.put(systemData.getDataName(), systemData.getDataValue());
        }

        result.put("code",0);
        result.put("message","获取参数成功。");
        result.put("data",dataMap);
        logger.info("后台 运营管理--参数设置--获取参数成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //参数设置-参数保存
    @RequestMapping(value = "/a/u/datas",method = RequestMethod.PUT)
    public  Map<String, Object> saveDatas(
            @RequestParam(required = false) String officialSeal
            ,@RequestParam(required = false) String investmentDay,
            @RequestParam(required = false) String creditorDay,
            @RequestParam(required = false) String creditorLine
            ,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);

        //返回数据List。改为map
//        List<Object> result = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        //参数校验。不能全部没有，或者没有值。
        if ((null == officialSeal || ("").equals(officialSeal))
                && (null == investmentDay || ("").equals(investmentDay))
                && (null == creditorDay || ("").equals(creditorDay))
                && (null == creditorLine || ("").equals(creditorLine))) {
            result.put("code",-1);
            result.put("message","需要更新的参数不能为空。");
            return result;
        }

        //获取现有数据。与要更新的键值对比较。如果相同，返回拒绝。否则，更新。
        List<SystemData> systemDatas = null;

        //比较结果。
        Boolean a = false;
        Boolean b = false;
        Boolean c = false;
        Boolean d = false;

        try {
            //获取数据库的参数值。
            systemDatas = dataService2.getSystemData();

            //提取为键值对。
            Map<String, String> dataMap = new HashMap<>();
            for (SystemData systemData1 : systemDatas){
                dataMap.put(systemData1.getDataName(),systemData1.getDataValue());
            }
            System.out.println("dataMap："+dataMap);

            //分别判断更新。
            a = ((null != officialSeal && !("").equals(officialSeal)) && officialSeal != dataMap.get("officialSeal"));
            b = ((null != investmentDay && !("").equals(investmentDay)) && investmentDay != dataMap.get("investmentDay"));
            c = ((null != creditorDay && !("").equals(creditorDay)) && creditorDay != dataMap.get("creditorDay"));
            d = ((null != creditorLine && !("").equals(creditorLine)) && creditorLine != dataMap.get("creditorLine"));

            //备份
            if (a || b|| c|| d){
                dataService2.updateAsBackup();
                logger.info("后台 运营管理--参数设置--更新参数时备份成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.put("code",0);
                result.put("message","备份成功。");
            }else {
                result.put("code",-1);
                result.put("message","没有新参数，不需要更新。");
                return result;
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器在获取参数或备份时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--参数更新--服务器在获取参数或备份时出错。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }

        /*//先备份
        Integer num = null;
        if (null != officialSeal || null != investmentDay || null != creditorDay || null != creditorLine){
            try {
                num = dataService2.updateAsBackup();
                if (0 == num){
                    CAM cam1 = new CAM(0, "不需要备份,没有参数更新。");
                    result.add(cam1);
                }
                logger.info("后台 运营管理--参数设置--更新参数时备份成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            } catch (Exception e) {
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在获取参数时出错");
                e.printStackTrace();
                logger.info("后台 运营管理--参数更新--备份参数时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.add(cam1);
                return result;
            }
        }
*/
        //更新操作。
        Boolean s1 = false;

        try {
            //更新 公章。
            if (a) {
                SystemData systemData = null;
                systemData = new SystemData();
                systemData.setUpdateAt(System.currentTimeMillis());
                systemData.setUpdateBy((Long) account.get("uid"));
                systemData.setDataName("officialSeal");
                systemData.setDataValue(officialSeal);

                s1 = dataService2.updateSystemData(systemData);

                if (s1){
                    result.put("code1",0);
                    result.put("message1","公章保存，更新成功。");
                }else {
                    result.put("code1",-1);
                    result.put("message1","公章要更新的值与原值相同。");
                }
            }

            //更新 投资到期消息提前天数
            if (b){
                SystemData systemData1 = null;
                systemData1 = new SystemData();
                systemData1.setUpdateAt(System.currentTimeMillis());
                systemData1.setUpdateBy((Long) account.get("uid"));
                systemData1.setDataName("investmentDay");
                systemData1.setDataValue(investmentDay);

                s1 = dataService2.updateSystemData(systemData1);
                if (s1){
                    result.put("code2",0);
                    result.put("message2","投资到期消息提前天数，更新成功。");
                }else {
                    result.put("code2",-1);
                    result.put("message2","投资到期消息提前天数  要更新的值与原值相同。");
                }
            }

            //更新 债权到期提前天数
            if (c){
                SystemData systemData2 = null;
                systemData2 = new SystemData();
                systemData2.setUpdateAt(System.currentTimeMillis());
                systemData2.setUpdateBy((Long) account.get("uid"));
                systemData2.setDataName("creditorDay");
                systemData2.setDataValue(creditorDay);

                if (s1){
                    result.put("code3",0);
                    result.put("message3","债权到期提前天数,更新成功。");
                }else {
                    result.put("code3",-1);
                    result.put("message3","债权到期提前天数  要更新的值与原值相同。");
                }
            }

            //更新 总债权投满警戒线
            if (d){
                SystemData systemData3 = new SystemData();
                systemData3.setUpdateAt(System.currentTimeMillis());
                systemData3.setUpdateBy((Long) account.get("uid"));
                systemData3.setDataName("creditorLine");
                systemData3.setDataValue(creditorLine);

                s1 = dataService2.updateSystemData(systemData3);
                if (s1){
                    result.put("code4",0);
                    result.put("message4","总债权投满警戒线,更新成功。");
                }else {
                    result.put("code4",0);
                    result.put("message4","总债权投满警戒线  要更新的值与原值相同。");
                }
            }
        } catch (Exception e) {
            result.put("code",-1);
            result.put("message","服务器错误。");
            result.put("errorMessage","服务器在更新参数时出错。");
            e.printStackTrace();
            logger.info("后台 运营管理--参数设置--更新参数时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            return result;
        }
        logger.info("后台 运营管理--参数设置--更新参数成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //参数设置-参数还原。
    @RequestMapping(value = "/a/u/datas/reduction",method = RequestMethod.PUT)
    public List<Object> returnDatas(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        //参数还原。
        Integer num = null;
        try {
            num = dataService2.updataDataFromBackup();
            if (0 != num){
                CAM cam1 = new CAM(0,"参数还原成功。");
                result.add(cam1);
            }else {
                CAM cam1 = new CAM(-1,"没有参数需要还原。");
                result.add(cam1);
            }
        } catch (Exception e) {
            CAM cam1 = new CAM(-1,"服务器错误。");
            cam1.setErrorMessage("服务器在还原参数时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--参数设置--还原参数时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            result.add(cam1);
            return result;
        }
        return result;
    }

    //版本管理-获取
    @RequestMapping(value = "/a/u/versions",method = RequestMethod.GET)
    public List<Object> getVersion(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        List<SystemData> versionData = new ArrayList<>();
        try {
            versionData = dataService2.getSystemDataOfVersion();
            if (null == versionData){
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在获取版本参数时出错");
                result.add(cam1);
                return result;
            }
        } catch (Exception e) {
            CAM cam1 = new CAM(-1,"服务器错误。");
            cam1.setErrorMessage("服务器在获取版本参数时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--版本管理--获取版本参数时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            result.add(cam1);
            return result;
        }

        Map<String, String> version = new HashMap<>();
        for (SystemData systemData : versionData){
            version.put(systemData.getDataName(),systemData.getDataValue());
        }

        cam.setMessage("版本参数获取成功。");
        result.add(cam);
        result.add(version);
        logger.info("后台 运营管理--版本管理--获取版本参数成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //版本管理-更新
    @RequestMapping(value = "/a/u/versions",method = RequestMethod.PUT)
    public List<Object> saveVersion(
            @RequestParam(required = false) String versionName,
            @RequestParam(required = false) String version,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String versionInfo,
            HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        List<SystemData> versionData = new ArrayList<>();

        //参数校验
        if ((null == versionName || ("").equals(versionName)) && (null == version || ("").equals(version)) && (null == url || ("").equals(url)) && (null == versionInfo || ("").equals(versionInfo))){
            CAM cam1 = new CAM(-1, "版本信息不能全为空或无值");
            result.add(cam1);
            return result;
        }

        Boolean d = false;
        if ((null != versionName && !("").equals(versionName))){
            SystemData systemData = new SystemData();
            systemData.setUpdateBy((Long) account.get("uid"));
            systemData.setUpdateAt(System.currentTimeMillis());
            systemData.setDataName("versionName");
            systemData.setDataValue(versionName);

            try {
                d = dataService2.updateSystemDataOfVersion(systemData);
            } catch (Exception e) {
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在更新版本名时出错");
                e.printStackTrace();
                logger.info("后台 运营管理--版本管理--版本更新时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.add(cam1);
                return result;
            }
        }

        Boolean a = false;
        if ((null != version && !("").equals(version))){
            SystemData systemData = new SystemData();
            systemData.setUpdateBy((Long) account.get("uid"));
            systemData.setUpdateAt(System.currentTimeMillis());
            systemData.setDataName("version");
            systemData.setDataValue(version);

            try {
                a = dataService2.updateSystemDataOfVersion(systemData);
            } catch (Exception e) {
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在更新版本号时出错");
                e.printStackTrace();
                logger.info("后台 运营管理--版本管理--版本号更新时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.add(cam1);
                return result;
            }
        }

        Boolean b = false;
        if ((null != url && !("").equals(url))){
            SystemData systemData = new SystemData();
            systemData.setUpdateBy((Long) account.get("uid"));
            systemData.setUpdateAt(System.currentTimeMillis());
            systemData.setDataName("url");
            systemData.setDataValue(url);

            try {
                b = dataService2.updateSystemDataOfVersion(systemData);
            } catch (Exception e) {
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在更新版本URL地址时出错");
                e.printStackTrace();
                logger.info("后台 运营管理--版本管理--更新版本URL地址时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.add(cam1);
                return result;
            }
        }

        Boolean c = false;
        if ((null != versionInfo && !("").equals(versionInfo))){
            SystemData systemData = new SystemData();
            systemData.setUpdateBy((Long) account.get("uid"));
            systemData.setUpdateAt(System.currentTimeMillis());
            systemData.setDataName("versionInfo");
            systemData.setDataValue(versionInfo);

            try {
                c = dataService2.updateSystemDataOfVersion(systemData);
            } catch (Exception e) {
                CAM cam1 = new CAM(-1,"服务器错误。");
                cam1.setErrorMessage("服务器在更新版本信息时出错");
                e.printStackTrace();
                logger.info("后台 运营管理--版本管理--更新版本信息时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
                result.add(cam1);
                return result;
            }
        }

        if (a || b || c || d){
            cam.setMessage("版本更新成功。");
        }

        result.add(cam);
        logger.info("后台 运营管理--版本管理--版本参数更新成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //意见反馈-意见列表
    @RequestMapping(value = "/a/u/feedbacks",method = RequestMethod.GET)
    public List<Object> getFeedbacks(
            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize,
            @ModelAttribute FeedbackRPO rpo,
            HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        if (!(("").equals(rpo.getCreateAt1())) || !(("").equals(rpo.getCreateAt2()))){
           if ((null == rpo.getCreateAt1() ||("").equals(rpo.getCreateAt1()) )   || (null == rpo.getCreateAt2() ||("").equals(rpo.getCreateAt2()) )){
               CAM cam1 = new CAM(-1,"两个查询日期都要有值。");
               result.add(cam1);
               return result;
           }
        }

        List<DomainFeedBackDetail> list = null;
        try {
            list = feedbackService2.getFeedbackList(rpo);
            if (null == list){
                CAM cam1 = new CAM(0,"此条件下无值。");
                result.add(cam1);
                return result;
            }
        } catch (Exception e) {
            CAM cam1 = new CAM(-1,"服务器错误。");
            cam1.setErrorMessage("服务器在查询意见反馈时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--版本管理--查询意见反馈时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            result.add(cam1);
            return result;
        }

        cam.setMessage("查询成功。");
        result.add(cam);
        result.add(list);
        logger.info("后台 运营管理--版本管理--查询意见成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //意见反馈-意见详情
    @RequestMapping(value = "/a/u/feedbacks/{id}",method = RequestMethod.GET)
    public List<Object> getFeedback(@PathVariable long id, HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        if (id<1){
            CAM cam1 = new CAM(-1,"非法参数。");
            result.add(cam1);
            return result;
        }
        DomainFeedBackDetail detail = null;

        try {
            detail = feedbackService2.getFeedbackDetail(id);
            if (detail == null){
                CAM cam1 = new CAM(0,"此id没有对应记录。");
                result.add(cam1);
                return result;
            }
        } catch (Exception e) {
            CAM cam1 = new CAM(-1,"服务器错误。");
            cam1.setErrorMessage("服务器在查询意见反馈详情时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--版本管理--查询意见反馈详情时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            result.add(cam1);
            return result;
        }

        cam.setMessage("查询成功。");
        result.add(cam);
        result.add(detail);
        logger.info("后台 运营管理--版本管理--查询意见反馈详情成功。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
        return result;
    }

    //意见反馈-意见详情
    @RequestMapping(value = "/a/u/feedbacks/{id}",method = RequestMethod.DELETE)
    public List<Object> deleteFeedback(@PathVariable long id, HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> account = new HashMap<>();
        CAM cam = new CAM();
        account = tokenUtil.getAccount(request);
        //返回数据List。
        List<Object> result = new ArrayList<>();

        if (id<1){
            CAM cam1 = new CAM(-1,"非法参数。");
            result.add(cam1);
            return result;
        }

        Boolean a = false;
        try {
            a = feedbackService2.deleteFeedback(id);
            if (!a){
                CAM cam1 = new CAM(-1,"此id无对应记录。");
                result.add(cam1);
                return result;
            }
        } catch (Exception e) {
            CAM cam1 = new CAM(-1,"服务器错误。");
            cam1.setErrorMessage("服务器在删除意见反馈详情时出错");
            e.printStackTrace();
            logger.info("后台 运营管理--版本管理--删除意见反馈详情时服务器错误。当前账户id："+account.get("uid")+"，账户名："+account.get("loginName")+"，后台角色："+account.get("role"));
            result.add(cam1);
            return result;
        }

        cam.setMessage("删除成功。");
        result.add(cam);
        return result;
    }
}
