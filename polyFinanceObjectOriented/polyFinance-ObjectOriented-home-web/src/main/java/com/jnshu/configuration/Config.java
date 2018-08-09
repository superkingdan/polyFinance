package com.jnshu.configuration;

import com.jnshu.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置类，添加拦截器
 * 将自动适配视图解析器功能关闭
 * @author
 */
@SpringBootConfiguration
public class Config extends WebMvcConfigurerAdapter {
    @Autowired
    UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(userInterceptor).addPathPatterns("/a/u/**");
    }

}