import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/create-order',
    name: 'CreateOrder',
    component: () => import('../views/CreateOrder.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/order-detail/:id',
    name: 'OrderDetail',
    component: () => import('../views/OrderDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/dorm-binding',
    name: 'DormBinding',
    component: () => import('../views/DormBinding.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('token')
    if (!token) {
      next({ path: '/login' })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router