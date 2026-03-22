package io.github.fantasticname.dormitory.repair.entity;

import java.time.LocalDateTime;



/**
 * @author fantasticname
 * 用户表实体类，对应是数据库dorm_biding中user表
 */
public class User {
    private Long id;
    private String account;
    private String password;

    private Integer role;
    // 1-学生, 2-管理员


    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /**
     * 构造方法
     */
    public User() {
    }

    /**
     * getter 方法和setter 方法
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
