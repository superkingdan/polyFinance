package com.jnshu.utils3;

import javax.servlet.http.HttpServletRequest;


public class CookieFind {

    public long findByCookie(HttpServletRequest request) throws Exception {
        long id=Long.valueOf(CookieUtil.getCookieValue(request,"uid"));
//        /*解析JWT，取出JWTusername*/
//        String username;
//        Cookie cookie= CookieUnit.getCookieByName(request,"token");
//        Map<String, Claim> map = null;
//        try {
//            map = TokenJWT.verifyToken(cookie.getValue());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        username = map.get("username").asString();
//        User user= null;
        return id;
    }
}
