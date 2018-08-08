package com.jnshu.service3;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component(value = "cookieService3")
public interface CookieService3 {
    /*取出cookie里的token 并解析*/
    long findByCookie(HttpServletRequest request) throws Exception;
}
