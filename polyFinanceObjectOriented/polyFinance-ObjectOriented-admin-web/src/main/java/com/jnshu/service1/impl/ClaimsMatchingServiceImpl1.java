package com.jnshu.service1.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jnshu.dao.*;
import com.jnshu.dto1.ClaimsMatchingRO;
import com.jnshu.dto1.ClaimsMatchingRPO;
import com.jnshu.entity.*;
import com.jnshu.dto1.ContractMatchingRO;
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
    @Override
    public Claims getClaimsInfoById(long claimsId) {
        //查询债权匹配列表的data1
        return claimsMapper1.getClaimsMatchingById(claimsId);
    }

    /**
     * 按照分页等条件获得某债权匹配列表
     * @param rpo 分页条件
     * @return 此债权按照条件查询的结果
     */
    @Override
    public Page<ClaimsMatchingRO> getClaimsMatchingListByRpo(ClaimsMatchingRPO rpo) {
        //如果productName不为空，就查询其对应的productId
        if (rpo.getProductName()!=null&&!rpo.getProductName().equalsIgnoreCase("")){
            rpo.setProductId(productMapper1.getProductIdByProductName(rpo.getProductName()));
        }
        //如果userName不为空，就查询对应的userId[]
        if(rpo.getUserName()!=null&&!rpo.getUserName().equalsIgnoreCase("")){
            rpo.setUserId(userMapper1.getUserIdByName(rpo.getUserName()));
        }
        //利用分页查询符合条件的数据
        Page<ClaimsMatchingRO> claimsMatchingROPage= PageHelper.startPage(rpo.getPage(),rpo.getSize());
        //拦截这条语句查询相关信息并分页封装
        transactionMapper1.getClaimsMatchingList(rpo);
        //利用for循环遍历添加债权协议，产品名，用户名
        for (ClaimsMatchingRO ro : claimsMatchingROPage) {
            ro.setClaimsProtocolCode(contractMapper1.getClaimsCodeByContractCode(ro.getContractCode()));
            ro.setProductName(productMapper1.getProductNameByProductId(ro.getProductId()));
            ro.setUserName(userMapper1.getUserNameById(ro.getUserId()));
        }
        return claimsMatchingROPage;
    }

    /**
     * 获取适合给某债权匹配的合同列表
     * @param claimsId 债权Id
     * @return 适合的合同列表
     */
    @Override
    public List<ContractMatchingRO> getClaimsMatchingListById(long claimsId) {
        System.out.println("进入此service");
        //查询此债权已经匹配的用户
        Long[] userIds= transactionMapper1.getUserIdByClaimsId(claimsId);
        //查询此债权到期时间
        long claimsLendEndAt= claimsMapper1.getLendEndById(claimsId);
//        System.out.println(claimsLendEndAt);
        //查询此债权待匹配金额
        String claimsRemanentMoneyS= claimsMapper1.getClaimsMatchingById(claimsId).getRemanentMoney();
        BigDecimal claimsRemanentMoney=new BigDecimal(claimsRemanentMoneyS);
//        System.out.println(claimsRemanentMoney);
        List<String> contractCodes= contractMapper1.getContractCodeNotMatching();
        List<ContractMatchingRO> contractMatchingROES=new ArrayList<>();
        //查询对应信息
        for (int i=0;i<contractCodes.size();i++){
            //查询合同编号对应信息并放进roes对象中
            contractMatchingROES.add(transactionMapper1.getContractInfoByContractCode(contractCodes.get(i)));
            //将ro对象中的UserName和productName放进去
            contractMatchingROES.get(i).setUserName(userMapper1.getUserNameById(contractMatchingROES.get(i).getUserId()));
            contractMatchingROES.get(i).setProductName(productMapper1.getProductNameByProductId(contractMatchingROES.get(i).getProductId()));
//            System.out.println(i);
//            System.out.println( contractMatchingROES.get(i).getContractCode());
        }
        //计算每个对象的对应分数
        for (ContractMatchingRO contractMatchingROE : contractMatchingROES) {
            int fraction = 0;
            long userId = contractMatchingROE.getUserId();
            //如果用户id重复，直接扣除3000分
            for (Long userId1 : userIds) {
                if (userId == userId1)
                    fraction = fraction - 3000;
//                System.out.println("userId相等扣分"+fraction);
            }
            //如果合同金额大于债权金额，扣3000,小于不得分，等于得1200分
            BigDecimal money = new BigDecimal(contractMatchingROE.getMoney());
            if (money.compareTo(claimsRemanentMoney) > 0)
                fraction = fraction - 3000;
            if (money.compareTo(claimsRemanentMoney) == 0)
                fraction = fraction + 1200;
//            System.out.println("债权金额之后得分为"+fraction);
            //计算时间，如果一个是长债权，一个是短债权，则扣3000分，否则相差一天扣1分
            long endAt = contractMatchingROE.getEndAt();
//            System.out.println(endAt);
            long currentAt = System.currentTimeMillis();
            long sixMonth = 6 * 30 * 24 * 3600 * 1000L;
            //长短不一扣3000
            if (((claimsLendEndAt - currentAt - sixMonth)<0&&(endAt - currentAt - sixMonth) > 0)||((claimsLendEndAt - currentAt - sixMonth)>0&&(endAt - currentAt - sixMonth)<0))
                fraction = fraction - 3000;
            //债权时间大于合同时间，加600，然后每差一天扣一分
//            System.out.println("长短不一后得分为"+fraction);
            if (claimsLendEndAt - endAt > 0)
                fraction = fraction + 600 - (int) ((claimsLendEndAt - endAt) / (3600 * 24 * 1000));
            //债权时间小于合同时间，每差一天扣一分
            if (endAt - claimsLendEndAt > 0)
                fraction = fraction - (int)((endAt - claimsLendEndAt) / (24 * 3600 * 1000));
//            System.out.println("最后得分为"+fraction);
//            System.out.println("______________________________________");
            //把fraction放入
            contractMatchingROE.setFraction(fraction);
        }
        //新建一个roes，将筛选出的fraction>-1000的放进去，不合格的就不要
        List<ContractMatchingRO> matchingROES=new ArrayList<>();
        for (ContractMatchingRO contractMatchingROE : contractMatchingROES) {
            if (contractMatchingROE.getFraction() > -1000) {
                matchingROES.add(contractMatchingROE);
            }
        }
        return matchingROES;
    }

    /**
     * 保存债权匹配的结果
     * @param claimsMatching 债权匹配结果
     * @return 成功标志
     */
    @Override
    public int saveClaimsMatching(ClaimsMatching claimsMatching) {
        //查询交易对应的金额
        String money= transactionMapper1.getContractInfoByContractCode(claimsMatching.getContractCode()).getMoney();
        //获得最新的债权协议编号
        String newestClaimsProtocolCode= claimsMatchingMapper1.getNewestClaimsProtocolCode();
        System.out.println(newestClaimsProtocolCode);
        //生成新的债权协议编号,并存入claimsMatching对象
        String claimsProtocolCode= TransString.transClaimsCode(newestClaimsProtocolCode);
        claimsMatching.setClaimsProtocolCode(claimsProtocolCode);
        claimsMatching.setCreateAt(System.currentTimeMillis());
        //新建claims_matching数据
        int a= claimsMatchingMapper1.addClaimsMatching(claimsMatching);
        System.out.println("a="+a);
        long id=claimsMatching.getId();
        log.info("新建债权匹配id为"+id);
//        System.out.println(id);
        //生成新的合同对象
        Contract contract=new Contract();
        contract.setContractCode(claimsMatching.getContractCode());
        contract.setCurrentClaimsCode(claimsProtocolCode);
        contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_YES);
        contract.setUpdateAt(System.currentTimeMillis());
        contract.setUpdateBy(claimsMatching.getCreateBy());
        //修改合同表相关信息
        int b= contractMapper1.updateClaimsInfo(contract);
        System.out.println("b="+b);
        //生成新的交易对象
        Transaction transaction=new Transaction();
        transaction.setUpdateAt(System.currentTimeMillis());
        transaction.setUpdateBy(claimsMatching.getCreateBy());
        transaction.setClaimsId(claimsMatching.getClaimsId());
        transaction.setContractCode(claimsMatching.getContractCode());
        //修改交易表相关信息
        int c= transactionMapper1.updateClaimsId(transaction);
        System.out.println("c="+c);
        //获得原待匹配金额
        Claims claims= claimsMapper1.getClaimsMatchingById(claimsMatching.getClaimsId());
        String remanentMoney=claims.getRemanentMoney();
        String lendMoney=claims.getLendMoney();
        //获得债权结束时间
        long lendEndAt=claims.getLendEndAt();
        //获得债权匹配线
        String creditorLine= systemDataMapper1.getCreditorLine();
        BigDecimal moneyB=new BigDecimal(money);
        BigDecimal remanentMoneyB=new BigDecimal(remanentMoney);
        BigDecimal lendMoneyB=new BigDecimal(lendMoney);
        BigDecimal creditorLineB=new BigDecimal(creditorLine);
        //如果之前没超过警戒线，现在超过了，就通知管理员
        if(((lendMoneyB.subtract(remanentMoneyB))).compareTo(creditorLineB.multiply(lendMoneyB))<0&&((lendMoneyB.subtract(remanentMoneyB)).add(moneyB)).compareTo(creditorLineB.multiply(lendMoneyB))>0){
            System.out.println("债权"+claimsMatching.getClaimsId()+"超过警戒线，需通知管理员");
        }
        String remanentMoneyNew=remanentMoneyB.subtract(moneyB).toString();
        //设置需修改的债权的信息
        Claims claims1=new Claims();
        claims1.setId(claimsMatching.getClaimsId());
        claims1.setUpdateAt(System.currentTimeMillis());
        claims1.setUpdateBy(claimsMatching.getCreateBy());
        claims1.setRemanentMoney(remanentMoneyNew);
        claims1.setStatus(Claims.STATUS_USING);
        //修改债权表待匹配金额和状态
        int d= claimsMapper1.updateClaimsMoney(claims1);
        System.out.println("d="+d);
        //获得合同结束时间
        long endAt= transactionMapper1.getContractInfoByContractCode(claimsMatching.getContractCode()).getEndAt();
        //如果合同结束时间大于债权结束时间，添加定时任务
        int e=1;
        if(endAt>lendEndAt){
            TimedTask timedTask=new TimedTask();
            timedTask.setCreateAt(System.currentTimeMillis());
            timedTask.setCreateBy(claimsMatching.getCreateBy());
            timedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            timedTask.setNature(TimedTask.NATURE_CONTRACT);
            timedTask.setTaskTime(lendEndAt);
            timedTask.setContractId(contractMapper1.getContractIdByCode(claimsMatching.getContractCode()));
            e= timedTaskMapper1.addTaskedTime(timedTask);
            log.info("生成新定时任务id为"+id);
        }
        System.out.println("e="+e);
        return a*b*c*d*e;
    }
}
