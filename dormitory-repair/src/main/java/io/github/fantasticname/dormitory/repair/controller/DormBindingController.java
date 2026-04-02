package io.github.fantasticname.dormitory.repair.controller;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;
import io.github.fantasticname.dormitory.repair.exception.BusinessException;
import io.github.fantasticname.dormitory.repair.service.DormBindingService;
import io.github.fantasticname.dormitory.repair.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 宿舍绑定控制器
 * 
 * @author Fantasticname
 */
@RestController
@RequestMapping("/dorm")
public class DormBindingController {

    @Autowired
    private DormBindingService dormBindingService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 绑定宿舍
     * 
     * @param building 楼栋
     * @param room 房间
     * @param request 请求对象
     * @return 绑定结果
     */
    @PostMapping("/bind")
    public Map<String, Object> bindDorm(@RequestParam String building, @RequestParam String room, HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        if (userId == null) {
            throw new BusinessException(401, "未授权");
        }

        boolean success = dormBindingService.bindDorm(userId, building, room);
        if (!success) {
            throw new BusinessException(400, "已经绑定过宿舍");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "绑定成功");
        return result;
    }

    /**
     * 获取宿舍绑定信息
     * 
     * @param request 请求对象
     * @return 宿舍绑定信息
     */
    @PostMapping("/info")
    public Map<String, Object> getDormInfo(HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        if (userId == null) {
            throw new BusinessException(401, "未授权");
        }

        DormBinding binding = dormBindingService.getByUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("binding", binding);
        return result;
    }

    /**
     * 修改宿舍绑定信息
     * 
     * @param building 楼栋
     * @param room 房间
     * @param request 请求对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public Map<String, Object> updateDorm(@RequestParam String building, @RequestParam String room, HttpServletRequest request) {
        Long userId = jwtUtil.getUserIdFromRequest(request);
        if (userId == null) {
            throw new BusinessException(401, "未授权");
        }

        boolean success = dormBindingService.updateBind(userId, building, room);
        if (!success) {
            throw new BusinessException(400, "未绑定宿舍");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "修改成功");
        return result;
    }
}
