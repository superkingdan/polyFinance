package com.jnshu.service1.Impl;

import com.jnshu.dao.*;
import com.jnshu.dto1.TransactionListRO;
import com.jnshu.dto1.TransactionRO;
import com.jnshu.entity.*;
import com.jnshu.service1.TransactionService;
import com.jnshu.utils.TransString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * 交易相关service
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log= LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    TransactionMapper transactionMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    TimedTaskMapper timedTaskMapper;
    @Autowired
    SystemDataMapper systemDataMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BankCardMapper bankCardMapper;
    @Autowired
    BankMapper bankMapper;

    /**
     * 获得用户可续投列表，需要筛选用户的投资中产品状态为在售，产品为不限购，续投状态为可续投的产品
     * @param id 用户id
     * @return 用户可续投列表
     */
    @Override
    public List<TransactionListRO> getContinueInvList(long id) {
        System.out.println("开始查询用户"+id+"的可续投列表");
        return transactionMapper.getRenewalListByUserId(id);
    }

    /**
     * 添加续投订单
     * @param oldId 原订单id
     * @param userSign 用户签名
     * @return 添加结果
     */
    @Override
    public int addRenewal(long oldId, String userSign) {
        //首先根据旧id查询旧订单相关情况，用户id,起息时间，结束时间，金额，预期收益，产品id
        Transaction oldTransaction=transactionMapper.getOldTransactionById(oldId);
        System.out.println("旧交易相关信息为"+oldTransaction);
        long userId=oldTransaction.getUserId();
        long productId=oldTransaction.getProductId();
        long oldEndAt=oldTransaction.getEndAt();
        String money=oldTransaction.getMoney();
        BigDecimal moneyB=new BigDecimal(money);
        String expectEarnings=oldTransaction.getExpectEarnings();
        BigDecimal expectEarningsB=new BigDecimal(expectEarnings);

        //获得产品相关信息,还款类型，期限，产品名，利率，起息时间
        Product product=productMapper.getRefundInfoById(productId);
        System.out.println("产品相关信息为"+product);
        String productCode=product.getProductCode();
        int rateOfInterest=product.getRateOfInterest();
        int deadline=product.getDeadline();
        int deadlineMonth=deadline/30;
        int refundStyle=product.getRefundStyle();
        String productName=product.getProductName();

        //查找最新合同号
        String newestContractCode=contractMapper.getNewestContractCode();
        System.out.println("最新合同号为"+newestContractCode);
        //生成合同编号
        String contractCode= TransString.transContractCode(newestContractCode,productCode);
        System.out.println("新合同编号为"+contractCode);
        //创建新合同
        Contract contract=new Contract();
        contract.setCreateAt(System.currentTimeMillis());
        contract.setCreateBy(userId);
        contract.setUserSign(userSign);
        contract.setContractCode(contractCode);
        contract.setIsPay(Contract.IS_PAY_YES);
        contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_NO);
        //添加合同表数据
        int a=contractMapper.addContractCode(contract);
        System.out.println("新合同的id为"+contract.getId());
        //创建新交易
        Transaction newTransaction=new Transaction();
        newTransaction.setCreateAt(System.currentTimeMillis());
        newTransaction.setCreateBy(userId);
        newTransaction.setContractCode(newestContractCode);
        long startAt=oldEndAt+rateOfInterest*3600*24*1000;
        newTransaction.setStartAt(startAt);
        long endAt;
        //计算结束时间，开始时间转化为date形式加上deadline转化为月份
        //如果交易天数小于一个月就按照实际天数计算，不过要加一天
        if (deadline<30) {
            endAt = startAt + (deadline+1) * 24 * 3600 * 1000;
        }
        //如果交易天数大于一个月就按照月份加当前月份，日期加一来计算时间
        else{
            Calendar calendar=Calendar.getInstance();
            calendar.clear();
            //设置起息时间为开始时间
            calendar.setTimeInMillis(startAt);
            //将此月份加deadline/30个月
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+(deadlineMonth));
            //再将日期加一天
            calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
            //转化为时间戳
            endAt=calendar.getTimeInMillis();
        }
        System.out.println("结束时间为"+endAt);
        newTransaction.setEndAt(endAt);
        newTransaction.setRenuwalStatus(Transaction.RENUWAL_STATUS_NOT);
        newTransaction.setMoney(money);
        newTransaction.setExpectEarnings(expectEarnings);
        newTransaction.setReturned("0");
        newTransaction.setNotReturn(expectEarnings);
        newTransaction.setProductId(productId);
        newTransaction.setStatus(Transaction.STATUS_INVESTING);
        newTransaction.setContractCode(contractCode);
        //添加新交易
        int b=transactionMapper.addTransaction(newTransaction);
        long newId=newTransaction.getId();
        System.out.println("新交易的id为"+newId);
        //修改旧交易
        oldTransaction.setUpdateBy(userId);
        oldTransaction.setUpdateAt(System.currentTimeMillis());
        oldTransaction.setRenuwalStatus(Transaction.RENUWAL_STATUS_HAVE);
        int c=transactionMapper.updateRenuwalStatus(oldTransaction);
        //修改原定时任务
        TimedTask oldTimedTask=new TimedTask();
        oldTimedTask.setUpdateAt(System.currentTimeMillis());
        oldTimedTask.setUpdateBy(userId);
        oldTimedTask.setTransactionId(oldId);
        //如果是本息一次还，修改原定时任务金额为预期收益
        if(refundStyle==1) {
            oldTimedTask.setMoney(expectEarnings);
        }
            //如果是先息后本修改最后一次回本金的任务的金额为减去本金
        else {
              //获取原本最后一次
            String lastTotalMoney=timedTaskMapper.getOldMoneyByTransactionId(oldId);
            BigDecimal lastTotalMoneyB=new BigDecimal(lastTotalMoney);
            String lastInterestMoney=lastTotalMoneyB.subtract(moneyB).toString();
            oldTimedTask.setMoney(lastInterestMoney);
        }
        //修改原定时任务性质为返息
        oldTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
        int d=timedTaskMapper.updateOldTransByTransId(oldTimedTask);
        System.out.println("修改原定时任务成功");
        //添加新定时任务
        //如果是本息一次还，添加回本金任务
        int e=0;
        if(refundStyle==1){
            TimedTask newTimedTask=new TimedTask();
            newTimedTask.setCreateAt(System.currentTimeMillis());
            newTimedTask.setCreateBy(userId);
            newTimedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            //结束时间一天之后开始返款
            newTimedTask.setTaskTime(endAt+24*3600*1000);
            newTimedTask.setNature(TimedTask.NATURE_RETURN_PRINCIPAL);
            //返回金额就是预期收益加金额
            String totalMoney=moneyB.add(expectEarningsB).toString();
            newTimedTask.setMoney(totalMoney);
            newTimedTask.setTransactionId(newId);
            e=timedTaskMapper.addTaskedTime(newTimedTask);
            System.out.println("本息一次还的还款定时任务id为"+newTimedTask.getId());
        }
        //如果是先息后本，添加deadlineMonth次返息任务，一次本带最后一次利息任务
       else{
            TimedTask newTimedTask=new TimedTask();
            newTimedTask.setCreateAt(System.currentTimeMillis());
            newTimedTask.setCreateBy(userId);
            newTimedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            newTimedTask.setTransactionId(newId);
            //循环，设置金额，时间，任务性质
            //因为七天的新手礼包是本息一次还，所以这里不用考虑deadline小于30的情况
            //循环deadlineMonth+1次，其中最后一次是本加最后一次利息，前deadlineMonth是只有利息
            BigDecimal notReturn=expectEarningsB;
            BigDecimal everyMonthReturn=expectEarningsB.divide(new BigDecimal(deadlineMonth),2,BigDecimal.ROUND_HALF_UP);
            BigDecimal everyDayReturn=expectEarningsB.divide(new BigDecimal(deadline),2,BigDecimal.ROUND_HALF_UP);
            //创建结束时间
            Calendar endAtCalender=Calendar.getInstance();
            endAtCalender.clear();
            endAtCalender.setTimeInMillis(endAt);
            //创建开始时间
            Calendar startAtCalender=Calendar.getInstance();
            startAtCalender.clear();
            startAtCalender.setTimeInMillis(startAt);
            System.out.println("开始时间为"+startAtCalender.get(Calendar.YEAR)+startAtCalender.get(Calendar.MONTH)+startAtCalender.get(Calendar.DAY_OF_MONTH));
            for(int i=0;i<(deadlineMonth+1);i++){
                //判断，前deadlineMonth次是返息，其中第一次的利息比较特殊
                if(i==0){
                    //设置性质
                    newTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
                    //设置时间,需判断开始开始时间的日期是否小于20
                    //取得开始时间的日
                    int dayOfStartAt=startAtCalender.get(Calendar.DAY_OF_MONTH);
                    //比较，如果小于20，就在本月进行第一次
                    if(dayOfStartAt<20){
                        //设置时间日为20号
                        startAtCalender.set(Calendar.DAY_OF_MONTH,20);
                        //转化为毫秒设置到定时任务中
                        newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                        //设置第一个月的返息
                        BigDecimal firstMonthReturn=everyDayReturn.multiply(new BigDecimal(20-dayOfStartAt));
                        newTimedTask.setMoney(firstMonthReturn.toString());
                        System.out.println("第一次返息定时任务详情"+newTimedTask);
                        //添加定时任务并计算未返收益
                        e=timedTaskMapper.addTaskedTime(newTimedTask);
                        notReturn=notReturn.subtract(firstMonthReturn);
                    }
                    //如果是大于等于20号的情况，则在下月20号第一次
                    else {
                        //设置时间日为20号
                        startAtCalender.set(Calendar.DAY_OF_MONTH,20);
                        //设置月份为下个月
                        startAtCalender.set(Calendar.MONTH,startAtCalender.get(Calendar.MONTH)+1);
                        //转化为毫秒设置到定时任务中
                        newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                        //设置第一个月的返息
                        BigDecimal firstMonthReturn=everyDayReturn.multiply(new BigDecimal(50-dayOfStartAt));
                        newTimedTask.setMoney(firstMonthReturn.toString());
                        System.out.println("第一次返息定时任务详情"+newTimedTask);
                        //添加定时任务并计算未返收益
                        e=timedTaskMapper.addTaskedTime(newTimedTask);
                        notReturn=notReturn.subtract(firstMonthReturn);
                    }
                }
                //其余的标准情况
                if(i>0&&i<deadlineMonth){
                    //设置性质
                    newTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
                    //设置时间为下个月20号
                    startAtCalender.set(Calendar.MONTH,startAtCalender.get(Calendar.MONTH)+1);
                    //转化为毫秒设置到定时任务中
                    newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                    //设置金额
                    newTimedTask.setMoney(everyMonthReturn.toString());
                    System.out.println("第"+(i+1)+"月返息定时任务详情"+newTimedTask);
                    e=timedTaskMapper.addTaskedTime(newTimedTask);
                    //计算剩余未返金额
                    notReturn=notReturn.subtract(everyMonthReturn);
                }
                //最后一次也就是本加最后一次利息
                if(i==deadlineMonth){
                    //设置性质
                    newTimedTask.setNature(TimedTask.NATURE_RETURN_PRINCIPAL);
                    //设置时间,直接使用最后结束时间即可
                    newTimedTask.setTaskTime(endAtCalender.getTimeInMillis());
                    //设置金额,未返回加本金
                    newTimedTask.setMoney(notReturn.add(moneyB).toString());
                    System.out.println("最后一次定时任务详情"+newTimedTask);
                    e=timedTaskMapper.addTaskedTime(newTimedTask);
                }
            }
        }
        //添加定时改变续投状态的任务
        TimedTask renewalTask=new TimedTask();
        renewalTask.setCreateAt(System.currentTimeMillis());
        renewalTask.setCreateBy(userId);
        renewalTask.setNature(TimedTask.NATURE_RENEWAL);
        renewalTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
        renewalTask.setTransactionId(newId);
        //设置定时任务时间为交易到期时间-续投提醒时间
        long investmentDay=Long.parseLong(systemDataMapper.getInvestmentDay());
         renewalTask.setTaskTime(endAt-investmentDay*24*3600*1000);
        System.out.println("改变续投状态的定时任务详情"+renewalTask);
        int f=timedTaskMapper.addTaskedTime(renewalTask);
        //添加消息中心消息
        Message message=new Message();
        message.setCreateAt(System.currentTimeMillis());
        message.setCreateBy(userId);
        message.setTitle("续投成功");
        message.setIntroduce(productName+"已续投成功");
        message.setTransactionId(newId);
        message.setUserId(userId);
        int g=messageMapper.addMessage(message);
        System.out.println("新建消息id为"+message.getId());
        return a*b*c*d*e*f*g;
    }

    /**
     * 获得用户投资列表
     * @param userId 用户id
     * @param status 投资状态
     * @return 用户指定状态的投资列表
     */
    @Override
    public List<TransactionListRO> getTransactionListRO(long userId, int status) {
        return transactionMapper.getTransListByUserIdAndStatus(status,userId);
    }

    /**
     * 获得用户指定投资详情
     * @param id 交易id
     * @return 交易详情
     */
    @Override
    public TransactionRO getTransactionById(long id) {
        TransactionRO ro=transactionMapper.getTransInfoByTransId(id);
        long userId=ro.getUserId();
        long defaultCard=userMapper.getDefaultCardById(userId);
        BankCard bankCard=bankCardMapper.getBankIdById(defaultCard);
        ro.setDefaultCardId(bankCard.getBankCard());
        String bankName=bankMapper.getBankNameById(bankCard.getBankId());
        ro.setDefaultCardBankName(bankName);
        long contractId=contractMapper.getContractIdByCode(ro.getContractCode());
        ro.setContractId(contractId);
        return ro;
    }


}
