# 宿舍报修管理系统 API 接口文档

## 1. 项目说明
- **后端基础路径**: `/api`
- **认证方式**: 基于 JWT 的登录鉴权。
- **认证头**: `Authorization: Bearer <token>`
- **角色定义**: 
  - `1`: 学生 (Student)
  - `2`: 维修人员/管理员 (Admin)

---

## 2. 认证模块 (Auth)
基础路径: `/auth`

### 2.1 用户注册
- **接口地址**: `/register`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | account | String | 是 | 账号 (学生: 3125/3225开头, 管理员: 0025开头, 共10位) |
  | password | String | 是 | 密码 |
  | role | Integer | 是 | 角色 (1:学生, 2:管理员) |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "注册成功"
  }
  ```

### 2.2 用户登录
- **接口地址**: `/login`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | account | String | 是 | 账号 |
  | password | String | 是 | 密码 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "user": {
      "id": 1,
      "account": "3125004123",
      "role": 1
    }
  }
  ```

---

## 3. 用户模块 (User)
基础路径: `/user` (需要认证)

### 3.1 获取个人信息
- **接口地址**: `/profile`
- **请求方式**: `GET`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取用户信息成功",
    "user": {
      "id": 1,
      "account": "3125004123",
      "role": 1
    }
  }
  ```

### 3.2 修改密码
- **接口地址**: `/password`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | oldPassword | String | 是 | 旧密码 |
  | newPassword | String | 是 | 新密码 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "密码修改成功"
  }
  ```

---

## 4. 宿舍绑定模块 (Dorm Binding)
基础路径: `/dorm` (需要认证，仅限学生)

### 4.1 绑定宿舍
- **接口地址**: `/bind`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | building | String | 是 | 楼栋 |
  | room | String | 是 | 房间号 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "绑定成功"
  }
  ```

### 4.2 修改/更新绑定信息
- **接口地址**: `/update`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | building | String | 是 | 楼栋 |
  | room | String | 是 | 房间号 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "修改成功"
  }
  ```

### 4.3 获取绑定信息
- **接口地址**: `/info`
- **请求方式**: `GET`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取绑定信息成功",
    "binding": {
      "userId": 1,
      "building": "A1",
      "room": "101"
    }
  }
  ```

---

## 5. 报修订单模块 (Repair Order)
基础路径: `/repair` (需要认证)

### 5.1 创建报修单
- **接口地址**: `/create`
- **请求方式**: `POST`
- **参数格式**: `multipart/form-data`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | deviceType | String | 是 | 设备类型 |
  | description | String | 是 | 故障描述 |
  | file | MultipartFile | 否 | 故障图片文件 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "报修单创建成功",
    "order": {
      "id": 10,
      "deviceType": "空调",
      "description": "不制冷",
      "status": 0,
      "imagePath": "uploads/uuid.png"
    }
  }
  ```

### 5.2 获取个人报修列表 (学生)
- **接口地址**: `/list`
- **请求方式**: `GET`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取订单列表成功",
    "orders": [...]
  }
  ```

### 5.3 获取所有报修列表 (管理员)
- **接口地址**: `/all`
- **请求方式**: `GET`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | status | Integer | 否 | 状态筛选 (0:待处理, 1:处理中, 2:已完成, 3:已取消) |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取所有订单列表成功",
    "orders": [...]
  }
  ```

### 5.4 获取订单详情
- **接口地址**: `/detail/{orderId}`
- **请求方式**: `GET`
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "获取订单详情成功",
    "order": { ... }
  }
  ```

### 5.5 修改订单状态 (管理员)
- **接口地址**: `/status`
- **请求方式**: `POST`
- **参数格式**: `application/x-www-form-urlencoded`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | orderId | Long | 是 | 订单ID |
  | status | Integer | 是 | 新状态 (0:待处理, 1:处理中, 2:已完成) |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "状态更新成功"
  }
  ```

### 5.6 取消报修单 (学生)
- **接口地址**: `/cancel`
- **请求方式**: `POST`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | orderId | Long | 是 | 订单ID |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "订单取消成功"
  }
  ```

### 5.7 删除报修单 (管理员)
- **接口地址**: `/delete`
- **请求方式**: `POST`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | orderId | Long | 是 | 订单ID |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "订单删除成功"
  }
  ```

### 5.8 上传/更新图片
- **接口地址**: `/upload`
- **请求方式**: `POST`
- **参数格式**: `multipart/form-data`
- **参数列表**:
  | 参数名 | 类型 | 必须 | 说明 |
  | :--- | :--- | :--- | :--- |
  | orderId | Long | 是 | 订单ID |
  | file | MultipartFile | 是 | 图片文件 |
- **响应示例**:
  ```json
  {
    "code": 200,
    "message": "图片上传成功",
    "imagePath": "uploads/uuid.png"
  }
  ```

---

## 6. 静态资源
- **上传图片访问**: `/api/uploads/{filename}` (无需认证)
