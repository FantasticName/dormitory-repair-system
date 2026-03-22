package io.github.fantasticname.dormitory.repair.entity;

import java.time.LocalDateTime;


/**
 * @author fantasticname
 * 绑定宿舍单实体类，对应是数据库dorm_biding中的dorm_binding表
 */
public class DormBinding {

    private Long id;
    private Long userId;
    private String building;
    private String room;
    private LocalDateTime createdAt;

    // 构造方法

    public DormBinding() {
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
