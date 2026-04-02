<template>
  <div class="create-order-container">
    <header class="header">
      <h1>创建报修订单</h1>
      <button class="back-btn" @click="$router.push('/home')">返回</button>
    </header>
    <form @submit.prevent="handleCreateOrder" class="order-form">
      <div class="form-group">
        <label for="deviceType">设备类型</label>
        <input type="text" id="deviceType" v-model="form.deviceType" required>
      </div>
      <div class="form-group">
        <label for="description">故障描述</label>
        <textarea id="description" v-model="form.description" rows="4" required></textarea>
      </div>
      <button type="submit" class="btn">提交订单</button>
    </form>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'CreateOrder',
  data() {
    return {
      form: {
        deviceType: '',
        description: ''
      }
    }
  },
  methods: {
    async handleCreateOrder() {
      try {
        const params = new URLSearchParams()
        params.append('deviceType', this.form.deviceType)
        params.append('description', this.form.description)
        const response = await api.post('/repair/create', params)
        if (response.code === 200) {
          alert('订单创建成功')
          this.$router.push('/home')
        }
      } catch (error) {
        alert(error.response?.data?.message || '订单创建失败')
      }
    }
  }
}
</script>

<style scoped>
.create-order-container {
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

.order-form {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

label {
  font-weight: bold;
  color: #555;
}

input, textarea {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  font-family: inherit;
}

.btn {
  padding: 12px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}

.btn:hover {
  background: #45a049;
}
</style>