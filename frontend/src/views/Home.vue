<template>
  <div class="home-container">
    <header class="header">
      <h1>宿舍报修系统</h1>
      <div class="user-info">
        <span>{{ user?.account }}</span>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </header>
    <nav class="nav">
      <button class="nav-btn" @click="$router.push('/create-order')">创建报修</button>
      <button class="nav-btn" @click="$router.push('/dorm-binding')">宿舍绑定</button>
    </nav>
    <main class="main">
      <h2>我的报修订单</h2>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="orders.length === 0" class="empty">暂无订单</div>
      <ul v-else class="order-list">
        <li v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <span class="order-id">订单号: {{ order.id }}</span>
            <span :class="['status', getStatusClass(order.status)]">{{ getStatusText(order.status) }}</span>
          </div>
          <div class="order-content">
            <p><strong>设备类型:</strong> {{ order.deviceType }}</p>
            <p><strong>故障描述:</strong> {{ order.description }}</p>
            <p><strong>创建时间:</strong> {{ order.createTime }}</p>
          </div>
          <div class="order-actions">
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
      loading: true
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
        const response = await api.get('/repair/list')
        if (response.code === 200) {
          this.orders = response.orders
        }
      } catch (error) {
        alert('获取订单失败')
      } finally {
        this.loading = false
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

.main h2 {
  color: #333;
  margin-bottom: 15px;
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