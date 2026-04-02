package io.github.fantasticname.dormitory.repair.controller;

import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import io.github.fantasticname.dormitory.repair.service.RepairOrderService;
import io.github.fantasticname.dormitory.repair.util.FileUtil;
import io.github.fantasticname.dormitory.repair.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 维修订单控制器
 * 
 * @author FantasticName
 */
@RestController
@RequestMapping("/repair")
public class RepairOrderController {

    private static final Logger logger = LoggerFactory.getLogger(RepairOrderController.class);

    @Autowired
    private RepairOrderService repairOrderService;

    /**
     * 创建维修订单
     * 
     * @param deviceType 设备类型
     * @param description 故障描述
     * @param request 请求对象
     * @return 创建结果
     */
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestParam String deviceType, @RequestParam String description, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 从请求属性中获取用户ID（由拦截器设置）
        Long userId = (Long) request.getAttribute("userId");
        
        RepairOrder order = repairOrderService.createOrder(userId, deviceType, description);
        if (order != null) {
            result.put("code", 200);
            result.put("message", "订单创建成功");
            result.put("order", order);
        } else {
            result.put("code", 400);
            result.put("message", "订单创建失败");
        }
        
        return result;
    }

    /**
     * 上传订单图片
     * 
     * @param orderId 订单ID
     * @param file 图片文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadImage(@RequestParam Long orderId, @RequestParam MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 上传文件
            String imagePath = FileUtil.uploadFile(file);
            if (imagePath != null) {
                // 更新订单图片路径
                boolean success = repairOrderService.updateImagePath(orderId, imagePath);
                if (success) {
                    result.put("code", 200);
                    result.put("message", "图片上传成功");
                    result.put("imagePath", imagePath);
                } else {
                    // 上传成功但更新失败，删除文件
                    FileUtil.deleteFile(imagePath);
                    result.put("code", 400);
                    result.put("message", "图片上传失败");
                }
            } else {
                result.put("code", 400);
                result.put("message", "图片上传失败");
            }
        } catch (Exception e) {
            logger.error("上传图片异常", e);
            result.put("code", 500);
            result.put("message", "系统异常");
        }
        
        return result;
    }

    /**
     * 获取用户的维修订单列表
     * 
     * @param request 请求对象
     * @return 订单列表
     */
    @GetMapping("/list")
    public Map<String, Object> getOrderList(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 从请求属性中获取用户ID（由拦截器设置）
        Long userId = (Long) request.getAttribute("userId");
        
        result.put("code", 200);
        result.put("message", "获取订单列表成功");
        result.put("orders", repairOrderService.getOrdersByUserId(userId));
        
        return result;
    }

    /**
     * 获取订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/detail/{orderId}")
    public Map<String, Object> getOrderDetail(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        
        RepairOrder order = repairOrderService.getOrderById(orderId);
        if (order != null) {
            result.put("code", 200);
            result.put("message", "获取订单详情成功");
            result.put("order", order);
        } else {
            result.put("code", 404);
            result.put("message", "订单不存在");
        }
        
        return result;
    }

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status 新状态
     * @return 更新结果
     */
    @PostMapping("/status")
    public Map<String, Object> updateOrderStatus(@RequestParam Long orderId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        
        boolean success = repairOrderService.updateOrderStatus(orderId, status);
        if (success) {
            result.put("code", 200);
            result.put("message", "状态更新成功");
        } else {
            result.put("code", 400);
            result.put("message", "状态更新失败");
        }
        
        return result;
    }

}