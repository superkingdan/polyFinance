package com.jnshu.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.CookieUtil;
import com.jnshu.utils3.TokenJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户拦截器
 * @author
 */
@Component(value = "userInterceptor")
public class UserInterceptor implements HandlerInterceptor {

    private static final Logger log= LoggerFactory.getLogger(UserInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("进入拦截器");
//        JSONObject json=new JSONObject();
        String uidS;
        String cookie;
        try {
            uidS = CookieUtil.getCookieValue(httpServletRequest, "uid");
            cookie = CookieUtil.getCookieValue(httpServletRequest, "token");
        }catch (Exception e){
            throw new MyException(10001,"请登入");
        }
        if (cookie==null||uidS==null){
            throw new MyException(10001,"请登入");
        }
        Map<String, Object> map;
        try{
            map = TokenJWT.validToken(cookie);
        }catch (Exception e){
            throw new MyException(10001,"token不正确,请重新登入");
        }
        String state = (String) map.get("state");
        String userId= String.valueOf(map.get("uid"));
        if (state.equals("EXPIRED")) {
            throw new MyException(10001,"已过期,请登入");
        }
        return true;

    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
