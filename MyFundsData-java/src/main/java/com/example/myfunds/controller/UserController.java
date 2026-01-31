package com.example.myfunds.controller;

import com.example.myfunds.entity.User;
import com.example.myfunds.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 提供用户注册、登录等API接口
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param user 用户注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("开始用户注册，请求参数：username={}, email={}, phone={}", 
                user.getUsername(), user.getEmail(), user.getPhone());
        
        try {
            User registeredUser = userService.register(user);
            log.info("用户注册成功，用户ID：{}, 用户名：{}", 
                    registeredUser.getId(), registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            log.error("用户注册失败，请求参数：username={}, 错误原因：{}", 
                    user.getUsername(), e.getMessage());
            throw e;
        }
    }

    /**
     * 用户登录
     * @param loginRequest 登录请求，包含用户名和密码
     * @return 登录结果和用户信息
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        log.info("开始用户登录，用户名：{}", loginRequest.getUsername());
        
        try {
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            log.info("用户登录成功，用户ID：{}, 用户名：{}", user.getId(), user.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("用户登录失败，用户名：{}, 错误原因：{}", 
                    loginRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long id) {
        log.info("获取用户信息，用户ID：{}", id);
        
        try {
            User user = userService.getUserById(id);
            log.info("获取用户信息成功，用户ID：{}, 用户名：{}", user.getId(), user.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("获取用户信息失败，用户ID：{}, 错误原因：{}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 更新的用户信息
     * @return 更新后的用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserInfo(@PathVariable Long id, @RequestBody User user) {
        log.info("开始更新用户信息，用户ID：{}, 请求参数：nickname={}, email={}, phone={}", 
                id, user.getNickname(), user.getEmail(), user.getPhone());
        
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            log.info("更新用户信息成功，用户ID：{}, 用户名：{}", updatedUser.getId(), updatedUser.getUsername());
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("更新用户信息失败，用户ID：{}, 错误原因：{}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * 测试接口
     * 用于验证服务是否正常启动
     * @return 测试成功字符串
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("测试接口被调用");
        return ResponseEntity.ok("测试成功");
    }

    /**
     * 登录请求类
     */
    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}