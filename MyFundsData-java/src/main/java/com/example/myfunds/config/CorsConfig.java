package com.example.myfunds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 全局CORS配置类
 * 解决前后端分离架构下的跨域资源共享问题
 */
@Configuration
public class CorsConfig {

    /**
     * 配置CORS过滤器
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 允许所有来源访问，生产环境中应限制为具体的前端域名
        corsConfig.addAllowedOriginPattern("*");
        
        // 允许所有HTTP方法
        corsConfig.addAllowedMethod("*");
        
        // 允许所有请求头
        corsConfig.addAllowedHeader("*");
        
        // 允许携带凭证（如cookie）
        corsConfig.setAllowCredentials(true);
        
        // 允许暴露响应头
        corsConfig.addExposedHeader("*");
        
        // 创建URL匹配源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", corsConfig);
        
        // 创建并返回CORS过滤器
        return new CorsFilter(source);
    }
}