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
        if(oldCode!=null&&!oldCode.equalsIgnoreCase("")) {
            char ch[] = oldCode.toCharArray();
            //存储数字
            StringBuilder sb1 = new StringBuilder();
            //存储字母
            StringBuilder sb2 = new StringBuilder();
            for (char c : ch) {
                if (c >= '0' && c <= '9') {
                    sb1.append(c);
                }
                if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                    sb2.append(c);
                }
            }
            int num = Integer.parseInt(sb1.toString());
            System.out.println(num);
            //得到原code中的年份
            int year = num / 1000000;
            System.out.println("year"+year);
            //得到当前年
            Calendar time = Calendar.getInstance();
            int thisYearWhole = time.get(Calendar.YEAR);
            int thisYear = thisYearWhole % 100;
            System.out.println(thisYear);
            //判断年份是否和当前年份相同,不同就归零
            if (year != thisYear) {
                num = thisYear * 1000000 + 1;
            } else
                num++;
            newCode = sb2.toString() + num;

        }
        else {
            Calendar time = Calendar.getInstance();
            int thisYearWhole = time.get(Calendar.YEAR);
            int thisYear = thisYearWhole % 100;
             newCode="UKZQ"+thisYear*1000000+1;
        }
        return newCode;
    }
}
