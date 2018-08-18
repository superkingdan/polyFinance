package com.jnshu.utils;

import java.util.Calendar;

public class TransString {
    /**
     * 将旧债权协议编号加一转化为新编号
     * @param oldCode 旧债权协议
     * @return 新债权协议
     */
    public static String transClaimsCode(String oldCode){
        String newCode;
        if(oldCode!=null&&!oldCode.equals("")) {
           newCode=transCode(oldCode,"ZQ");
        }
        else {
            Calendar time = Calendar.getInstance();
            int thisYearWhole = time.get(Calendar.YEAR);
            int thisYear = thisYearWhole % 100;
             newCode="UKZQ"+(thisYear*100000000+10000001);
        }
        return newCode;
    }

    /**
     * 将旧合同转化为新合同
     * @param oldCode 旧合同编号
     * @param productCode 产品code
     * @return 新合同编号
     */
    public static String transContractCode(String oldCode,String productCode){
        String newCode;
        if(oldCode!=null&& !oldCode.equals("")) {
        newCode = transCode(oldCode,productCode);
    }
        else {
        Calendar time = Calendar.getInstance();
        int thisYearWhole = time.get(Calendar.YEAR);
        int thisYear = thisYearWhole % 100;
        newCode="UK"+productCode+(thisYear*100000000+10000001);
    }
        return newCode;
    }

    /**
     * 如果旧编号不为空，将旧编号直接加1
     * @param oldCode 旧code
     * @param code 债权的话就是ZQ,合同的话就是产品code
     * @return 新的编号
     */
    private static String transCode(String oldCode,String code){
        String newCode="";
        char ch[] = oldCode.toCharArray();
        //存储数字
        StringBuilder sb1 = new StringBuilder();
        for (char c : ch) {
            if (c >= '0' && c <= '9') {
                sb1.append(c);
            }
        }
        int num = Integer.parseInt(sb1.toString());
        //得到原code中的年份
        int year = num / 100000000;
        //得到当前年
        Calendar time = Calendar.getInstance();
        int thisYearWhole = time.get(Calendar.YEAR);
        int thisYear = thisYearWhole % 100;
        //判断年份是否和当前年份相同,不同就归零
        if (year != thisYear) {
            num = thisYear * 100000000+10000001;
        } else
            num++;
        newCode = "UK"+code+ num;
        return newCode;
    }
}
