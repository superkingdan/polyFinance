package com.jnshu.service1.Impl;

import com.jnshu.dao.*;
import com.jnshu.dto1.BankCardRO;
import com.jnshu.entity.*;
import com.jnshu.exception.MyException;
import com.jnshu.service1.PaymentService1;
import com.jnshu.utils.TransString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * 支付相关service
 * @author wangqichao
 */
@Service
@Transactional
public class PaymentServiceImpl1 implements PaymentService1 {
    private static final Logger log= LoggerFactory.getLogger(PaymentServiceImpl1.class);

    @Autowired
    BankCardMapper1 bankCardMapper1;
    @Autowired
    ContractMapper1 contractMapper1;
    @Autowired
    ProductMapper1 productMapper1;
    @Autowired
    BankMapper1 bankMapper1;
    @Autowired
    TransactionLogMapper1 transactionLogMapper1;
    @Autowired
    UserMapper1 userMapper1;
    @Autowired
    TimedTaskMapper1 timedTaskMapper1;
    @Autowired
    TransactionMapper1 transactionMapper1;
    @Autowired
    SystemDataMapper1 systemDataMapper1;
    @Autowired
    MessageMapper1 messageMapper1;

    /**
     * 获得用户银行卡信息
     * @param id 用户id
     * @return 用户银行卡信息
     */
    @Override
    public List<BankCardRO> getInvestment(long id) {
        log.info("获取用户"+id+"的银行卡信息");
        return bankCardMapper1.getBankCardInfoByUserId(id);
    }

    /**
     *根据用户签名，用户id和产品id生成新的合同
     * @param userSign 用户签名
     * @param productId 产品id
     * @param userId 用户id
     * @return 合同id
     */
    @Override
    public Long addContract(String userSign, long productId, long userId) throws Exception{
        //查找用户是否够买过新手礼包以及此产品是否是新手礼包
        Product product=productMapper1.getProductById(productId);
        if(product.getDeadline()<30){
            Integer isNew=userMapper1.getIsNewById(userId);
            if(isNew!=null){
                throw new MyException(-1,"已购买过新手礼");
            }
        }
        //查找最新合同号,如果没有数据就传""
        String newestContractCode;
        try{
            newestContractCode= contractMapper1.getNewestContractCode();
        }catch (Exception e){
            newestContractCode="";
        }
        System.out.println("最新合同号为"+newestContractCode);
        //查找产品id对应的productCode
        String productCode= product.getProductCode();
        //生成合同编号
        String contractCode= TransString.transContractCode(newestContractCode,productCode);
        System.out.println("新合同编号为"+contractCode);
        Contract contract=new Contract();
        contract.setCreateAt(System.currentTimeMillis());
        contract.setCreateBy(userId);
        contract.setUserSign(userSign);
        contract.setContractCode(contractCode);
        contract.setIsPay(Contract.IS_PAY_NO);
        contract.setIsMatchingClaims(Contract.IS_MATCHING_CLAIMS_NO);
        //添加到合同表中去
        contractMapper1.addContractCode(contract);
        return contract.getId();
    }

