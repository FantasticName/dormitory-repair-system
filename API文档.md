# 宿舍报修系统 API 接口文档

## 1. 认证相关接口

### 1.1 登录接口
- **接口路径**: `/api/auth/login`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `account` | String | 是 | 用户账号 |
  | `password` | String | 是 | 用户密码 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "token": "JWT token",
    "user": {
      "id": 1,
      "account": "user123",
      "role": 1,
      "name": "张三"
    }
  }
  ```

### 1.2 注册接口
- **接口路径**: `/api/auth/register`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `account` | String | 是 | 用户账号 |
  | `password` | String | 是 | 用户密码 |
  | `role` | Integer | 是 | 角色类型：1-学生，2-管理员 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "注册成功"
  }
  ```

## 2. 维修订单相关接口

### 2.1 创建维修订单
- **接口路径**: `/api/repair/create`
- **请求方法**: POST
- **请求头**: `Authorization: JWT token`
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `deviceType` | String | 是 | 设备类型 |
  | `description` | String | 是 | 故障描述 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "订单创建成功",
    "order": {
      "id": 1,
      "userId": 1,
      "deviceType": "空调",
      "description": "不制冷",
      "status": 0,
      "createdAt": "2026-03-30T10:00:00",
      "updatedAt": "2026-03-30T10:00:00"
    }
  }
  ```

### 2.2 上传订单图片
- **接口路径**: `/api/repair/upload`
- **请求方法**: POST
- **请求头**: `Authorization: JWT token`
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `orderId` | Long | 是 | 订单ID |
  | `file` | File | 是 | 图片文件 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "图片上传成功",
    "imagePath": "uploads/1234567890.jpg"
  }
  ```

### 2.3 获取用户订单列表
- **接口路径**: `/api/repair/list`
- **请求方法**: GET
- **请求头**: `Authorization: JWT token`
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "获取订单列表成功",
    "orders": [
      {
        "id": 1,
        "userId": 1,
        "deviceType": "空调",
        "description": "不制冷",
        "status": 0,
        "createdAt": "2026-03-30T10:00:00",
        "updatedAt": "2026-03-30T10:00:00",
        "imagePath": "uploads/1234567890.jpg"
      }
    ]
  }
  ```

### 2.4 获取订单详情
- **接口路径**: `/api/repair/detail/{orderId}`
- **请求方法**: GET
- **请求头**: `Authorization: JWT token`
- **路径参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `orderId` | Long | 是 | 订单ID |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "获取订单详情成功",
    "order": {
      "id": 1,
      "userId": 1,
      "deviceType": "空调",
      "description": "不制冷",
      "status": 0,
      "createdAt": "2026-03-30T10:00:00",
      "updatedAt": "2026-03-30T10:00:00",
      "imagePath": "uploads/1234567890.jpg"
    }
  }
  ```

### 2.5 更新订单状态
- **接口路径**: `/api/repair/status`
- **请求方法**: POST
- **请求头**: `Authorization: JWT token`
- **请求参数**:
  | 参数名 | 类型 | 必填 | 描述 |
  | :--- | :--- | :--- | :--- |
  | `orderId` | Long | 是 | 订单ID |
  | `status` | Integer | 是 | 订单状态：0-待处理，1-处理中，2-已完成，3-已取消 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "状态更新成功"
  }
  ```

## 3. 状态码说明

| 状态码 | 描述 |
| :--- | :--- |
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，token无效或过期 |
| 404 | 资源不存在 |
| 500 | 系统内部错误 |

## 4. 注意事项

1. 所有需要认证的接口都需要在请求头中添加 `Authorization: JWT token`
2. 文件上传接口的请求方式为 `multipart/form-data`
3. 其他接口的请求方式为 `application/x-www-form-urlencoded`
4. 响应格式统一为JSON格式
