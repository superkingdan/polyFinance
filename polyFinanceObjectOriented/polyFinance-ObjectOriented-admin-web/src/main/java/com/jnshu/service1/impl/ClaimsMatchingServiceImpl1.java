package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.*;
import com.jnshu.dto1.ClaimsMatchingRO;
import com.jnshu.dto1.ClaimsMatchingRPO;
import com.jnshu.entity.*;
import com.jnshu.dto1.ContractMatchingRO;
import com.jnshu.exception.MyException;
import com.jnshu.service1.ClaimsMatchingService1;
import com.jnshu.utils.TransString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 债权匹配相关接口
 * @author wangqichao
 */
@Service
//开启事务
@Transactional
public class ClaimsMatchingServiceImpl1 implements ClaimsMatchingService1 {
    @Autowired
    ClaimsMapper1 claimsMapper1;
    @Autowired
    ProductMapper1 productMapper1;
    @Autowired
    UserMapper1 userMapper1;
    @Autowired
    TransactionMapper1 transactionMapper1;
    @Autowired
    ContractMapper1 contractMapper1;
    @Autowired
    ClaimsMatchingMapper1 claimsMatchingMapper1;
    @Autowired
    SystemDataMapper1 systemDataMapper1;
    @Autowired
    TimedTaskMapper1 timedTaskMapper1;

    private static final Logger log= LoggerFactory.getLogger(ClaimsMatchingService1.class);

    /**
     * 获得指定id债权匹配情况
     * @param claimsId 债权id
     * @return 债权匹配详情
     * @throws Exception 自定义异常
     */
    @Override
    public Claims getClaimsInfoById(long claimsId) throws Exception{
        log.info("获得指定债权"+claimsId+"匹配情况");
        //查询债权匹配列表的data1
        Claims claims;
        try{
            claims=claimsMapper1.getClaimsMatchingById(claimsId);
        }catch (Exception e){
            throw new MyException(-1,"获取债权"+claimsId+"匹配情况失败");
        }
        return claims;
    }

    /**
     * 按照分页等条件获得某债权匹配列表
     * @param rpo 分页条件
     * @return 此债权按照条件查询的结果
     */
    @Override
    public Page<ClaimsMatchingRO> getClaimsMatchingListByRpo(ClaimsMatchingRPO rpo) throws Exception{
        log.info("获得债权"+rpo.getId()+"的匹配列表");
        //如果productName不为空，就查询其对应的productId[]
        if (rpo.getProductName()!=null&&!rpo.getProductName().equalsIgnoreCase("")){
           try{
               rpo.setProductId(productMapper1.getProductIdByProductName(rpo.getProductName()));
           }catch (Exception e){
               throw new MyException(-1,"按照产品名获得产品id失败");
           }
        }
        //如果userName不为空，就查询对应的userId[]
        if(rpo.getUserName()!=null&&!rpo.getUserName().equalsIgnoreCase("")){
            try{
                rpo.setUserId(userMapper1.getUserIdByName(rpo.getUserName()));
            }catch (Exception e){
                throw new MyException(-1,"按照用户名获得用户id失败");
            }
        }
        //利用分页查询符合条件的数据
        Page<ClaimsMatchingRO> claimsMatchingROPage= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        //拦截这条语句查询相关信息并分页封装
        try{
            transactionMapper1.getClaimsMatchingList(rpo);
        }catch (Exception e){
            throw new MyException(-1,"查询匹配列表失败");
        }
        //利用for循环遍历添加债权协议，产品名，用户名
        for (ClaimsMatchingRO ro : claimsMatchingROPage) {
            try {
                ro.setClaimsProtocolCode(contractMapper1.getClaimsCodeByContractCode(ro.getContractCode()));
                ro.setProductName(productMapper1.getProductNameByProductId(ro.getProductId()));
                ro.setUserName(userMapper1.getUserNameById(ro.getUserId()));
            }catch (Exception e){
                throw new MyException(-1,"添加其余信息失败");
            }
        }
        return claimsMatchingROPage;
    }

