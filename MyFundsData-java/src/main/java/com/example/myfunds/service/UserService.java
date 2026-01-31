package com.example.myfunds.service;

import com.example.myfunds.entity.User;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑方法
 */
public interface UserService {
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册后的用户信息
     */
    User register(User user);
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息
     */
    User login(String username, String password);
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    User updateUser(User user);
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否已存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否已存在
     */
    boolean checkEmailExists(String email);
    
    /**
     * 检查手机号是否已存在
     * @param phone 手机号
     * @return 是否已存在
     */
    boolean checkPhoneExists(String phone);
}