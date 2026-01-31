package com.example.myfunds.config;

import com.example.myfunds.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置类，用于注册自定义过滤器
 */
@Configuration
public class FilterConfig {

    /**
     * 注册日志过滤器，应用到所有请求
     */
    @Bean
    public FilterRegistrationBean<LogFilter> logFilterRegistration() {
        // 创建过滤器注册Bean
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        
        // 设置要注册的过滤器
        registrationBean.setFilter(new LogFilter());
        
        // 设置过滤器名称
        registrationBean.setName("LogFilter");
        
        // 设置过滤器的URL模式，/*表示应用到所有请求
        registrationBean.addUrlPatterns("/*");
        
        // 设置过滤器的执行顺序，值越小，优先级越高
        registrationBean.setOrder(1);
        
        return registrationBean;
    }
}