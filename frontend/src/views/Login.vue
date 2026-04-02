<template>
  <div class="login-container">
    <h1>宿舍报修系统</h1>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="account">账号</label>
        <input type="text" id="account" v-model="form.account" required>
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input type="password" id="password" v-model="form.password" required>
      </div>
      <button type="submit" class="btn">登录</button>
      <p class="register-link">还没有账号？<router-link to="/register">立即注册</router-link></p>
    </form>
  </div>
</template>

<script>
import api from '../utils/api'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        account: '',
        password: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      try {
        const params = new URLSearchParams()
        params.append('account', this.form.account)
        params.append('password', this.form.password)
        const response = await api.post('/auth/login', params)
        if (response.code === 200) {
          localStorage.setItem('token', response.token)
          localStorage.setItem('user', JSON.stringify(response.user))
          this.$router.push('/home')
        }
      } catch (error) {
        alert(error.response?.data?.message || '登录失败')
      }
    }
  }
}
</script>

<style scoped>
.login-container {
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

input {
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

.register-link {
  text-align: center;
  margin-top: 15px;
  color: #666;
}

.register-link a {
  color: #4CAF50;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>