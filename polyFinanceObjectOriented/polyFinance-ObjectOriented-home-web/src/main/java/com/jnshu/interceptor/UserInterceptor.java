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
        JSONObject json = new JSONObject();
        String uidS= CookieUtil.getCookieValue(httpServletRequest,"uid");
        String cookie = CookieUtil.getCookieValue(httpServletRequest, "token");
        Map<String, Object> map = TokenJWT.validToken(cookie);
        String state = (String) map.get("state");
        String userId= String.valueOf(map.get("uid"));


        if (cookie == null) {
//            json.put("code",10001);
//            json.put("message","cookie无效");
            throw new MyException(10001,"cookie无效");
//            SendMsgUtil.sendJsonMessage(httpServletResponse,json);
//            return false;

        }
        if (state.equals("EXPIRED")) {
//            json.put("code",1002);
//            json.put("message","已过期,请登入");
//            SendMsgUtil.sendJsonMessage(httpServletResponse,json);
            throw new MyException(10001,"已过期,请登入");
//            return false;
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
