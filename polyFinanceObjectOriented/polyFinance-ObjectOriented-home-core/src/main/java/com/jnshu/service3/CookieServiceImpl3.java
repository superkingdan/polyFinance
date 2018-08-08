package com.jnshu.service3;

import com.jnshu.utils3.CookieUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CookieServiceImpl3 implements CookieService3 {
    @Override
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
