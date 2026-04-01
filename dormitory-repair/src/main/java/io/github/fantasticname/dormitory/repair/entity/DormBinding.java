package io.github.fantasticname.dormitory.repair.entity;

import java.time.LocalDateTime;

/**
 * 宿舍绑定实体类
 * 对应数据库dorm_biding中的dorm_binding表
 * 
 * @author FantasticName
 */
public class DormBinding {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 楼栋
     */
    private String building;
    
    /**
     * 房间号
     */
    private String room;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 构造方法
     */
    public DormBinding() {
    }

    /**
     * 获取绑定ID
     * 
     * @return 绑定ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置绑定ID
     * 
     * @param id 绑定ID
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
     * 获取楼栋
     * 
     * @return 楼栋
     */
    public String getBuilding() {
        return building;
    }

    /**
     * 设置楼栋
     * 
     * @param building 楼栋
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * 获取房间号
     * 
     * @return 房间号
     */
    public String getRoom() {
        return room;
    }

    /**
     * 设置房间号
     * 
     * @param room 房间号
     */
    public void setRoom(String room) {
        this.room = room;
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
}
