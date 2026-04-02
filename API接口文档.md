# 宿舍报修系统API接口文档

## 1. 认证相关接口

### 1.1 登录接口
- **请求路径**: `/auth/login`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | account | String | 是 | 账号 |
  | password | String | 是 | 密码 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "token": "JWT令牌",
    "user": {
      "id": 1,
      "account": "user123",
      "role": 0
    }
  }
  ```

### 1.2 注册接口
- **请求路径**: `/auth/register`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | account | String | 是 | 账号 |
  | password | String | 是 | 密码 |
  | role | Integer | 是 | 角色（0：学生，1：管理员） |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "注册成功"
  }
  ```

## 2. 宿舍绑定相关接口

### 2.1 绑定宿舍接口
- **请求路径**: `/dorm/bind`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | building | String | 是 | 楼栋 |
  | room | String | 是 | 房间 |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "绑定成功"
  }
  ```

### 2.2 获取宿舍绑定信息接口
- **请求路径**: `/dorm/info`
- **请求方法**: POST
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "获取成功",
    "binding": {
      "id": 1,
      "userId": 1,
      "building": "A栋",
      "room": "101"
    }
  }
  ```

### 2.3 修改宿舍绑定信息接口
- **请求路径**: `/dorm/update`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | building | String | 是 | 楼栋 |
  | room | String | 是 | 房间 |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "修改成功"
  }
  ```

## 3. 维修订单相关接口

### 3.1 创建维修订单接口
- **请求路径**: `/repair/create`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | deviceType | String | 是 | 设备类型 |
  | description | String | 是 | 故障描述 |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
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
      "createTime": "2024-01-01 10:00:00"
    }
  }
  ```

### 3.2 上传订单图片接口
- **请求路径**: `/repair/upload`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | orderId | Long | 是 | 订单ID |
  | file | MultipartFile | 是 | 图片文件 |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "图片上传成功",
    "imagePath": "/upload/20240101/12345.jpg"
  }
  ```

### 3.3 获取用户的维修订单列表接口
- **请求路径**: `/repair/list`
- **请求方法**: GET
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
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
        "createTime": "2024-01-01 10:00:00"
      }
    ]
  }
  ```

### 3.4 获取订单详情接口
- **请求路径**: `/repair/detail/{orderId}`
- **请求方法**: GET
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | orderId | Long | 是 | 订单ID |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
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
      "createTime": "2024-01-01 10:00:00"
    }
  }
  ```

### 3.5 更新订单状态接口
- **请求路径**: `/repair/status`
- **请求方法**: POST
- **请求参数**:
  | 参数名 | 类型 | 必选 | 描述 |
  |--------|------|------|------|
  | orderId | Long | 是 | 订单ID |
  | status | Integer | 是 | 新状态（0：待处理，1：处理中，2：已完成） |
- **请求头**:
  | 头名 | 值 |
  |------|------|
  | Authorization | Bearer JWT令牌 |
- **返回结果**:
  ```json
  {
    "code": 200,
    "message": "状态更新成功"
  }
  ```

## 4. 状态码说明

| 状态码 | 描述 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 系统异常 |