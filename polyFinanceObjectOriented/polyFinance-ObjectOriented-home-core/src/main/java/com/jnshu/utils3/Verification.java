package com.jnshu.utils3;

public class Verification {
    public static boolean regexUsername(String username) {
        //用户名匹配结果
        //如果isUsername_matcheris true , 则return true , else  return false
        return username.matches("^[a-zA-Z][a-zA-Z0-9]{8,16}$");

    }
    public static boolean regexPassword(String password) {
        //密码匹配结果
        //如果password_matcheris true , 则return true , else  return false
        return password.matches("^[a-zA-Z0-9]{6,16}$");
    }
    public static boolean regexEmailAddress(String email){

        //邮箱匹配结果
        //如果isEmail_matcher value is true , 则 return true , else return false
        return email.matches("[a-zA-Z_0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,}){1,3}");

    }
    public static boolean regexPhone(String phone){

        //手机匹配结果
        //如果手机 value is true , 则 return true , else return false
        return phone.matches("^1[3|4|5|7|8][0-9]\\d{4,8}$");

    }
    public static boolean regexkQQ(String qq){
        return qq.matches("^[1-9][0-9]{6,14}$");
    }

    public static boolean regexIdCard(String idCard) {
        return idCard.matches("^[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))((0[1-9])|([1-2]\\d)|(3[0-1]))((\\d{4})|(\\d{3}[Xx]))$");
    }
}

    //        String pattern = "^[a-zA-Z0-9]{8,16}$";
//        ^\w+@\w+(\.\w+)+$/ 邮箱
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(str);
//        boolean confirm = m.matches();
