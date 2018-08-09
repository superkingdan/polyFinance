package com.jnshu.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String uidS= CookieUtil.getCookieValue(httpServletRequest,"uid");
//        String cookie = CookieUtil.getCookieValue(httpServletRequest, "token");
//        Map<String, Object> map = TokenJWT.validToken(cookie);
//        String state = (String) map.get("state");

        if (uidS!=null) {
            System.out.println("用户已登录");
            return true;
        }
        System.out.println("用户未登录，需重新登陆");
        throw new MyException(10001,"用户未登录，需跳转登陆页");
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
