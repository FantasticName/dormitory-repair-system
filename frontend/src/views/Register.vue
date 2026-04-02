<template>
  <div class="register-container">
    <h1>注册账号</h1>
    <form @submit.prevent="handleRegister">
      <div class="form-group">
        <label for="account">账号</label>
        <input type="text" id="account" v-model="form.account" required>
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="form.password" required>
      </div>
      <div class="form-group">
        <label for="role">角色</label>
        <select id="role" v-model="form.role" required>
          <option value="1">学生</option>
          <option value="2">管理员</option>
        </select>
      </div>
      <button type="submit" class="btn">注册</button>
      <p class="login-link">已有账号？<router-link to="/login">立即登录</router-link></p>
    </form>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'Register',
  data() {
    return {
      form: {
        account: '',
        password: '',
        role: 0
      }
    }
  },
  methods: {
    async handleRegister() {
      try {
        const params = new URLSearchParams()
        params.append('account', this.form.account)
        params.append('password', this.form.password)
        params.append('role', this.form.role)
        const response = await api.post('/auth/register', params)
        if (response.code === 200) {
          alert('注册成功，请登录')
          this.$router.push('/login')
        }
      } catch (error) {
        alert(error.response?.data?.message || '注册失败')
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 100px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input, select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.btn {
  width: 100%;
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

.login-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.login-link a {
  color: #4CAF50;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>