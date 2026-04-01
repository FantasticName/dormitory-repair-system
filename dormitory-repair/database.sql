CREATE DATABASE dormitory_repair;
USE dormitory_repair;

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `account` VARCHAR(20) NOT NULL UNIQUE COMMENT '账号（学号/工号）',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `role` TINYINT NOT NULL COMMENT '角色：1-学生，2-管理员',
    `name` VARCHAR(50) COMMENT '姓名',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '用户表';

-- 宿舍绑定表（学生绑定宿舍）
CREATE TABLE `dorm_binding` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE COMMENT '学生用户ID',
    `building` VARCHAR(10) NOT NULL COMMENT '楼栋号',
    `room` VARCHAR(10) NOT NULL COMMENT '房间号',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '宿舍绑定表';

-- 报修单表
CREATE TABLE `repair_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '报修学生ID',
    `device_type` VARCHAR(20) NOT NULL COMMENT '设备类型（如：水龙头、灯管）',
    `description` TEXT COMMENT '问题描述',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-处理中，2-已完成，3-已取消',
    `image_path` VARCHAR(255) COMMENT '图片路径',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间'
) COMMENT '报修单表';
