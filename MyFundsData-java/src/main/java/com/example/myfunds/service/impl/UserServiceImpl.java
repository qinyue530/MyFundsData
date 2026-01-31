package com.example.myfunds.service.impl;

import com.example.myfunds.entity.User;
import com.example.myfunds.mapper.UserMapper;
import com.example.myfunds.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册后的用户信息
     */
    @Override
    @Transactional
    public User register(User user) {
        log.info("开始用户注册，用户名：{}, 邮箱：{}, 手机号：{}", 
                user.getUsername(), user.getEmail(), user.getPhone());
        
        try {
            // 检查用户名是否已存在
            log.debug("步骤1：检查用户名是否已存在，用户名：{}", user.getUsername());
            if (checkUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            
            // 检查邮箱是否已存在
            log.debug("步骤2：检查邮箱是否已存在，邮箱：{}", user.getEmail());
            if (user.getEmail() != null && checkEmailExists(user.getEmail())) {
                throw new RuntimeException("邮箱已被注册");
            }
            
            // 检查手机号是否已存在
            log.debug("步骤3：检查手机号是否已存在，手机号：{}", user.getPhone());
            if (user.getPhone() != null && checkPhoneExists(user.getPhone())) {
                throw new RuntimeException("手机号已被注册");
            }
            
            // 设置默认状态为活跃
            log.debug("步骤4：设置用户状态为活跃");
            user.setStatus("ACTIVE");
            
            // 简单加密密码（实际项目中应使用BCrypt等安全加密方式）
            log.debug("步骤5：处理用户密码");
            String encodedPassword = user.getPassword(); // 实际项目中应该加密
            user.setPassword(encodedPassword);
            
            // 保存用户信息
            log.debug("步骤6：保存用户信息到数据库");
            userMapper.insert(user);
            log.info("用户注册成功，用户ID：{}, 用户名：{}", user.getId(), user.getUsername());
            return user;
        } catch (Exception e) {
            log.error("用户注册失败，用户名：{}, 错误原因：{}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息
     */
    @Override
    public User login(String username, String password) {
        log.info("开始用户登录，用户名：{}", username);
        
        try {
            // 根据用户名查询用户
            log.debug("步骤1：根据用户名查询用户，用户名：{}", username);
            Optional<User> userOptional = userMapper.selectByUsername(username);
            if (!userOptional.isPresent()) {
                throw new RuntimeException("用户名或密码错误");
            }
            
            User user = userOptional.get();
            log.debug("查询到用户信息，用户ID：{}, 用户名：{}", user.getId(), user.getUsername());
            
            // 简单密码验证（实际项目中应使用加密验证）
            log.debug("步骤2：验证用户密码，用户ID：{}", user.getId());
            if (!user.getPassword().equals(password)) {
                throw new RuntimeException("用户名或密码错误");
            }
            
            // 检查用户状态
            log.debug("步骤3：检查用户状态，用户ID：{}, 状态：{}", user.getId(), user.getStatus());
            if (!"ACTIVE".equals(user.getStatus())) {
                throw new RuntimeException("用户账号已被停用");
            }
            
            log.info("用户登录成功，用户ID：{}, 用户名：{}", user.getId(), username);
            return user;
        } catch (Exception e) {
            log.error("用户登录失败，用户名：{}, 错误原因：{}", username, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public User getUserById(Long id) {
        log.info("开始根据ID查询用户，用户ID：{}", id);
        
        try {
            Optional<User> userOptional = userMapper.selectById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                log.info("根据ID查询用户成功，用户ID：{}, 用户名：{}", id, user.getUsername());
                return user;
            } else {
                throw new RuntimeException("用户不存在");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    @Override
    @Transactional
    public User updateUser(User user) {
        log.info("开始更新用户信息，用户ID：{}, 更新内容：nickname={}, email={}, phone={}", 
                user.getId(), user.getNickname(), user.getEmail(), user.getPhone());
        
        try {
            // 检查用户是否存在
            log.debug("步骤1：检查用户是否存在，用户ID：{}", user.getId());
            User existingUser = getUserById(user.getId());
            
            // 如果用户名有变化，检查新用户名是否已存在
            log.debug("步骤2：检查用户名是否变化，旧用户名：{}, 新用户名：{}", 
                    existingUser.getUsername(), user.getUsername());
            if (!existingUser.getUsername().equals(user.getUsername()) && 
                checkUsernameExists(user.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
            
            // 更新用户信息
            log.debug("步骤3：更新用户信息到数据库，用户ID：{}", user.getId());
            userMapper.update(user);
            log.info("用户信息更新成功，用户ID：{}, 用户名：{}", user.getId(), user.getUsername());
            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否已存在
     */
    @Override
    public boolean checkUsernameExists(String username) {
        log.debug("检查用户名是否已存在，用户名：{}", username);
        Optional<User> userOptional = userMapper.selectByUsername(username);
        boolean exists = userOptional.isPresent();
        log.debug("用户名检查结果：用户名={}, 已存在={}", username, exists);
        return exists;
    }

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否已存在
     */
    @Override
    public boolean checkEmailExists(String email) {
        log.debug("检查邮箱是否已存在，邮箱：{}", email);
        Optional<User> userOptional = userMapper.selectByEmail(email);
        boolean exists = userOptional.isPresent();
        log.debug("邮箱检查结果：邮箱={}, 已存在={}", email, exists);
        return exists;
    }

    /**
     * 检查手机号是否已存在
     * @param phone 手机号
     * @return 是否已存在
     */
    @Override
    public boolean checkPhoneExists(String phone) {
        log.debug("检查手机号是否已存在，手机号：{}", phone);
        Optional<User> userOptional = userMapper.selectByPhone(phone);
        boolean exists = userOptional.isPresent();
        log.debug("手机号检查结果：手机号={}, 已存在={}", phone, exists);
        return exists;
    }
}