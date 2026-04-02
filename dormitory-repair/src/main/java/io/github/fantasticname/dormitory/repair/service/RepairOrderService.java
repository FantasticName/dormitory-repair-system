package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import io.github.fantasticname.dormitory.repair.mapper.RepairOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 维修订单服务类
 * 
 * @author FantasticName
 */
@Service
public class RepairOrderService {

    private static final Logger logger = LoggerFactory.getLogger(RepairOrderService.class);

    @Autowired
    private RepairOrderMapper repairOrderMapper;

    /**
     * 创建报修单
     * 
     * @param userId 用户ID
     * @param deviceType 设备类型
     * @param description 故障描述
     * @return 维修订单对象
     */
    @Transactional
    public RepairOrder createOrder(Long userId, String deviceType, String description) {
        return createOrder(userId, deviceType, description, null);
    }
    


    /**
     * 创建报修单（带图片路径）
     * 
     * @param userId 用户ID
     * @param deviceType 设备类型
     * @param description 故障描述
     * @param imagePath 图片路径
     * @return 维修订单对象
     */
    @Transactional
    public RepairOrder createOrder(Long userId, String deviceType, String description, String imagePath) {
        // 创建报修单实体对象
        RepairOrder order = new RepairOrder();
        order.setUserId(userId);
        order.setDeviceType(deviceType);
        order.setDescription(description);
        order.setStatus(0); // 待处理
        order.setImagePath(imagePath);

        // 写入数据库
        int rows = repairOrderMapper.insertOrder(order);
        if (rows > 0) {
            logger.info("创建订单成功: {}", order.getId());
            return order;
        }
        return null;
    }
    


    /**
     * 查询学生的所有报修单
     * 
     * @param userId 用户ID
     * @return 维修订单列表
     */
    public List<RepairOrder> getOrdersByUserId(Long userId) {
        return repairOrderMapper.findOrderByUserId(userId);
    }
    




    /**
     * 按照状态查询所有报修单（管理员用）
     * 
     * @param status 订单状态
     * @return 维修订单列表
     */
    public List<RepairOrder> getAllOrders(Integer status) {
        logger.info("Service: 查询所有订单, status: {}", status);
        List<RepairOrder> orders = repairOrderMapper.findAllByStatus(status);
        logger.info("Service: 查询到订单数量: {}", orders != null ? orders.size() : 0);
        return orders;
    }




    /**
     * 查看单个报修单详情
     * 
     * @param orderId 订单ID
     * @return 维修订单对象
     */
    public RepairOrder getOrderById(Long orderId) {
        return repairOrderMapper.findOrderById(orderId);
    }




    /**
     * 修改订单状态
     * 
     * @param orderId 订单ID
     * @param newStatus 新状态
     * @return 是否成功
     */
    @Transactional
    public boolean updateOrderStatus(Long orderId, Integer newStatus) {
        RepairOrder order = repairOrderMapper.findOrderById(orderId);
        if (order == null) {
            logger.warn("订单不存在: {}", orderId);
            return false;
        }

        int rows = repairOrderMapper.updateStatus(orderId, newStatus);
        logger.info("更新订单状态成功: {}, 状态: {}", orderId, newStatus);
        return rows > 0;
    }
    


    /**
     * 删除报修单
     * 
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional
    public boolean deleteOrder(Long orderId) {
        int rows = repairOrderMapper.deleteOrderById(orderId);
        logger.info("删除订单成功: {}", orderId);
        return rows > 0;
    }



    /**
     * 取消报修单（只有状态为0待处理的可以取消）
     * 
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Transactional
    public boolean cancelOrder(Long orderId) {
        RepairOrder order = repairOrderMapper.findOrderById(orderId);
        if (order == null || order.getStatus() != 0) {
            logger.warn("取消订单失败: 订单不存在或状态不是待处理");
            return false;
        }
        int rows = repairOrderMapper.updateStatus(orderId, 3); // 3表示已取消
        logger.info("取消订单成功: {}", orderId);
        return rows > 0;
    }


    
    /**
     * 更新订单图片路径
     * 
     * @param orderId 订单ID
     * @param imagePath 图片路径
     * @return 是否成功
     */
    @Transactional
    public boolean updateImagePath(Long orderId, String imagePath) {
        RepairOrder order = repairOrderMapper.findOrderById(orderId);
        if (order == null) {
            logger.warn("订单不存在: {}", orderId);
            return false;
        }

        int rows = repairOrderMapper.updateImagePath(orderId, imagePath);
        logger.info("更新订单图片路径成功: {}", orderId);
        return rows > 0;
    }
}