package com.jnshu.utils3;

public class UserNumber {
    /**
     * 将用户编号加一转化为新编号
     * @param userNumber 旧用户编号
     * @return 新用户编号
     */
    public static String NewUserNumber(String userNumber){
        if (userNumber==null){
            String oneNumber="UK1810000001";
            return oneNumber;
        }
        int s= Integer.parseInt(userNumber.substring(2));
        int a=s+1;
        String newNumber="UK"+a;
        return newNumber;
    }





}

