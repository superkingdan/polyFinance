package com.jnshu.service1.Impl;

import com.jnshu.dao.*;
import com.jnshu.dto1.TransactionListRO;
import com.jnshu.dto1.TransactionRO;
import com.jnshu.entity.*;
import com.jnshu.exception.MyException;
import com.jnshu.service1.TransactionService1;
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
public class TransactionServiceImpl1 implements TransactionService1 {
    private static final Logger log= LoggerFactory.getLogger(TransactionService1.class);

    @Autowired
    TransactionMapper1 transactionMapper1;
    @Autowired
    ProductMapper1 productMapper1;
    @Autowired
    ContractMapper1 contractMapper1;
    @Autowired
    TimedTaskMapper1 timedTaskMapper1;
    @Autowired
    SystemDataMapper1 systemDataMapper1;
    @Autowired
    MessageMapper1 messageMapper1;
    @Autowired
    UserMapper1 userMapper1;
    @Autowired
    BankCardMapper1 bankCardMapper1;
    @Autowired
    BankMapper1 bankMapper1;

    /**
     * 获得用户可续投列表，需要筛选用户的投资中产品状态为在售，产品为不限购，续投状态为可续投的产品
     * @param id 用户id
     * @return 用户可续投列表
     */
    @Override
    public List<TransactionListRO> getContinueInvList(long id) throws Exception{
        log.info("开始查询用户"+id+"的可续投列表");
        List<TransactionListRO> list;
        try{
            list=transactionMapper1.getRenewalListByUserId(id);
        }catch (Exception e){
            throw new MyException(-1,"查询可续投列表出错");
        }
        return list;
    }

