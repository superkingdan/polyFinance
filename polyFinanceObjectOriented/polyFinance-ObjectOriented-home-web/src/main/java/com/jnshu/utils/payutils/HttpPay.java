package com.jnshu.utils.payutils;

import com.jnshu.entity.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * 将基本信息转化为请求信息
 * @author  wangqichao
 */
public class HttpPay {
    private static final String BACK_URL= ConfigReader.getString("h5.back_url");
    private static final String HOME_URL=ConfigReader.getString("h5.home_url");
    private static final String RETURN_URL=ConfigReader.getString("h5.return_url");

    /**
     * 将相关信息转化为Map键值对形式
     * @param transactionLogId 交易流水id
     * @param userIdL 用户id
     * @param money 交易金额
     * @param bankCard 银行卡号
     * @param realName 用户姓名
     * @param userIdCard 用户身份证号
     * @return Map键值对
     */
    public static Map<String,String> transParam(Long transactionLogId, Long userIdL, String money, String bankCard, String realName, String userIdCard){
        Map<String,String> param=new HashMap<>();
        String mchntCd= Constants.H5_MCHNT_CD;
        String type="11";
        String version="2.0";
        String logoTp="0";
        String mchtOrderId=transactionLogId.toString();
        String userId=userIdL.toString();
        String idType="0";
        String signTp="md5";
        String privateKey= Constants.H5_MCHNT_KEY;
        StringBuilder signPlain=new StringBuilder();
//拼接获得签名模板
        signPlain.append(type).append("|").append(version).append("|").append(mchntCd).append("|").append(mchtOrderId)
                .append("|").append(userId).append("|").append(money).append("|").append(bankCard).append("|").append(BACK_URL)
                .append("|").append(realName).append("|").append(userIdCard).append("|").append(idType).append("|").append(logoTp)
                .append("|").append(HOME_URL).append("|").append(RETURN_URL).append("|").append(privateKey);
//获得加密后的sign
        String sign= MD5.MD5Encode(signPlain.toString());
        System.out.println("[签名明文:]"+signPlain);
        StringBuffer orderPlain = new StringBuffer();
//组合order
        orderPlain.append("<ORDER>").append("<VERSION>2.0</VERSION>").append("<LOGOTP>0</LOGOTP>").append("<MCHNTCD>").append(mchntCd).append("</MCHNTCD>")
                .append("<TYPE>").append(type).append("</TYPE>").append("<MCHNTORDERID>").append(mchtOrderId).append("</MCHNTORDERID>").append("<USERID>").append(userId).append("</USERID>").append("<AMT>").append(money).append("</AMT>").append("<BANKCARD>").append(bankCard).append("</BANKCARD>").append("<BACKURL>").append(BACK_URL).append("</BACKURL>").append("<HOMEURL>").append(HOME_URL).append("</HOMEURL>").append("<REURL>").append(RETURN_URL).append("</REURL>").append("<NAME>").append(realName).append("</NAME>").append("<IDTYPE>").append(idType).append("</IDTYPE>").append("<IDNO>").append(userIdCard).append("</IDNO>").append("<REM1>").append(userId).append("</REM1>").append("<REM2>").append(userId).append("</REM2>").append("<REM3>").append(userId).append("</REM3>").append("<SIGNTP>").append("md5").append("</SIGNTP>").append("<SIGN>").append(sign).append("</SIGN>").append("</ORDER>");
        System.out.println("[订单信息:]" + orderPlain.toString());
        param.put("ENCTP","1");
        param.put("VERSION",version);
        param.put("LOGOTP",logoTp);
        param.put("MCHNTCD",mchntCd);
        param.put("FM", orderPlain.toString());
        System.out.println("FM:"+orderPlain.toString());
//DES加密
        try{
            param.put("FM", DESCoderFUIOU.desEncrypt(orderPlain.toString(), DESCoderFUIOU.getKeyLength8(Constants.H5_MCHNT_KEY)));
        }catch (Exception e){
            e.printStackTrace();
            param.put("FM","系统异常");
        }
        return param;
    }

    /**
     * 将回调的数据加密后比较签名是否一致
     * @param responseCode 响应代码
     * @param mchntCd 商户代码
     * @param mchtOrderId 商户订单代码
     * @param orderId 富友订单代码
     * @param bankCard 银行卡号
     * @param amt 金额
     * @param sign md5签名
     * @return 比对结果
     */
    public static  Boolean comparedParam(String responseCode,String mchntCd,String mchtOrderId,String orderId,String bankCard,String amt,String sign){
            StringBuilder signPlain=new StringBuilder();
            String version="2.0";
            String type="10";
            String privateKey= Constants.H5_MCHNT_KEY;
            //拼接签名模板
            signPlain.append(type).append("|").append(version).append("|").append(responseCode).append("|").append(mchntCd)
                    .append("|").append(mchtOrderId).append("|").append(orderId).append("|").append(amt).append("|")
                    .append(bankCard).append("|").append(privateKey);
            //获得加密之后的签名
            String newSign=MD5.MD5Encode(signPlain.toString());
            return newSign.equals(sign);
    }
}