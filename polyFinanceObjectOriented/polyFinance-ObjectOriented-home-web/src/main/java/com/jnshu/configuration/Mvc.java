package com.jnshu.configuration;

import com.jnshu.interceptor.PayInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置类，添加拦截器
 * 将自动适配视图解析器功能关闭
 * @author wangqichao
 */
@SpringBootConfiguration
public class Mvc extends WebMvcConfigurerAdapter{
    @Autowired
    PayInterceptor payInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(payInterceptor).addPathPatterns("/a/u/r/**");
    }
//    /**
//     （1）在配置类中继承：WebMvcConfigurerAdapter
//     （2）覆盖方法：configureContentNegotiation
//     favorPathExtension表示支持后缀匹配，
//     属性ignoreAcceptHeader默认为fasle，表示accept-header匹配，defaultContentType开启默认匹配。
//     例如：请求aaa.xx，若设置<entry key="xx" value="application/xml"/> 也能匹配以xml返回。
//     根据以上条件进行一一匹配最终，得到相关并符合的策略初始化ContentNegotiationManager
//     */
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.favorPathExtension(false);
//        }
}
