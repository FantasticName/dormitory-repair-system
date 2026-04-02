<template>
  <div class="dorm-binding-container">
    <header class="header">
      <h1>宿舍绑定</h1>
      <button class="back-btn" @click="$router.push('/home')">返回</button>
    </header>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else class="binding-form">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="building">楼栋</label>
          <input type="text" id="building" v-model="form.building" required>
        </div>
        <div class="form-group">
          <label for="room">房间</label>
          <input type="text" id="room" v-model="form.room" required>
        </div>
        <button type="submit" class="btn">{{ isBound ? '修改绑定' : '绑定宿舍' }}</button>
      </form>
      <div v-if="binding" class="current-binding">
        <h3>当前绑定信息</h3>
        <p>楼栋: {{ binding.building }}</p>
        <p>房间: {{ binding.room }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'DormBinding',
  data() {
    return {
      form: {
        building: '',
        room: ''
      },
      binding: null,
      loading: true,
      isBound: false
    }
  },
  mounted() {
    this.loadBindingInfo()
  },
  methods: {
    async loadBindingInfo() {
      try {
        this.loading = true
        const response = await api.post('/dorm/info')
        if (response.code === 200 && response.binding) {
          this.binding = response.binding
          this.form.building = response.binding.building
          this.form.room = response.binding.room
          this.isBound = true
        }
      } catch (error) {
        // 未绑定宿舍时会返回400错误，这里不处理
      } finally {
        this.loading = false
      }
    },
    async handleSubmit() {
      try {
        let response
        const params = new URLSearchParams()
        params.append('building', this.form.building)
        params.append('room', this.form.room)
        
        if (this.isBound) {
          response = await api.post('/dorm/update', params)
        } else {
          response = await api.post('/dorm/bind', params)
        }
        if (response.code === 200) {
          alert(this.isBound ? '修改成功' : '绑定成功')
          this.loadBindingInfo()
        }
      } catch (error) {
        alert(error.response?.data?.message || '操作失败')
      }
    }
  }
}
</script>

<style scoped>
.dorm-binding-container {
  max-width: 400px;
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

.loading {
  text-align: center;
  padding: 50px;
  color: #666;
}

.binding-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

form {
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

input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
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

.current-binding {
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 4px;
  background: #f9f9f9;
}

.current-binding h3 {
  color: #333;
  margin-top: 0;
  margin-bottom: 10px;
}

.current-binding p {
  margin: 5px 0;
  color: #555;
}
</style>