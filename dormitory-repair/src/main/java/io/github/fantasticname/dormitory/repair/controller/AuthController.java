package io.github.fantasticname.dormitory.repair.controller;

import io.github.fantasticname.dormitory.repair.entity.User;
import io.github.fantasticname.dormitory.repair.exception.BusinessException;
import io.github.fantasticname.dormitory.repair.service.UserService;
import io.github.fantasticname.dormitory.repair.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证功能
 * 
 * @author FantasticName
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录
     * 
     * @param account 账号
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String account, @RequestParam String password) {
        User user = userService.login(account, password);
        if (user == null) {
            throw new BusinessException(401, "账号或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    /**
     * 注册
     * 
     * @param account 账号
     * @param password 密码
     * @param role 角色
     * @return 注册结果
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestParam String account, @RequestParam String password, @RequestParam Integer role) {
        boolean success = userService.register(account, password, role);
        if (!success) {
            throw new BusinessException(400, "账号已存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "注册成功");
        return result;
    }

}

