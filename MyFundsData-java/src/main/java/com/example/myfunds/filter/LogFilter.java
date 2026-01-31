package com.example.myfunds.filter;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志过滤器，用于从请求Header中提取用户ID并放入MDC
 * 确保日志中包含用户ID信息，便于追踪用户操作全流程
 */
public class LogFilter implements Filter {

    // 请求Header中的用户ID字段名
    private static final String USER_ID_HEADER = "X-User-Id";
    // MDC中的用户ID键名
    private static final String MDC_USER_ID_KEY = "userId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，无需额外操作
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 将ServletRequest转换为HttpServletRequest
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            
            // 从请求Header中提取用户ID
            String userId = httpRequest.getHeader(USER_ID_HEADER);
            
            // 将用户ID放入MDC
            if (userId != null && !userId.isEmpty()) {
                MDC.put(MDC_USER_ID_KEY, userId);
            } else {
                // 如果没有用户ID，放入空字符串或默认值
                MDC.put(MDC_USER_ID_KEY, "");
            }
            
            // 继续执行过滤链
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 清除MDC中的用户ID，避免内存泄漏
            MDC.remove(MDC_USER_ID_KEY);
        }
    }

    @Override
    public void destroy() {
        // 销毁方法，无需额外操作
    }
}