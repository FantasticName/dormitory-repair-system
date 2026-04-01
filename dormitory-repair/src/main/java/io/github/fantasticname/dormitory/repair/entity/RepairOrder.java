package io.github.fantasticname.dormitory.repair.entity;

import java.time.LocalDateTime;

/**
 * 维修订单实体类
 * 对应数据库dorm_biding中的repair_order表
 * 
 * @author FantasticName
 */
public class RepairOrder {
    /**
     * 订单ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 设备类型
     */
    private String deviceType;
    
    /**
     * 故障描述
     */
    private String description;
    
    /**
     * 订单状态：0待处理 1处理中 2已完成 3已取消
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 图片路径
     */
    private String imagePath;

    /**
     * 构造方法
     */
    public RepairOrder() {
    }

    /**
     * 获取订单ID
     * 
     * @return 订单ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置订单ID
     * 
     * @param id 订单ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     * 
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取设备类型
     * 
     * @return 设备类型
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 设置设备类型
     * 
     * @param deviceType 设备类型
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 获取故障描述
     * 
     * @return 故障描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置故障描述
     * 
     * @param description 故障描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取订单状态
     * 
     * @return 订单状态：0待处理 1处理中 2已完成 3已取消
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态
     * 
     * @param status 订单状态：0待处理 1处理中 2已完成 3已取消
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取创建时间
     * 
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     * 
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取更新时间
     * 
     * @return 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间
     * 
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 获取图片路径
     * 
     * @return 图片路径
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 设置图片路径
     * 
     * @param imagePath 图片路径
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