    /**
     * 生成交易流水
     * @param rpo 需要的信息
     * @return 生成的流水id
     */
    @Override
    public Long addPayTransactionLog(PaymentRPO rpo) {
        //创建新的交易流水对象
        TransactionLog transactionLog=new TransactionLog();
        transactionLog.setCreateAt(System.currentTimeMillis());
        transactionLog.setCreateBy(rpo.getUserId());
        transactionLog.setUserId(rpo.getUserId());
        transactionLog.setContractId(rpo.getContractId());
        //查询产品名
        String productName= productMapper1.getProductNameByProductId(rpo.getProductId());
        transactionLog.setProductName(productName);
        transactionLog.setTransactionAt(System.currentTimeMillis());
        transactionLog.setMoney(rpo.getMoney());
        transactionLog.setStatus(TransactionLog.STATUS_PAY_FAIL);
        BankCard card= bankCardMapper1.getBankIdById(rpo.getBankCardId());
        String bankName= bankMapper1.getBankNameById(card.getBankId());
        String transactionWay=bankName+","+card.getBankCard();
        transactionLog.setTransactionWay(transactionWay);
        //插入交易流水
        int a= transactionLogMapper1.addTransactionLog(transactionLog);
        return transactionLog.getId();
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户真实信息
     */
    @Override
    public User getUserInfo(long userId) {
        return userMapper1.getUserRealInfo(userId);
    }

    /**
     * 获取用户银行卡信息
     * @param bankCardId 银行卡id
     * @return 银行卡信息
     */
    @Override
    public BankCard getBankCard(long bankCardId) {
        return bankCardMapper1.getBankIdById(bankCardId);
    }

    /**
     * 更新交易流水表
     * @param transactionLogId 交易流水Id
     * @return 合同id
     */
    @Override
    public Long updateTransactionLog(long transactionLogId,String bankLog) {
        TransactionLog log= transactionLogMapper1.getTransLogById(transactionLogId);
        long contractId=log.getContractId();
        log.setUpdateAt(System.currentTimeMillis());
        log.setUpdateBy(log.getCreateBy());
        log.setId(transactionLogId);
        log.setStatus(TransactionLog.STATUS_PAY_SUCCESS);
        log.setBankLog(bankLog);
        transactionLogMapper1.updateTransactionLogById(log);
        return contractId;
    }

    /**
     * 更新合同表
     * @param contractId 合同id
     * @return 合同code
     */
    @Override
    public String updateContract(long contractId) {
        Contract contract= contractMapper1.getContractById(contractId);
        contract.setIsPay(Contract.IS_PAY_YES);
        contract.setUpdateAt(System.currentTimeMillis());
        contract.setUpdateBy(contract.getCreateBy());
        //更新合同表
        contractMapper1.updateIsPay(contract);
        contractMapper1.updateIsPay(contract);
        return contract.getContractCode();
    }

    /**
     *创建相应的交易，以及定时任务，消息
     * @param transactionLogId 交易流水id
     * @param contractCode 合同code
     * @return 交易id
     */
    @Override
    public Long addTransaction(long transactionLogId,String contractCode) {
        //根据交易流水表id查找交易流水相关信息
        TransactionLog log= transactionLogMapper1.getTransLogById(transactionLogId);
        long userId=log.getUserId();
        String productName=log.getProductName();
        long contractId=log.getContractId();
        String money=log.getMoney();

        //根据产品名查找产品相关信息
        Product product= productMapper1.getProductByName(productName);
        int rateOfInterest=product.getRateOfInterest();
        long productId=product.getId();
        int deadline=product.getDeadline();
        //如果产品期限小于30天就是新手礼包，将其is_new字段修改为1
        if(deadline<30){
         userMapper1.updateIsNewById(userId);
        }
        int refundStyle=product.getRefundStyle();
        int isLimitedPurchase=product.getIsLimitePurchase();
        //将时限转化为月，但是新手礼包是用不到这个的
        int deadlineMonth=deadline/30;
        String interestRate=product.getInterestRate();
        //创建交易信息
        Transaction transaction=new Transaction();
        transaction.setCreateAt(System.currentTimeMillis());
        transaction.setCreateBy(userId);
        transaction.setUserId(userId);
        //计算起息时间
        long startAt=System.currentTimeMillis()+rateOfInterest*24*3600*1000L;
        transaction.setStartAt(startAt);
        //计算结束时间
        long endAt=startAt+deadline*24*3600*1000L;
        transaction.setEndAt(endAt);
        transaction.setRenuwalStatus(Transaction.RENUWAL_STATUS_NOT);
        transaction.setMoney(money);
        //计算预期收益
        BigDecimal moneyB=new BigDecimal(money);
        BigDecimal interestRateB=new BigDecimal(interestRate);
        BigDecimal expectEarningsB=moneyB.multiply(interestRateB).multiply(new BigDecimal(deadline)).divide(new BigDecimal(360),2,BigDecimal.ROUND_HALF_UP);
        transaction.setExpectEarnings(expectEarningsB.toString());
        transaction.setReturned("0");
        transaction.setNotReturn(expectEarningsB.toString());
        transaction.setProductId(productId);
        transaction.setStatus(Transaction.STATUS_INVESTING);
        transaction.setContractCode(contractCode);
        int d= transactionMapper1.addTransaction(transaction);
        long transactionId=transaction.getId();
        //创建定时任务,小于七天的不需要建立修改续投状态的定时任务，只有回款
        //如果是本息一次还，添加回本定时任务
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
            newTimedTask.setTransactionId(transactionId);
            e= timedTaskMapper1.addTaskedTime(newTimedTask);
            System.out.println("本息一次还的还款定时任务id为:"+newTimedTask.getId());
        }
        //如果是先息后本，添加deadlineMonth次返息任务，一次本带最后一次利息任务
        else{
            TimedTask newTimedTask=new TimedTask();
            newTimedTask.setCreateAt(System.currentTimeMillis());
            newTimedTask.setCreateBy(userId);
            newTimedTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            newTimedTask.setTransactionId(transactionId);
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
                        System.out.println("第一次返息定时任务详情:"+newTimedTask);
                        //添加定时任务并计算未返收益
                        e= timedTaskMapper1.addTaskedTime(newTimedTask);
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
                        System.out.println("第"+(i+1)+"月返息定时任务详情"+newTimedTask);
                        //添加定时任务并计算未返收益
                        e= timedTaskMapper1.addTaskedTime(newTimedTask);
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
                    System.out.println("第"+(i+1)+"月返息定时任务详情:"+newTimedTask);
                    e= timedTaskMapper1.addTaskedTime(newTimedTask);
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
                    System.out.println("最后一次定时任务详情:"+newTimedTask);
                    e= timedTaskMapper1.addTaskedTime(newTimedTask);
                }
            }
        }
        //添加定时改变续投状态的任务,可以续投的才可以改变
        int f=1;
        if(isLimitedPurchase==0) {
            TimedTask renewalTask = new TimedTask();
            renewalTask.setCreateAt(System.currentTimeMillis());
            renewalTask.setCreateBy(userId);
            renewalTask.setNature(TimedTask.NATURE_RENEWAL);
            renewalTask.setStatus(TimedTask.STATUS_NOT_EXECUTE);
            renewalTask.setTransactionId(transactionId);
            //设置定时任务时间为交易到期时间-续投提醒时间
            long investmentDay = Long.parseLong(systemDataMapper1.getInvestmentDay());
            renewalTask.setTaskTime(endAt - investmentDay * 24 * 3600 * 1000);
            System.out.println("改变续投状态的定时任务详情" + renewalTask);
            f= timedTaskMapper1.addTaskedTime(renewalTask);
        }
        //创建消息
        Message message=new Message();
        message.setCreateAt(System.currentTimeMillis());
        message.setCreateBy(userId);
        message.setTitle("投资成功");
        message.setIntroduce(productName+"已投资成功");
        message.setTransactionId(transactionId);
        message.setUserId(userId);
        int g= messageMapper1.addMessage(message);
        System.out.println("新建消息id为"+message.getId());
        //修改用户资产
        String property= userMapper1.getPropertyById(userId);
        BigDecimal newPropertyB=new BigDecimal(property).add(moneyB);
        User user=new User();
        user.setProperty(newPropertyB.toString());
        user.setId(userId);
        user.setUpdateAt(System.currentTimeMillis());
        user.setUpdateBy(userId);
        int h= userMapper1.updatePropertyById(user);
        return transactionId;
    }

    /**
     * 通过合同id获得交易id
     * @param contractId 合同id
     * @return 交易id
     */
    @Override
    public Long getTransactionIdByContractId(long contractId) {
        String contractCode= contractMapper1.getContractCodeById(contractId);
        return transactionMapper1.getTransactionIdByContractCode(contractCode);
    }

//    @Override
//    public Boolean judge(long id, HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) throws Exception{
//        User user= userMapper1.getUserRealStatusById(id);
//        if(user.getRealStatus()==1&&user.getDefaultCard()>0){
//            System.out.println("验证通过，放行");
//            return true;
//        }
//        //如果未实名就去未实名的接口，返回的code是10010
//        if(user.getRealStatus()!=1){
//            log.error("用户"+id+"未实名，跳转到返回错误信息为未实名的接口");
//            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unrealname");
//            return false;
//        }
//        else {
//            log.error("用户"+id+"未绑定银行卡，跳转到返回错误信息为未绑卡的接口");
//            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/nocard");
//            return false;
//        }
//    }
}
