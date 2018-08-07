package com.jnshu.controller;

import com.jnshu.dto1.BankCardRO;
import com.jnshu.dto1.TransactionRO;
import com.jnshu.entity.BankCard;
import com.jnshu.entity.PaymentRPO;
import com.jnshu.entity.User;
import com.jnshu.service1.PaymentService;
import com.jnshu.service1.TransactionService;
import com.jnshu.utils.ossutils.ConvertToFile;
import com.jnshu.utils.ossutils.IsImage;
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
 * 支付模块相关接口
 * @author wangqichao
 */
@RestController
public class PaymentController {
    private static final Logger log= LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paymentService;
    @Autowired
    TransactionService transactionService;

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
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("获取用户银行卡，但是cookie中没有uid");
            return map;
        }
        log.info("获得id为"+id+"的用户的银行卡信息");
        List<BankCardRO> ros=paymentService.getInvestment(id);
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
    public Map addRenewal(@RequestParam(value = "id")long id,@RequestParam(value="userSign")String userSign){
        log.info("添加续投订单，原交易id为"+id);
        int a=transactionService.addRenewal(id,userSign);
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("message","success");
        return map;
    }

    /**
     * 创建合同并返回合同id
     * @param rpo 请求数据
     * @return 返回参数，code,message,合同id
     */
    @PostMapping(value = "/a/u/r/pay/contract")
    public Map addUserSign(PaymentRPO rpo,HttpServletRequest request)throws Exception{
        String respMsg="";
        Map<String,Object> map=new HashMap<>();
        //获取用户id
        long id;
        String uidS= CookieUtil.getCookieValue(request,"uid");
        if (uidS!=null) {
            id = Long.parseLong(uidS);
        }
        //如果cookie中没有uid直接报错
        else {
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("创建合同，但是cookie中没有uid");
            return map;
        }
        log.info("用户"+id+"签署完合同，开始支付");
        rpo.setUserId(id);
        System.out.println("开始创建合同");
        long contractId=paymentService.addContract(rpo.getUserSign(),rpo.getProductId(),rpo.getUserId());
        rpo.setContractId(contractId);
        System.out.println("开始生成交易流水");
        long transactionLogId=paymentService.addPayTransactionLog(rpo);
        User user=paymentService.getUserInfo(id);
        BankCard bankCard=paymentService.getBankCard(rpo.getBankCardId());
        //转化基本信息为需要的map形式
        //首先将金额转化为分为单位
        BigDecimal moneyCent=new BigDecimal(rpo.getMoney()).multiply(BigDecimal.valueOf(100));
        Map<String,String> param= HttpPay.transParam(transactionLogId,id,moneyCent.toString(),bankCard.getBankCard(),user.getRealName(),user.getIdCard());
        //向富友发起请求，并将结果返回
        respMsg= HttpFormUtil.formForward(PAY_URL,param);
        map.put("code",0);
        map.put("message","success");
        map.put("respMsg",respMsg);
        System.out.println("respMsg:"+respMsg);
        return map;
    }

