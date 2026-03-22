package io.github.fantasticname.dormitory.repair.entity;

import java.time.LocalDateTime;

/**
 * @author fantasticname
 * 维修表单实体类，对应是数据库dorm_biding中的repair_order表
 */
public class RepairOrder {
    private Long id;
    private Long userId;
    private String deviceType;
    private String description;


    private Integer status;
    // 0待处理 1处理中 2已完成 3已取消


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // 构造方法

    public RepairOrder() {
    }


    // getter and setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
