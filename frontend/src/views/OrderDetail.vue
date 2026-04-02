<template>
  <div class="order-detail-container">
    <header class="header">
      <h1>订单详情</h1>
      <button class="back-btn" @click="$router.push('/home')">返回</button>
    </header>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="!order" class="error">订单不存在</div>
    <div v-else class="order-info">
      <div class="info-item">
        <span class="label">订单号:</span>
        <span class="value">{{ order.id }}</span>
      </div>
      <div class="info-item">
        <span class="label">设备类型:</span>
        <span class="value">{{ order.deviceType }}</span>
      </div>
      <div class="info-item">
        <span class="label">故障描述:</span>
        <span class="value">{{ order.description }}</span>
      </div>
      <div class="info-item">
        <span class="label">状态:</span>
        <span :class="['status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span>
      </div>
      <div class="info-item">
        <span class="label">创建时间:</span>
        <span class="value">{{ order.createTime }}</span>
      </div>
      <div v-if="order.imagePath" class="info-item">
        <span class="label">故障图片:</span>
        <img :src="order.imagePath" alt="故障图片" class="order-image">
      </div>
      <div class="upload-section">
        <h3>上传故障图片</h3>
        <input type="file" @change="handleFileChange" class="file-input">
        <button v-if="file" @click="handleUpload" class="upload-btn">上传</button>
      </div>
      <div v-if="user?.role === 2" class="status-update">
        <h3>更新订单状态</h3>
        <select v-model="newStatus" class="status-select">
          <option value="0">待处理</option>
          <option value="1">处理中</option>
          <option value="2">已完成</option>
        </select>
        <button @click="handleUpdateStatus" class="update-btn">更新状态</button>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'OrderDetail',
  data() {
    return {
      order: null,
      loading: true,
      file: null,
      newStatus: 0,
      user: null
    }
  },
  mounted() {
    this.loadUserInfo()
    this.loadOrderDetail()
  },
  methods: {
    loadUserInfo() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        this.user = JSON.parse(userStr)
      }
    },
    async loadOrderDetail() {
      try {
        this.loading = true
        const { id } = this.$route.params
        const response = await api.get(`/repair/detail/${id}`)
        if (response.code === 200) {
          this.order = response.order
          this.newStatus = response.order.status
        }
      } catch (error) {
        alert('获取订单详情失败')
      } finally {
        this.loading = false
      }
    },
    handleFileChange(e) {
      this.file = e.target.files[0]
    },
    async handleUpload() {
      if (!this.file) {
        alert('请选择文件')
        return
      }
      try {
        const formData = new FormData()
        formData.append('orderId', this.order.id)
        formData.append('file', this.file)
        const response = await api.post('/repair/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        if (response.code === 200) {
          alert('图片上传成功')
          this.loadOrderDetail()
        }
      } catch (error) {
        alert('图片上传失败')
      }
    },
    async handleUpdateStatus() {
      try {
        const params = new URLSearchParams()
        params.append('orderId', this.order.id)
        params.append('status', this.newStatus)
        const response = await api.post('/repair/status', params)
        if (response.code === 200) {
          alert('状态更新成功')
          this.loadOrderDetail()
        }
      } catch (error) {
        alert('状态更新失败')
      }
    },
    getStatusClass(status) {
      switch (status) {
        case 0: return 'status-pending'
        case 1: return 'status-processing'
        case 2: return 'status-completed'
        default: return ''
      }
    },
    getStatusText(status) {
      switch (status) {
        case 0: return '待处理'
        case 1: return '处理中'
        case 2: return '已完成'
        default: return '未知'
      }
    }
  }
}
</script>

<style scoped>
.order-detail-container {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.header h1 {
  color: #333;
  margin: 0;
  font-size: 20px;
}

.back-btn {
  padding: 5px 10px;
  background: #666;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.back-btn:hover {
  background: #555;
}

.loading, .error {
  text-align: center;
  padding: 50px;
  color: #666;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.label {
  font-weight: bold;
  color: #555;
}

.value {
  color: #333;
  padding: 5px 0;
}

.status {
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
  width: fit-content;
}

.status-pending {
  background: #ffeb3b;
  color: #f57f17;
}

.status-processing {
  background: #2196f3;
  color: white;
}

.status-completed {
  background: #4caf50;
  color: white;
}

.order-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 4px;
  margin-top: 5px;
}

.upload-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.upload-section h3 {
  color: #333;
  margin-bottom: 10px;
}

.file-input {
  margin-bottom: 10px;
}

.upload-btn {
  padding: 8px 12px;
  background: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.upload-btn:hover {
  background: #1976d2;
}

.status-update {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.status-update h3 {
  color: #333;
  margin-bottom: 10px;
}

.status-select {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
  width: 100%;
}

.update-btn {
  padding: 8px 12px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.update-btn:hover {
  background: #45a049;
}
</style>