    /**
     * 获取适合给某债权匹配的合同列表
     * @param claimsId 债权Id
     * @return 适合的合同列表
     */
    @Override
    public List<ContractMatchingRO> getClaimsMatchingListById(long claimsId) throws Exception{
        log.info("获取适合给债权"+claimsId+"匹配的合同列表");
        try {
            //查询此债权已经匹配的用户
            Long[] userIds;
            try {
                userIds = transactionMapper1.getUserIdByClaimsId(claimsId);
            } catch (Exception e) {
                throw new MyException(-1, "查询此债权以匹配用户失败");
            }
            //查询此债权到期时间
            long claimsLendEndAt;
            try {
                claimsLendEndAt = claimsMapper1.getLendEndById(claimsId);
            } catch (Exception e) {
                throw new MyException(-1, "查询此债权到期时间失败");
            }
            //查询此债权待匹配金额
            String claimsRemanentMoneyS;
            try {
                claimsRemanentMoneyS = claimsMapper1.getClaimsMatchingById(claimsId).getRemanentMoney();
            } catch (Exception e) {
                throw new MyException(-1, "查询此债权待匹配金额失败");
            }
            BigDecimal claimsRemanentMoney = new BigDecimal(claimsRemanentMoneyS);
            //查询尚未匹配债权的合同
            List<String> contractCodes;
            try {
                contractCodes = contractMapper1.getContractCodeNotMatching();
            } catch (Exception e) {
                throw new MyException(-1, "查询未匹配债权的合同失败");
            }
            if(contractCodes==null){
                throw new MyException(-1,"暂无可匹配合同");
            }
            List<ContractMatchingRO> contractMatchingROES1 = new ArrayList<>();
            //查询对应信息
            for (int i = 0; i < contractCodes.size(); i++) {
                try {
                    //查询合同编号对应信息并放进roes对象中
                    ContractMatchingRO contractMatchingRO = transactionMapper1.getContractInfoByContractCode(contractCodes.get(i));
                    //即使是空对象也放进去，可以避免数组越界问题
                    contractMatchingROES1.add(contractMatchingRO);
                    if (contractMatchingRO != null) {
                        //将ro对象中的UserName和productName放进去
                        System.out.println(i);
                        System.out.println(contractMatchingROES1.size());
                        if (contractMatchingROES1.get(i).getUserId() > 0) {
                            contractMatchingROES1.get(i).setUserName(userMapper1.getUserNameById(contractMatchingROES1.get(i).getUserId()));
                        }
                        if (contractMatchingROES1.get(i).getProductId() > 0) {
                            contractMatchingROES1.get(i).setProductName(productMapper1.getProductNameByProductId(contractMatchingROES1.get(i).getProductId()));
                            }
                    }
                }catch(Exception e){
                        throw new MyException(-1, "查询合同编号用户产品名失败");
                    }
                }
                //循环添加对象进去
            List<ContractMatchingRO> contractMatchingROES = new ArrayList<>();
            for(int i=0;i<contractMatchingROES1.size();i++){
                if(contractMatchingROES1.get(i)!=null){
                    contractMatchingROES.add(contractMatchingROES1.get(i));
                }
            }
            //计算每个对象的对应分数
            for (ContractMatchingRO contractMatchingROE : contractMatchingROES) {
                int fraction = 0;
                long userId = contractMatchingROE.getUserId();
                //如果用户id重复，直接扣除3000分
                if(userIds!=null) {
                    for (Long userId1 : userIds) {
                        if (userId == userId1)
                            fraction = fraction - 3000;
                    }
                }
                System.out.println("计算用户是否重复成功");
                //如果合同金额大于债权金额，扣3000,小于不得分，等于得1200分
                BigDecimal money = new BigDecimal(contractMatchingROE.getMoney());
                if (money.compareTo(claimsRemanentMoney) > 0)
                    fraction = fraction - 3000;
                if (money.compareTo(claimsRemanentMoney) == 0)
                    fraction = fraction + 1200;
                System.out.println("计算金额得分成功");
                //计算时间，如果一个是长债权，一个是短债权，则扣3000分，否则相差一天扣1分
                long endAt = contractMatchingROE.getEndAt();
                long currentAt = System.currentTimeMillis();
                long sixMonth = 6 * 30 * 24 * 3600 * 1000L;
                //长短不一扣3000
                if (((claimsLendEndAt - currentAt - sixMonth) < 0 && (endAt - currentAt - sixMonth) > 0) || ((claimsLendEndAt - currentAt - sixMonth) > 0 && (endAt - currentAt - sixMonth) < 0))
                    fraction = fraction - 3000;
                //债权时间大于合同时间，加600，然后每差一天扣一分
                if (claimsLendEndAt - endAt > 0)
                    fraction = fraction + 600 - (int) ((claimsLendEndAt - endAt) / (3600 * 24 * 1000));
                //债权时间小于合同时间，每差一天扣一分
                if (endAt - claimsLendEndAt > 0)
                    fraction = fraction - (int) ((endAt - claimsLendEndAt) / (24 * 3600 * 1000));
                //把fraction放入
                contractMatchingROE.setFraction(fraction);
            }
            //新建一个roes，将筛选出的fraction>-1000的放进去，不合格的就不要
            List<ContractMatchingRO> matchingROES = new ArrayList<>();
            for (ContractMatchingRO contractMatchingROE : contractMatchingROES) {
                if (contractMatchingROE.getFraction() > -1000) {
                      matchingROES.add(contractMatchingROE);
                }
            }

            return matchingROES;
        }catch (Exception e){
            throw new MyException(-1, "查询债权"+claimsId+"适合匹配合同未知错误");
        }
    }