    /**
     * 添加续投订单,2E
     * @param oldId 原订单id
     * @param userSign 用户签名
     * @return 添加结果
     */
    @Override
    public long addRenewal(long oldId, String userSign) throws Exception{
        //首先根据旧id查询旧订单相关情况，用户id,起息时间，结束时间，金额，预期收益，产品id
        log.info("添加续投订单");
        try {
            Transaction oldTransaction;
            try {
                oldTransaction = transactionMapper1.getOldTransactionById(oldId);
            } catch (Exception e) {
                log.error("获取旧交易详情失败");
                throw new MyException(-1, "获取旧交易详情失败");
            }
            if (oldTransaction == null) {
                throw new MyException(-1, "无对应旧交易详情");
            }
            long userId = oldTransaction.getUserId();
            long productId = oldTransaction.getProductId();
            long oldEndAt = oldTransaction.getEndAt();
            String money = oldTransaction.getMoney();
            BigDecimal moneyB = new BigDecimal(money);
            String expectEarnings = oldTransaction.getExpectEarnings();
            BigDecimal expectEarningsB = new BigDecimal(expectEarnings);

            //获得产品相关信息,还款类型，期限，产品名，利率，起息时间
            Product product;
            try {
                product = productMapper1.getRefundInfoById(productId);
            } catch (Exception e) {
                log.error("获取产品详情失败");
                throw new MyException(-1, "获取产品详情失败");
            }
            if (product == null) {
                throw new MyException(-1, "无对应产品详情");
            }
            String productCode = product.getProductCode();
            int rateOfInterest = product.getRateOfInterest();
            int deadline = product.getDeadline();
            int deadlineMonth = deadline / 30;
            int refundStyle = product.getRefundStyle();
            String productName = product.getProductName();

            //查找最新合同号
            String newestContractCode;
            try {
                newestContractCode = contractMapper1.getNewestContractCode();
            } catch (Exception e) {
                log.error("获取最新合同编号失败");
                throw new MyException(-1, "获取最新合同编号失败");
            }
            if (newestContractCode==null){
                newestContractCode="";
            }
            System.out.println("最新合同号为" + newestContractCode);
            //生成合同编号
            String contractCode = TransString.transContractCode(newestContractCode, productCode);
           log.info("新合同编号为" + contractCode);
            //创建新合同
            Contract contract = new Contract();
            contract.setCreateAt(System.currentTimeMillis());
            contract.setCreateBy(userId);
            contract.setUserSign(userSign);
            contract.setContractCode(contractCode);
            contract.setIsPay(Contract.IS_PAY_YES);
            contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_NO);
            //添加合同表数据
            try {
                contractMapper1.addContractCode(contract);
            } catch (Exception e) {
                log.error("创建新合同失败");
                throw new MyException(-1, "创建新合同失败");
            }
            log.info("新合同的id为" + contract.getId());
            //创建新交易
            Transaction newTransaction = new Transaction();
            newTransaction.setCreateAt(System.currentTimeMillis());
            newTransaction.setCreateBy(userId);
            newTransaction.setContractCode(newestContractCode);
            long startAt = oldEndAt + rateOfInterest * 3600 * 24 * 1000;
            newTransaction.setStartAt(startAt);
            long endAt;
            //计算结束时间，开始时间转化为date形式加上deadline转化为月份
            //如果交易天数小于一个月就按照实际天数计算，不过要加一天
            if (deadline < 30) {
                endAt = startAt + (deadline + 1) * 24 * 3600 * 1000;
            }
            //如果交易天数大于一个月就按照月份加当前月份，日期加一来计算时间
            else {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                //设置起息时间为开始时间
                calendar.setTimeInMillis(startAt);
                //将此月份加deadline/30个月
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + (deadlineMonth));
                //再将日期加一天
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                //转化为时间戳
                endAt = calendar.getTimeInMillis();
            }
            System.out.println("结束时间为" + endAt);
            newTransaction.setEndAt(endAt);
            newTransaction.setRenuwalStatus(Transaction.RENUWAL_STATUS_NOT);
            newTransaction.setMoney(money);
            newTransaction.setExpectEarnings(expectEarnings);
            newTransaction.setReturned("0");
            newTransaction.setNotReturn(expectEarnings);
            newTransaction.setProductId(productId);
            newTransaction.setStatus(Transaction.STATUS_INVESTING);
            newTransaction.setContractCode(contractCode);
            newTransaction.setUserId(userId);
            //添加新交易
            try {
                transactionMapper1.addTransaction(newTransaction);
            } catch (Exception e) {
                log.error("创建新交易失败");
                throw new MyException(-1, "创建新交易失败");
            }
            long newId = newTransaction.getId();
            log.info("新交易的id为" + newId);
            //修改旧交易
            oldTransaction.setUpdateBy(userId);
            oldTransaction.setUpdateAt(System.currentTimeMillis());
            oldTransaction.setRenuwalStatus(Transaction.RENUWAL_STATUS_HAVE);
            try {
                transactionMapper1.updateRenuwalStatus(oldTransaction);
            } catch (Exception e) {
                log.error("修改旧交易失败");
                throw new MyException(-1, "修改旧交易失败");
            }
            //修改原定时任务
            TimedTask oldTimedTask = new TimedTask();
            oldTimedTask.setUpdateAt(System.currentTimeMillis());
            oldTimedTask.setUpdateBy(userId);
            oldTimedTask.setTransactionId(oldId);
            //如果是本息一次还，修改原定时任务金额为预期收益
            if (refundStyle == 1) {
                oldTimedTask.setMoney(expectEarnings);
            }
            //如果是先息后本修改最后一次回本金的任务的金额为减去本金
            else {
                //获取原本最后一次
                String lastTotalMoney = timedTaskMapper1.getOldMoneyByTransactionId(oldId);
                BigDecimal lastTotalMoneyB = new BigDecimal(lastTotalMoney);
                String lastInterestMoney = lastTotalMoneyB.subtract(moneyB).toString();
                oldTimedTask.setMoney(lastInterestMoney);
            }
            //修改原定时任务性质为返息
            oldTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
            try {
                timedTaskMapper1.updateOldTransByTransId(oldTimedTask);
            } catch (Exception e) {
                log.error("修改旧定时任务失败");
                throw new MyException(-1, "修改旧定时任务失败");
            }
            System.out.println("修改原定时任务成功");
            //添加新定时任务
            //如果是本息一次还，添加回本金任务
            if (refundStyle == 1) {
                TimedTask newTimedTask = new TimedTask();
                newTimedTask.setCreateAt(System.currentTimeMillis());
                newTimedTask.setCreateBy(userId);
                newTimedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
                //结束时间一天之后开始返款
                newTimedTask.setTaskTime(endAt + 24 * 3600 * 1000);
                newTimedTask.setNature(TimedTask.NATURE_RETURN_PRINCIPAL);
                //返回金额就是预期收益加金额
                String totalMoney = moneyB.add(expectEarningsB).toString();
                newTimedTask.setMoney(totalMoney);
                newTimedTask.setTransactionId(newId);
                try {
                    timedTaskMapper1.addTaskedTime(newTimedTask);
                } catch (Exception e) {
                    log.error("添加新本息一次还任务失败");
                    throw new MyException(-1, "添加新本息一次还定时任务失败");
                }
                log.info("本息一次还的还款定时任务id为" + newTimedTask.getId());
            }
            //如果是先息后本，添加deadlineMonth次返息任务，一次本带最后一次利息任务
            else {
                TimedTask newTimedTask = new TimedTask();
                newTimedTask.setCreateAt(System.currentTimeMillis());
                newTimedTask.setCreateBy(userId);
                newTimedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
                newTimedTask.setTransactionId(newId);
                //循环，设置金额，时间，任务性质
                //因为七天的新手礼包是本息一次还，所以这里不用考虑deadline小于30的情况
                //循环deadlineMonth+1次，其中最后一次是本加最后一次利息，前deadlineMonth是只有利息
                BigDecimal notReturn = expectEarningsB;
                BigDecimal everyMonthReturn = expectEarningsB.divide(new BigDecimal(deadlineMonth), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal everyDayReturn = expectEarningsB.divide(new BigDecimal(deadline), 2, BigDecimal.ROUND_HALF_UP);
                //创建结束时间
                Calendar endAtCalender = Calendar.getInstance();
                endAtCalender.clear();
                endAtCalender.setTimeInMillis(endAt);
                //创建开始时间
                Calendar startAtCalender = Calendar.getInstance();
                startAtCalender.clear();
                startAtCalender.setTimeInMillis(startAt);
                for (int i = 0; i < (deadlineMonth + 1); i++) {
                    //判断，前deadlineMonth次是返息，其中第一次的利息比较特殊
                    if (i == 0) {
                        //设置性质
                        newTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
                        //设置时间,需判断开始开始时间的日期是否小于20
                        //取得开始时间的日
                        int dayOfStartAt = startAtCalender.get(Calendar.DAY_OF_MONTH);
                        //比较，如果小于20，就在本月进行第一次
                        if (dayOfStartAt < 20) {
                            //设置时间日为20号
                            startAtCalender.set(Calendar.DAY_OF_MONTH, 20);
                            //转化为毫秒设置到定时任务中
                            newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                            //设置第一个月的返息
                            BigDecimal firstMonthReturn = everyDayReturn.multiply(new BigDecimal(20 - dayOfStartAt));
                            newTimedTask.setMoney(firstMonthReturn.toString());
                            //添加定时任务并计算未返收益
                            try {
                                timedTaskMapper1.addTaskedTime(newTimedTask);
                            } catch (Exception e) {
                                log.error("创建第一次返息定时任务失败");
                                throw new MyException(-1, "创建第一次返息定时任务失败");
                            }
                            log.info("第一次返息定时任务Id为"+newTimedTask.getId());
                            notReturn = notReturn.subtract(firstMonthReturn);
                        }
                        //如果是大于等于20号的情况，则在下月20号第一次
                        else {
                            //设置时间日为20号
                            startAtCalender.set(Calendar.DAY_OF_MONTH, 20);
                            //设置月份为下个月
                            startAtCalender.set(Calendar.MONTH, startAtCalender.get(Calendar.MONTH) + 1);
                            //转化为毫秒设置到定时任务中
                            newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                            //设置第一个月的返息
                            BigDecimal firstMonthReturn = everyDayReturn.multiply(new BigDecimal(50 - dayOfStartAt));
                            newTimedTask.setMoney(firstMonthReturn.toString());
                            //添加定时任务并计算未返收益
                            try {
                                timedTaskMapper1.addTaskedTime(newTimedTask);
                            } catch (Exception e) {
                                log.error("创建第一次返息定时任务失败");
                                throw new MyException(-1, "创建第一次返息定时任务失败");
                            }
                            log.info("第一次返息定时任务Id为"+newTimedTask.getId());
                            notReturn = notReturn.subtract(firstMonthReturn);
                        }
                    }
                    //其余的标准情况
                    if (i > 0 && i < deadlineMonth) {
                        //设置性质
                        newTimedTask.setNature(TimedTask.NATURE_RETURN_INTEREST);
                        //设置时间为下个月20号
                        startAtCalender.set(Calendar.MONTH, startAtCalender.get(Calendar.MONTH) + 1);
                        //转化为毫秒设置到定时任务中
                        newTimedTask.setTaskTime(startAtCalender.getTimeInMillis());
                        //设置金额
                        newTimedTask.setMoney(everyMonthReturn.toString());
                        try {
                            timedTaskMapper1.addTaskedTime(newTimedTask);
                        } catch (Exception e) {
                            log.error("创建第" + (i + 1) + "次返息定时任务失败");
                            throw new MyException(-1, "创建第一次返息定时任务失败");
                        }
                        log.info("第" + (i + 1) + "月返息定时任务id为" + newTimedTask.getId());
                        //计算剩余未返金额
                        notReturn = notReturn.subtract(everyMonthReturn);
                    }
                    //最后一次也就是本加最后一次利息
                    if (i == deadlineMonth) {
                        //设置性质
                        newTimedTask.setNature(TimedTask.NATURE_RETURN_PRINCIPAL);
                        //设置时间,直接使用最后结束时间即可
                        newTimedTask.setTaskTime(endAtCalender.getTimeInMillis());
                        //设置金额,未返回加本金
                        newTimedTask.setMoney(notReturn.add(moneyB).toString());
                        try {
                            timedTaskMapper1.addTaskedTime(newTimedTask);
                        } catch (Exception e) {
                            log.error("创建最后一次返息定时任务失败");
                            throw new MyException(-1, "创建最后一次返息定时任务失败");
                        }
                        log.info("最后一次定时任务id为" + newTimedTask.getId());
                    }
                }
            }
            //添加定时改变续投状态的任务
            TimedTask renewalTask = new TimedTask();
            renewalTask.setCreateAt(System.currentTimeMillis());
            renewalTask.setCreateBy(userId);
            renewalTask.setNature(TimedTask.NATURE_RENEWAL);
            renewalTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            renewalTask.setTransactionId(newId);
            //设置定时任务时间为交易到期时间-续投提醒时间
            long investmentDay = Long.parseLong(systemDataMapper1.getInvestmentDay());
            renewalTask.setTaskTime(endAt - investmentDay * 24 * 3600 * 1000);
            try {
                timedTaskMapper1.addTaskedTime(renewalTask);
            } catch (Exception e) {
                log.error("创建修改续投状态定时任务失败");
                throw new MyException(-1, "创建修改续投状态定时任务失败");
            }
            System.out.println("改变续投状态的定时任务id为" + renewalTask.getId());
            //添加消息中心消息
            Message message = new Message();
            message.setCreateAt(System.currentTimeMillis());
            message.setCreateBy(userId);
            message.setTitle("续投成功");
            message.setIntroduce(productName + "已续投成功");
            message.setTransactionId(newId);
            message.setUserId(userId);
            try {
                messageMapper1.addMessage(message);
            } catch (Exception e) {
                log.error("新增消息失败");
                throw new MyException(-1, "新增消息失败");
            }
            log.info("新建消息id为" + message.getId());
            return newId;
        }catch (Exception e){
            log.error("新增交易失败");
            throw new MyException(-1, "新增交易产生未知错误");
        }
    }

    /**
     * 获得用户投资列表
     * @param userId 用户id
     * @param status 投资状态
     * @return 用户指定状态的投资列表
     */
    @Override
    public List<TransactionListRO> getTransactionListRO(long userId, int status)throws Exception {
        log.info("获得用户"+userId+"投资列表");
        List<TransactionListRO> list;
        try{
            list=transactionMapper1.getTransListByUserIdAndStatus(status,userId);
        }catch (Exception e){
            throw new MyException(-1,"获得用户投资列表出错");
        }
        return list;
    }

    /**
     * 获得用户指定投资详情
     * @param id 交易id
     * @return 交易详情
     */
    @Override
    public TransactionRO getTransactionById(long id) throws Exception{
        log.info("获得用户指定投资"+id+"详情");
        try {
            TransactionRO ro;
            try {
                ro = transactionMapper1.getTransInfoByTransId(id);
            } catch (Exception e) {
                throw new MyException(-1, "获取交易信息失败");
            }
            if (ro == null) {
                throw new MyException(-1, "获取交易信息为空");
            }
            long userId = ro.getUserId();
            Long defaultCard;
            try {
                defaultCard = userMapper1.getDefaultCardById(userId);
            } catch (Exception e) {
                throw new MyException(-1, "获取默认银行卡失败");
            }
            //如果两张卡都被解绑，就设置其为无银行卡
            if (defaultCard == 0) {
                ro.setDefaultCardId("0000");
                ro.setDefaultCardBankName("无默认卡");
            }else {
                //查询并设置银行卡号
                BankCard bankCard;
                try {
                    bankCard = bankCardMapper1.getBankIdById(defaultCard);
                    ro.setDefaultCardId(bankCard.getBankCard());
                } catch (Exception e) {
                    throw new MyException(-1, "获取银行卡信息失败");
                }
                //查询并设置银行名
                String bankName;
                try {
                    bankName = bankMapper1.getBankNameById(bankCard.getBankId());
                } catch (Exception e) {
                    throw new MyException(-1, "获取银行名失败");
                }
                ro.setDefaultCardBankName(bankName);
            }
            //查询并设置合同id
            Long contractId;
            try {
                contractId = contractMapper1.getContractIdByCode(ro.getContractCode());
                ro.setContractId(contractId);
            } catch (Exception e) {
                throw new MyException(-1, "获取银行名为空");
            }
            return ro;
        }catch (Exception e) {
            throw new MyException(-1, "获取指定投资产生未知错误");
        }
    }


}
