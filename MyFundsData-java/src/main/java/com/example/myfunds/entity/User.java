package com.example.myfunds.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 存储用户基本信息，用于登录认证和数据关联
 */
@Data
public class User {
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（加密存储）
     */
    private String password;
    
    /**
     * 电子邮箱
     */
    private String email;
    
    /**
     * 手机号码
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 状态：ACTIVE-活跃，INACTIVE-停用
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;
}