    /**
     * 保存债权匹配的结果
     * @param claimsMatching 债权匹配结果
     * @return 成功标志
     */
    @Override
    public int saveClaimsMatching(ClaimsMatching claimsMatching) throws Exception {
        log.info("开始债权匹配，债权id为" + claimsMatching.getClaimsId() + "出借合同编号为" + claimsMatching.getContractCode());
        try {
            //查询交易对应的金额
            String money;
            try {
                money = transactionMapper1.getContractInfoByContractCode(claimsMatching.getContractCode()).getMoney();
            } catch (Exception e) {
                throw new MyException(-1, "查询交易对应金额失败");
            }
            //获得最新的债权协议编号
            String newestClaimsProtocolCode;
            try {
                newestClaimsProtocolCode = claimsMatchingMapper1.getNewestClaimsProtocolCode();
            } catch (Exception e) {
                throw new MyException(-1, "获得最新债权协议编号失败");
            }
            if (newestClaimsProtocolCode == null) {
                newestClaimsProtocolCode = "";
            }
            //生成新的债权协议编号,并存入claimsMatching对象
            String claimsProtocolCode = TransString.transClaimsCode(newestClaimsProtocolCode);
            claimsMatching.setClaimsProtocolCode(claimsProtocolCode);
            claimsMatching.setCreateAt(System.currentTimeMillis());
            //新建claims_matching数据
            try {
                claimsMatchingMapper1.addClaimsMatching(claimsMatching);
            } catch (Exception e) {
                throw new MyException(-1, "新增债权匹配信息失败");
            }
            long id = claimsMatching.getId();
            log.info("新建债权匹配id为" + id);
            //生成新的合同对象
            Contract contract = new Contract();
            contract.setContractCode(claimsMatching.getContractCode());
            contract.setCurrentClaimsCode(claimsProtocolCode);
            contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_YES);
            contract.setUpdateAt(System.currentTimeMillis());
            contract.setUpdateBy(claimsMatching.getCreateBy());
            //修改合同表相关信息
            try {
                contractMapper1.updateClaimsInfo(contract);
            } catch (Exception e) {
                throw new MyException(-1, "修改合同表失败");
            }
            //生成新的交易对象
            Transaction transaction = new Transaction();
            transaction.setUpdateAt(System.currentTimeMillis());
            transaction.setUpdateBy(claimsMatching.getCreateBy());
            transaction.setClaimsId(claimsMatching.getClaimsId());
            transaction.setContractCode(claimsMatching.getContractCode());
            //修改交易表相关信息
            try {
                transactionMapper1.updateClaimsId(transaction);
            } catch (Exception e) {
                throw new MyException(-1, "新增交易表信息失败");
            }
            //获得原待匹配金额
            Claims claims;
            try {
                claims = claimsMapper1.getClaimsMatchingById(claimsMatching.getClaimsId());
            } catch (Exception e) {
                throw new MyException(-1, "获得原债权信息失败");
            }
            if (claims == null) {
                throw new MyException(-1, "原债权信息为空");
            }
            String remanentMoney = claims.getRemanentMoney();
            String lendMoney = claims.getLendMoney();
            //获得债权结束时间
            long lendEndAt = claims.getLendEndAt();
            //获得债权匹配线
            String creditorLine;
            try {
                creditorLine = systemDataMapper1.getCreditorLine();
            } catch (Exception e) {
                throw new MyException(-1, "获得债权匹配线失败");
            }
            BigDecimal moneyB = new BigDecimal(money);
            BigDecimal remanentMoneyB = new BigDecimal(remanentMoney);
            BigDecimal lendMoneyB = new BigDecimal(lendMoney);
            BigDecimal creditorLineB = new BigDecimal(creditorLine);
            //如果之前没超过警戒线，现在超过了，就通知管理员
            if (((lendMoneyB.subtract(remanentMoneyB))).compareTo(creditorLineB.multiply(lendMoneyB)) < 0 && ((lendMoneyB.subtract(remanentMoneyB)).add(moneyB)).compareTo(creditorLineB.multiply(lendMoneyB)) > 0) {
                System.out.println("债权" + claimsMatching.getClaimsId() + "超过警戒线，需通知管理员");
            }
            String remanentMoneyNew = remanentMoneyB.subtract(moneyB).toString();
            //设置需修改的债权的信息
            Claims claims1 = new Claims();
            claims1.setId(claimsMatching.getClaimsId());
            claims1.setUpdateAt(System.currentTimeMillis());
            claims1.setUpdateBy(claimsMatching.getCreateBy());
            claims1.setRemanentMoney(remanentMoneyNew);
            claims1.setStatus(Claims.STATUS_USING);
            //修改债权表待匹配金额和状态
            try {
                claimsMapper1.updateClaimsMoney(claims1);
            } catch (Exception e) {
                throw new MyException(-1, "修改原债权待匹配金额和状态失败");
            }
            //获得合同结束时间
            long endAt;
            try {
                endAt = transactionMapper1.getContractInfoByContractCode(claimsMatching.getContractCode()).getEndAt();
            } catch (Exception e) {
                throw new MyException(-1, "获得合同结束时间失败");
            }
            //如果合同结束时间大于债权结束时间，添加定时任务
            if (endAt > lendEndAt) {
                TimedTask timedTask = new TimedTask();
                timedTask.setCreateAt(System.currentTimeMillis());
                timedTask.setCreateBy(claimsMatching.getCreateBy());
                timedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
                timedTask.setNature(TimedTask.NATURE_CONTRACT);
                timedTask.setTaskTime(lendEndAt);
                timedTask.setContractId(contractMapper1.getContractIdByCode(claimsMatching.getContractCode()));
                try {
                    timedTaskMapper1.addTaskedTime(timedTask);
                } catch (Exception e) {
                    throw new MyException(-1, "添加新债权定时任务失败");
                }
                log.info("生成新定时任务id为" + id);
            }
            return 1;
        }catch (Exception e){
            throw new MyException(-1, "保存债权匹配结果失败");
        }
    }
}
