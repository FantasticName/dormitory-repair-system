# 宿舍报修管理系统（第一周）

## 项目简介

本项目是QG后台一轮考核第一周项目

本项目为“宿舍报修管理系统”，采用纯 MyBatis + MySQL + 控制台交互，实现学生报修与管理员管理的核心功能。项目代码采用三层架构设计。

- **Java 17**
- **Maven **
- **MyBatis **
- **MySQL **
- **控制台交互（无前端）**

## 功能列表

### 公共功能

- 用户注册（区分学生/管理员，账号正则校验）【首次登录的学生会强制绑定宿舍】
- 用户登录
- 修改密码
- 查看个人信息

### 学生功能

- 绑定/修改宿舍（楼栋号 + 房间号）
- 创建报修单（设备类型、问题描述）
- 查看个人报修记录
- 取消报修单

### 管理员功能

- 查询所有报修单（支持按状态筛选）
- 查看报修单详情
- 更新报修单状态
- 删除报修单

## 项目结构

text

```
src/main/java/io/github/fantasticname/dormitory/repair
├── controller          # 控制台菜单交互层
│   └── ConsoleController.java
├── entity              # 实体类（对应数据库表）
│   ├── User.java
│   ├── DormBinding.java
│   └── RepairOrder.java
├── mapper              # MyBatis Mapper 接口
│   ├── UserMapper.java
│   ├── DormBindingMapper.java
│   └── RepairOrderMapper.java
├── service             # 业务逻辑层
│   ├── UserService.java
│   ├── DormBindingService.java
│   └── RepairOrderService.java
└── util                # 工具类
|   ├── AccountValidatorWithRegularExpressionUtil.java    # 使用正则表达式进行账号格式校验
|   ├── ConsoleUtil.java         # 控制台输入封装
|   └── SqlSessionUtil.java      # MyBatis 工具类（管理 SqlSessionFactory）
|
└── MainApp.java 

src/main/resources
├── mybatis-config.xml          # MyBatis 核心配置文件
└── mapper                      # XML 映射文件（存放 SQL 语句）
    ├── UserMapper.xml
    ├── DormBindingMapper.xml
    └── RepairOrderMapper.xml
```



## 数据库设计

建表语句：

MySQL

```Mysql
CREATE DATABASE dormitory_repair;
USE dormitory_repair;

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `account` VARCHAR(20) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `role` TINYINT NOT NULL COMMENT '1-学生,2-管理员',
    `name` VARCHAR(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 宿舍绑定表
CREATE TABLE `dorm_binding` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `building` VARCHAR(10) NOT NULL,
    `room` VARCHAR(10) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
);

-- 报修单表
CREATE TABLE `repair_order` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `device_type` VARCHAR(20) NOT NULL,
    `description` TEXT,
    `status` TINYINT DEFAULT 0 COMMENT '0-待处理,1-处理中,2-已完成,3-已取消',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);
```



## 如何运行

### 环境要求

- JDK 17
- Maven 3.9+
- MySQL 8.0

### 步骤

1. **克隆项目**（或直接导入已编写的代码）
2. **修改数据库配置**
   打开 `src/main/resources/mybatis-config.xml`，将 `<dataSource>` 中的 `username` 和 `password` 改为你自己的 MySQL 账号密码。
3. **创建数据库表**
   在 MySQL 中执行上述 SQL 语句。
4. **编译并运行**
   使用 IDEA 打开项目，右键 `MainApp` 类 → `Run 'MainApp.main()'`
5. **开始使用**
   根据控制台菜单提示操作即可。

## 注意事项

- 想不出来。

## 未来扩展方向（可选）

- 根据阿里巴巴编码规范，采用javadoc进行规范注释（也有可能你看到这个项目的时候已经改过来了，而我忘了修改README.md）
- 消息通知，保修单优先级，维修评价等等。

- 引入密码加密

- 增加日志框架

- 密码输入隐藏

  

------

**项目作者**：FantasticName 
**项目时间**：2026年3月22日