//    /**
//     * 获得第三方银行验证码
//     * @param phone 手机号
//     * @param bankCardId 银行卡id
//     * @return code,message
//     */
//    @PostMapping(value = "/a/u/r/pay/code")
//    public Map sendCode(@RequestParam(value="phone")String phone,@RequestParam(value="bankCardId")Long bankCardId){
//        long id=1L;
//        log.info("用户"+id+"发送银行短信,手机号为"+phone+"，银行卡id为"+bankCardId);
//        Map<String,Object> map=new HashMap<>();
//        map.put("code",10000);
//        map.put("message","ok");
//        return map;
//    }
//
//    /**
//     * 用户支付
//     * @param rpo 用户支付信息
//     * @return 支付结果,code,message,生成的交易号
//     */
//    @PostMapping(value = "/a/u/r/pay/payment")
//    public Map pay(@ModelAttribute PaymentRPO rpo){
//        long id=1L;
//        log.info("用户"+id+"支付，支付信息为"+rpo);
//        Map<String,Object> map=new HashMap<>();
//        map.put("code",10000);
//        map.put("message","ok");
//        map.put("transactionId",4568752);
//        return map;
//    }

    /**
     * 回调接口
     * @param request 回调参数
     */
    @PostMapping(value = "/a/pay/callback")
    public void callBack(HttpServletRequest request){
        log.info("回调接口被调用");
        //首先从请求中将数据拿出来
        // 其中只有响应码，商户号，商户订单号，富友订单号，银行卡号，金额比较重要，需要提取出来做MD5加密对比用
        String responseCode=request.getParameter("RESPONSECODE");
        String mchntCd=request.getParameter("MCHNTCD");
        String mchntOrderId=request.getParameter("MCHNTORDERID");
        String orderId=request.getParameter("ORDERID");
        String bankCard=request.getParameter("BANKCARD");
        String amt=request.getParameter("AMT");
        String sign=request.getParameter("SIGN");
        //然后将其加密一遍，看是否被修改过
        Boolean comparedResult=HttpPay.comparedParam(responseCode,mchntCd,mchntOrderId,orderId,bankCard,amt,sign);
        if(responseCode.equals("0000")&&comparedResult) {
            //修改交易流水表之前的数据,并返回合同id
            long contractId=paymentService.updateTransactionLog(Long.valueOf(mchntOrderId));
            //验证通过，修改合同表之前的数据,并返回合同编号
            String contractCode=paymentService.updateContract(contractId);
            //创建交易表新数据
            long transactionId=paymentService.addTransaction(Long.valueOf(mchntOrderId),contractCode);
            log.info("创建了交易，交易id为："+transactionId);
        }else {
            log.info("回调出现问题，无法创建订单，问题流水号为："+mchntOrderId);
        }
    }

    /**
     * 交易成功之后根据合同id获得刚才的交易详情
     * @param contractId 合同id
     * @return 交易详情
     */
    @GetMapping(value = "/a/u/r/pay/transaction/{id}")
    public Map getTransactionByContractId(@PathVariable(value = "id")Long contractId){
        log.info("付款成功后通过付款成功界面查看交易详情，请求合同id为"+contractId);
        //通过合同id查找对应的合同code，然后将其转化为交易id
        long transactionId=paymentService.getTransactionIdByContractId(contractId);
        //调用transactionService的利用交易id查询交易详情的方法，直接查询
        Map<String,Object> map=new HashMap<>();
        TransactionRO ro=transactionService.getTransactionById(transactionId);
        map.put("code",0);
        map.put("message","success");
        map.put("data",ro);
        return map;
    }

    /**
     * 用户未实名认证
     * @return code,message
     */
    @GetMapping(value = "/a/u/unrealname")
    public Map returnUnRealName(){
        log.info("被拦截返回错误信息为未登录");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10010);
        map.put("message","user didn't have real name");
        return map;
    }

    /**
     * 用户未绑卡
     * @return code,message
     */
    @GetMapping(value = "/a/u/nocard")
    public  Map returnHaveNoCard(){
        System.out.println("被拦截错误信息为未绑卡");
        Map<String,Object> map=new HashMap<>();
        map.put("code",10020);
        map.put("message","user didn't have defaultCard");
        return map;
    }

    /**
     * 上传图片
     * @param file 用户数字签名
     * @param request
     * @return
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
            map.put("code",-1);
            map.put("message","there is no uid in cookie");
            log.info("上传图片，但是cookie中没有uid");
            return map;
        }
        log.info("用户"+id+"签署合同中，在上传签名");
        //判断是否有图片上传
        if(file==null||file.equals("")||file.getSize()<=0){
           map.put("code",10030);
           map.put("message","there is no file");
           log.info("无上传图片，很尴尬");
           return map;
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
            log.error("上传图片发生问题");
            log.error(e.getMessage());
            map.put("code",10032);
            map.put("message","upload file fail");
            return map;
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
