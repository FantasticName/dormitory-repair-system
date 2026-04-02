<template>
  <div class="home-container">
    <header class="header">
      <h1>宿舍报修系统</h1>
      <div class="user-info">
        <span>{{ user?.account }}</span>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </header>
    <nav class="nav" v-if="user?.role === 1">
      <button class="nav-btn" @click="$router.push('/create-order')">创建报修</button>
      <button class="nav-btn" @click="$router.push('/dorm-binding')">宿舍绑定</button>
    </nav>
    <main class="main">
      <div class="main-header">
        <h2>{{ user?.role === 2 ? '所有报修订单' : '我的报修订单' }}</h2>
        <div v-if="user?.role === 2" class="filter">
          <label>状态筛选:</label>
          <select v-model="statusFilter" @change="loadOrders">
            <option value="">全部</option>
            <option value="0">待处理</option>
            <option value="1">处理中</option>
            <option value="2">已完成</option>
            <option value="3">已取消</option>
          </select>
        </div>
      </div>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="orders.length === 0" class="empty">暂无订单</div>
      <ul v-else class="order-list">
        <li v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-main">
            <div class="order-text">
              <div class="order-header">
                <span class="order-id">订单号: {{ order.id }}</span>
                <span :class="['status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span>
              </div>
              <div class="order-content">
                <p><strong>设备类型:</strong> {{ order.deviceType }}</p>
                <p><strong>故障描述:</strong> {{ order.description }}</p>
                <p><strong>创建时间:</strong> {{ order.createTime }}</p>
              </div>
            </div>
            <div v-if="order.imagePath" class="order-thumbnail">
              <img :src="'/api/' + order.imagePath" alt="缩略图">
            </div>
          </div>
          <div class="order-actions">
            <button v-if="user?.role === 1 && order.status === 0" class="cancel-btn" @click="handleCancelOrder(order.id)">取消报修</button>
            <button v-if="user?.role === 2" class="delete-btn" @click="handleDeleteOrder(order.id)">删除订单</button>
            <button class="detail-btn" @click="$router.push(`/order-detail/${order.id}`)">查看详情</button>
          </div>
        </li>
      </ul>
    </main>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'Home',
  data() {
    return {
      user: null,
      orders: [],
      loading: true,
      statusFilter: ''
    }
  },
  mounted() {
    this.loadUserInfo()
    this.loadOrders()
  },
  methods: {
    loadUserInfo() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        this.user = JSON.parse(userStr)
      }
    },
    async loadOrders() {
      try {
        this.loading = true
        let response
        console.log('Current user role:', this.user?.role)
        if (this.user?.role === 2) { // 管理员
          const url = this.statusFilter !== '' ? `/repair/all?status=${this.statusFilter}` : '/repair/all'
          response = await api.get(url)
        } else {
          response = await api.get('/repair/list')
        }
        if (response.code === 200) {
          this.orders = response.orders
        }
      } catch (error) {
        alert('获取订单失败')
      } finally {
        this.loading = false
      }
    },
    async handleCancelOrder(orderId) {
      if (!confirm('确定要取消此报修单吗？')) {
        return
      }
      try {
        const params = new URLSearchParams()
        params.append('orderId', orderId)
        const response = await api.post('/repair/cancel', params)
        if (response.code === 200) {
          alert('报修单已取消')
          this.loadOrders()
        }
      } catch (error) {
        alert(error.response?.data?.message || '取消失败')
      }
    },
    async handleDeleteOrder(orderId) {
      if (!confirm('确定要删除此订单吗？')) {
        return
      }
      try {
        const params = new URLSearchParams()
        params.append('orderId', orderId)
        const response = await api.post('/repair/delete', params)
        if (response.code === 200) {
          alert('订单已删除')
          this.loadOrders()
        }
      } catch (error) {
        alert(error.response?.data?.message || '删除失败')
      }
    },
    handleLogout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      this.$router.push('/login')
    },
    getStatusClass(status) {
      switch (status) {
        case 0: return 'status-pending'
        case 1: return 'status-processing'
        case 2: return 'status-completed'
        case 3: return 'status-cancelled'
        default: return ''
      }
    },
    getStatusText(status) {
      switch (status) {
        case 0: return '待处理'
        case 1: return '处理中'
        case 2: return '已完成'
        case 3: return '已取消'
        default: return '未知'
      }
    }
  }
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
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
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logout-btn {
  padding: 5px 10px;
  background: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background: #da190b;
}

.nav {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.nav-btn {
  padding: 10px 15px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.nav-btn:hover {
  background: #45a049;
}

.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter select {
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.main h2 {
  color: #333;
  margin: 0;
}

.loading, .empty {
  text-align: center;
  padding: 50px;
  color: #666;
}

.order-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.order-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.order-main {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.order-text {
  flex: 1;
}

.order-thumbnail {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
}

.order-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 4px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.order-id {
  font-weight: bold;
  color: #333;
}

.status {
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
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

.status-cancelled {
  background: #9e9e9e;
  color: white;
}

.order-content {
  margin-bottom: 15px;
}

.order-content p {
  margin: 5px 0;
  color: #555;
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.cancel-btn {
  padding: 8px 12px;
  background: #ff9800;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.cancel-btn:hover {
  background: #f57c00;
}

.delete-btn {
  padding: 8px 12px;
  background: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-btn:hover {
  background: #da190b;
}

.detail-btn {
  padding: 8px 12px;
  background: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.detail-btn:hover {
  background: #1976d2;
}
</style>