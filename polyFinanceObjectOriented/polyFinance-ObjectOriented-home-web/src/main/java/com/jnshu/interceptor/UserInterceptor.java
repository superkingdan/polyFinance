package com.jnshu.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jnshu.dao3.UserMapper3;
import com.jnshu.entity.User;
import com.jnshu.exception.MyException;
import com.jnshu.utils3.CookieUtil;
import com.jnshu.utils3.TokenJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserMapper3 userMapper3;
    private static final Logger log= LoggerFactory.getLogger(UserInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("进入拦截器");
        JSONObject json = new JSONObject();
        String uidS;
        String cookie;
        try {
            uidS = CookieUtil.getCookieValue(httpServletRequest, "uid");
            cookie = CookieUtil.getCookieValue(httpServletRequest, "token");
        }catch (Exception e){
            log.error("拦截器空指针，cookie为空");
            throw new MyException(10001,"cookie为空，请登录");
        }
        User user=userMapper3.findUserById(Long.parseLong(uidS));
        if (user==null){
            throw new MyException(-1,"该用户不存在");
        }
        Map<String, Object> map;
        try {
            map = TokenJWT.validToken(cookie);
        }catch (Exception e){
            log.error("拦截器token解析失败");
            throw new MyException(10001,"token解析失败，请登录");
        }
        String state = (String) map.get("state");
        String userId= String.valueOf(map.get("uid"));


        if (cookie == null) {
            log.error("coolie为null");
            throw new MyException(10001,"cookie无效，请登录");
        }
        if (state.equals("EXPIRED")) {
            log.error("拦截器token解析失败");
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
