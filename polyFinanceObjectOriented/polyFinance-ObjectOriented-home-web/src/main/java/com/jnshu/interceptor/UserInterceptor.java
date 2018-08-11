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
import java.util.Objects;

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
        String uidS="";
        String cookie="";
        try {
//            uidS = CookieUtil.getCookieValue(httpServletRequest, "uid");
            cookie = CookieUtil.getCookieValue(httpServletRequest, "token");
        }catch (Exception e){
            log.error("无法取出cookie，需跳转登录界面");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unlogin");
            return false;
        }
        try {
            if (null==cookie||cookie.length()<=0) {
                log.error("cookie为null，需跳转登录界面");
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unlogin");
                return false;
            }
        }catch (Exception e){
            log.error("cookie判断出现异常，需跳转登录界面");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unlogin");
            return false;
        }
        Map<String, Object> map;
        try{
            map = TokenJWT.validToken(cookie);
            String state = (String) map.get("state");
            String userId= String.valueOf(map.get("uid"));
            if (state.equals("EXPIRED")) {
                log.error("cookie过期，需跳转登录界面");
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unlogin");
                return false;
            }
        }catch (Exception e){
            log.error("无法转化token，需跳转登录界面");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/a/unlogin");
            return false;
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
