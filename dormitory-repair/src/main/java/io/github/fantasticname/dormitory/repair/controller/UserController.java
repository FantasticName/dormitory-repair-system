package io.github.fantasticname.dormitory.repair.controller;

import io.github.fantasticname.dormitory.repair.entity.User;
import io.github.fantasticname.dormitory.repair.exception.BusinessException;
import io.github.fantasticname.dormitory.repair.service.UserService;
import io.github.fantasticname.dormitory.repair.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户功能控制器
 * 
 * @author FantasticName
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户信息
     * 
     * @param request 请求对象
     * @return 用户信息
     */
    @GetMapping("/profile")
    public Map<String, Object> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取用户信息成功");
        result.put("user", userVO);
        return result;
    }

    /**
     * 修改密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param request 请求对象
     * @return 修改结果
     */
    @PostMapping("/password")
    public Map<String, Object> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }

        boolean success = userService.changePassword(userId, oldPassword, newPassword);
        if (!success) {
            throw new BusinessException(400, "旧密码错误或修改失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "密码修改成功");
        return result;
    }
}
