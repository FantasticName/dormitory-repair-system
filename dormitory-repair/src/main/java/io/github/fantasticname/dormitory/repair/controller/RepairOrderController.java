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
import java.util.List;
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
     * @param file 图片文件（可选）
     * @param request 请求对象
     * @return 创建结果
     */
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestParam String deviceType, @RequestParam String description, 
                                          @RequestParam(required = false) MultipartFile file, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 从请求属性中获取用户ID和角色（由拦截器设置）
        Long userId = (Long) request.getAttribute("userId");
        Integer role = (Integer) request.getAttribute("role");

        if (role == null || role != 1) {
            result.put("code", 403);
            result.put("message", "仅学生可创建报修单");
            return result;
        }

        String imagePath = null;
        if (file != null && !file.isEmpty()) {
            imagePath = FileUtil.uploadFile(file);
        }
        
        RepairOrder order = repairOrderService.createOrder(userId, deviceType, description, imagePath);
        if (order != null) {
            result.put("code", 200);
            result.put("message", "报修单创建成功");
            result.put("order", order);
        } else {
            result.put("code", 400);
            result.put("message", "报修单创建失败");
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
     * 取消报修单（仅学生可取消自己的待处理订单）
     * 
     * @param orderId 订单ID
     * @param request 请求对象
     * @return 取消结果
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancelOrder(@RequestParam Long orderId, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 只有学生角色且是自己的订单可以取消（service层已有部分判断，这里做角色校验）
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) {
            result.put("code", 403);
            result.put("message", "仅学生可取消订单");
            return result;
        }

        // 这里理想情况下应该再校验一下该订单是否属于该用户
        RepairOrder order = repairOrderService.getOrderById(orderId);
        Long userId = (Long) request.getAttribute("userId");
        if (order == null || !order.getUserId().equals(userId)) {
            result.put("code", 403);
            result.put("message", "您无权操作此订单");
            return result;
        }
        
        boolean success = repairOrderService.cancelOrder(orderId);
        if (success) {
            result.put("code", 200);
            result.put("message", "订单取消成功");
        } else {
            result.put("code", 400);
            result.put("message", "订单取消失败，可能订单已在处理中");
        }
        
        return result;
    }

    /**
     * 获取报修单列表（管理员用，支持按状态筛选）
     * 
     * @param status 订单状态
     * @param request 请求对象
     * @return 订单列表
     */
    @GetMapping("/all")
    public Map<String, Object> getAllOrders(@RequestParam(required = false) Integer status, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 权限校验
        Integer role = (Integer) request.getAttribute("role");
        logger.info("获取所有报修单, role: {}, status: {}", role, status);

        if (role == null || role != 2) {
            result.put("code", 403);
            result.put("message", "权限不足，仅管理员可访问");
            return result;
        }
        
        List<RepairOrder> orders = repairOrderService.getAllOrders(status);
        logger.info("获取到报修单数量: {}", orders.size());

        result.put("code", 200);
        result.put("message", "获取所有订单列表成功");
        result.put("orders", orders);
        
        return result;
    }

    /**
     * 删除报修单（管理员用）
     * 
     * @param orderId 订单ID
     * @param request 请求对象
     * @return 删除结果
     */
    @PostMapping("/delete")
    public Map<String, Object> deleteOrder(@RequestParam Long orderId, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 权限校验
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            result.put("code", 403);
            result.put("message", "权限不足，仅管理员可访问");
            return result;
        }
        
        boolean success = repairOrderService.deleteOrder(orderId);
        if (success) {
            result.put("code", 200);
            result.put("message", "订单删除成功");
        } else {
            result.put("code", 400);
            result.put("message", "订单删除失败");
        }
        
        return result;
    }

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param status 新状态
     * @param request 请求对象
     * @return 更新结果
     */
    @PostMapping("/status")
    public Map<String, Object> updateOrderStatus(@RequestParam Long orderId, @RequestParam Integer status, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        // 权限校验
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            result.put("code", 403);
            result.put("message", "权限不足，仅管理员可更新状态");
            return result;
        }
        
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