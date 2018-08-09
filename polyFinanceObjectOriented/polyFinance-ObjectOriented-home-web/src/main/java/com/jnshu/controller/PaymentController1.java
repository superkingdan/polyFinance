package com.jnshu.controller;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.dto1.TransactionRO;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import com.jnshu.service1.PaymentService1;
import com.jnshu.service1.TransactionService1;
import com.jnshu.utils.ossutils.ConvertToFile;
import com.jnshu.utils.ossutils.OSSUploadUtil;
import com.jnshu.utils.payutils.ConfigReader;
import com.jnshu.utils.CookieUtil;
import com.jnshu.utils.payutils.HttpFormUtil;
import com.jnshu.utils.payutils.HttpPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付模块相关接口,1E
 * @author wangqichao
 */
@RestController
public class PaymentController1 {
    private static final Logger log= LoggerFactory.getLogger(PaymentController1.class);

    @Autowired
    PaymentService1 paymentService;
    @Autowired
    TransactionService1 transactionService;

    private static final String PAY_URL = ConfigReader.getString("h5.pay_url");

    /**
     * 获得投资界面用户银行卡信息
     * @return 用户银行卡信息
     */
    @GetMapping(value = "/a/u/r/pay/investment")
    public Map getInvestment(HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        //从cookie获得id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            log.info("获取用户银行卡，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        log.info("获得id为"+id+"的用户的银行卡信息");
        List<BankCardRO> ros;
        try {
            ros = paymentService.getInvestment(id);
        }catch (Exception e){
            log.error("获得id为"+id+"的用户的银行卡信息时发生错误");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("data",ros);
        return map;
    }

    /**
     * 添加续投订单
     * @param id 原订单id
     * @param userSign 用户签名
     * @return 返回参数，code,message
     */
    @PostMapping(value = "/a/u/r/pay/continue")
    public Map addRenewal(@RequestParam(value = "id",required = false)Long id,@RequestParam(value="userSign",required = false)String userSign)throws Exception{
        if(id==null||userSign==null){
            throw new MyException(10002,"参数不能为空");
        }
        log.info("添加续投订单，原交易id为"+id);
        try {
            transactionService.addRenewal(id, userSign);
        }catch (Exception e){
            log.error("添加续投订单出错，原交易id为"+id);
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 创建合同并返回富有支付信息和合同id
     * @param rpo 请求数据
     * @return 返回参数，code,message,合同id
     */
    @PostMapping(value = "/a/u/r/pay/contract")
    public Map addUserSign(PaymentRPO rpo,HttpServletRequest request)throws Exception{
        if(rpo.getUserSign()==null||rpo.getProductId()==null||rpo.getBankCardId()==null||rpo.getMoney()==null){
            throw new MyException(10002,"参数不能为空");
        }
        Map<String,Object> map=new HashMap<>();
        //获取用户id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            log.info("创建合同，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        log.info("用户"+id+"签署完合同，开始支付");
        rpo.setUserId(id);
        System.out.println("开始创建合同");
        long contractId;
        try {
            contractId = paymentService.addContract(rpo.getUserSign(), rpo.getProductId(), rpo.getUserId());
        }catch (Exception e){
            log.error("用户"+id+"开始创建合同,但是出错了");
            log.error(e.getMessage());
            throw new MyException(-1,"已购买过新手礼包");
        }
        rpo.setContractId(contractId);
        System.out.println("开始生成交易流水");
        long transactionLogId;
        User user;
        BankCard bankCard;
        try {
            transactionLogId = paymentService.addPayTransactionLog(rpo);
            user = paymentService.getUserInfo(id);
            bankCard = paymentService.getBankCard(rpo.getBankCardId());
        }catch (Exception e){
            log.error("用户"+id+"开始创建交易流水,但是出错了");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        //转化基本信息为需要的map形式
        //首先将金额转化为分为单位
//        System.out.println("金额为"+rpo.getMoney());
        BigDecimal moneyCent;
        try {
            moneyCent=new BigDecimal(rpo.getMoney()).multiply(BigDecimal.valueOf(100));
        }catch (Exception e){
            log.error("开始转化金额，但是金额有误");
            log.error(e.getMessage());
            throw new MyException(-1,"金额输入有误");
        }
        String respMsg="";
        //向富友发起请求，并将结果返回
        try {
            Map<String,String> param= HttpPay.transParam(transactionLogId,id,moneyCent.toString(),bankCard.getBankCard(),user.getRealName(),user.getIdCard());
            respMsg = HttpFormUtil.formForward(PAY_URL, param);
        }catch (Exception e){
            log.error("用户"+id+"开始向富友发送请求,但是出错了");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("respMsg",respMsg);
        map.put("contractId",contractId);
        System.out.println("respMsg:"+respMsg);
        return map;
    }


    /**
     * 回调接口
     * @param request 回调参数
     */
    @PostMapping(value = "/a/pay/callback")
    public void callBack(HttpServletRequest request)throws Exception{
        //首先从请求中将数据拿出来
        // 其中只有响应码，商户号，商户订单号，富友订单号，银行卡号，金额比较重要，需要提取出来做MD5加密对比用
        String responseCode=request.getParameter("RESPONSECODE");
        String mchntCd=request.getParameter("MCHNTCD");
        String mchntOrderId=request.getParameter("MCHNTORDERID");
        String orderId=request.getParameter("ORDERID");
        String bankCard=request.getParameter("BANKCARD");
        String amt=request.getParameter("AMT");
        String sign=request.getParameter("SIGN");
        log.info("回调接口被调用，交易流水号为"+mchntOrderId);
        //然后将其加密一遍，看是否被修改过
        Boolean comparedResult=HttpPay.comparedParam(responseCode,mchntCd,mchntOrderId,orderId,bankCard,amt,sign);
        if(responseCode.equals("0000")&&comparedResult) {
            //修改交易流水表之前的数据,并返回合同id
            long contractId;
            try {
                contractId = paymentService.updateTransactionLog(Long.valueOf(mchntOrderId));
            }catch (Exception e){
                log.error("交易流水号为"+mchntOrderId+"的流水在回调时修改交易流水表数据出错");
                log.error(e.getMessage());
                throw new MyException(-1,"未知错误");
            }
            //验证通过，修改合同表之前的数据,并返回合同编号
            String contractCode;
            try {
                contractCode=paymentService.updateContract(contractId);
            }catch (Exception e){
                log.error("交易流水号为"+mchntOrderId+"的流水在回调时修改合同表数据出错");
                log.error(e.getMessage());
                throw new MyException(-1,"未知错误");
            }
            //创建交易表新数据
            long transactionId;
            try{
                transactionId=paymentService.addTransaction(Long.valueOf(mchntOrderId),contractCode);
            }catch (Exception e){
                log.error("交易流水号为"+mchntOrderId+"的流水在回调时创建新交易数据出错");
                log.error(e.getMessage());
                throw new MyException(-1,"未知错误");
            }
            log.info("创建了交易，交易id为："+transactionId);
        }else {
            log.error("回调出现问题，无法创建订单，问题流水号为："+mchntOrderId);
        }
    }

    /**
     * 交易成功之后根据合同id获得刚才的交易详情
     * @param contractId 合同id
     * @return 交易详情
     */
    @GetMapping(value = "/a/u/r/pay/transaction/{id}")
    public Map getTransactionByContractId(@PathVariable(value = "id")Long contractId)throws Exception{
        log.info("付款成功后通过付款成功界面查看交易详情，请求合同id为"+contractId);
        //通过合同id查找对应的合同code，然后将其转化为交易id
        long transactionId;
        try{
            transactionId=paymentService.getTransactionIdByContractId(contractId);
        }catch (Exception e){
            log.error("获得交易id失败");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        //调用transactionService的利用交易id查询交易详情的方法，直接查询
        Map<String,Object> map=new HashMap<>();
        TransactionRO ro;
        try{
            ro=transactionService.getTransactionById(transactionId);
        }catch (Exception e){
            log.error("查询交易详情失败");
            log.error(e.getMessage());
            throw new MyException(-1,"未知错误");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }

    /**
     * 上传图片
     * @param file 用户数字签名
     * @return 签名Url
     * @throws Exception
     */
    @PostMapping(value = "/a/u/r/pay/uploadimg",produces = "application/json;charset=UTF-8")
    public Map uploadUserSign(@RequestParam(required = false) MultipartFile file, HttpServletRequest request)throws Exception{
        Map<String,Object> map=new HashMap<>();
        //获取用户id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            log.error("上传图片，但是cookie中没有uid");
            throw new MyException(10001,"授权已过期，请重新登录");
        }
        log.info("用户"+id+"签署合同中，在上传签名");
        //判断是否有图片上传
        if(file==null||file.equals("")||file.getSize()<=0){
           log.error("用户"+id+"想上传图片，但是无上传图片，很尴尬");
           throw new MyException(10030,"文件不能为空");
        }
        //判断是否为图片类型
        File f= ConvertToFile.multipartFileToFile(file);//转化文件格式
        String fName=f.getName();
        System.out.println(fName);
//        //判断是否为图片格式
//        if(!IsImage.isImage(fName)){
//            //如果不是就删除临时文件，并报错
//            File del = new File(f.toURI());
//            boolean delete=del.delete();
//            System.out.println(delete);
//            map.put("code",10031);
//            map.put("message","there is error file");
//            log.info("图片格式不正确，很尴尬");
//            return map;
//        }
        String fileUrl="";
        try {
            fileUrl = OSSUploadUtil.uploadFile(f, "jpg");//上传文件,设置后缀为jpg
        }catch (Exception e){
            log.error("用户"+id+"上传图片发生问题");
            log.error(e.getMessage());
            //删除临时文件
            File del = new File(f.toURI());
            boolean delete=del.delete();
            System.out.println(delete);
            throw new MyException(10032,"图片上传失败");
        }
        map.put("code",0);
        map.put("message","success");
        map.put("signUrl",fileUrl);
        log.info("上传成功，图片url为"+fileUrl);
        //删除临时文件
        File del = new File(f.toURI());
        boolean delete=del.delete();
        System.out.println(delete);
        return map;
    }
}
