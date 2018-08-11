package com.jnshu.utils;

import com.jnshu.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class MyInterceptor implements HandlerInterceptor {
    private static final Logger log= LoggerFactory.getLogger(MyInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        System.out.println("你好，我是拦截器。");

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        TokenUtil tokenUtil = new TokenUtil();
        Cookie cookie;
        Cookie cookie1;
        try {
            cookie = tokenUtil.getCookie(request);
            cookie1 = tokenUtil.getCookie2(request);
        }catch (Exception e){
            log.error("取出cookie过程出现问题，跳转登陆页面");
            response.sendRedirect(request.getContextPath()+"/a/unlogin");
            return false;
        }


//        System.out.println("拦截器中打印cookie name="+cookie.getName());
        //验证cookie
        if (cookie==null||!"uid".equals(cookie.getName())){
//            System.out.println("拦截器中验证 cookie name="+cookie.getName());
            //第一种方式，直接设置状态码
//            httpServletResponse.sendError(403, " you need loginIn.");
            //第二种方式。通过接口返回信息。
//            response.sendRedirect("/intercepted?next=" + request.getRequestURI());
//            response.sendRedirect("/a/login");
            log.error("取出的cookie为空，跳转登陆页面");
            response.sendRedirect(request.getContextPath()+"/a/unlogin");
            return false;
        }

        if (cookie1==null||!"token".equals(cookie1.getName())){
            log.error("取出的cookie出现问题，跳转登陆页面");
            response.sendRedirect(request.getContextPath()+"/a/unlogin");
            return false;
        }
//        System.out.println("拦截器中打印 cookie 时间="+cookie.getMaxAge());
//        if (System.currentTimeMillis()>cookie.getMaxAge()){
//            System.out.println("拦截器中验证 cookie name="+cookie.getMaxAge());
//            httpServletResponse.sendRedirect("/intercepted?next=" + httpServletRequest.getRequestURI());
//            return false;
//        }

        //验证token
//        String token = tokenUtil.getToken(request);
//        System.out.println(token);
        Map<String, Object> token2;
        try{
            token2 =tokenUtil.deToken(cookie1.getValue());
        }catch (Exception e){
            log.error("验证token出现问题，跳转登陆页面");
            response.sendRedirect(request.getContextPath()+"/a/unlogin");
            return false;
        }
        Boolean s = (Boolean) token2.get("verifyResult");
//        if (!s) response.sendRedirect("/intercepted?next=" + request.getRequestURI());
        if (!s){
            log.error("验证token出现问题，跳转登陆页面");
            response.sendRedirect(request.getContextPath()+"/a/unlogin");
            return false;
//            response.sendRedirect("/a/login");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        long startTime = (Long) httpServletRequest.getAttribute("startTime");
        httpServletRequest.removeAttribute("startTime");

        long endTime = System.currentTimeMillis();
//        modelAndView.addObject("totalTime", endTime - startTime);

        System.out.println("Request Precessing Time :: " + (endTime - startTime));

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
