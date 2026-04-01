// API基础URL
const API_BASE_URL = 'http://localhost:8080/api';

// 处理CORS问题
function handleCORS() {
    // 由于我们直接打开HTML文件，可能会遇到CORS问题
    // 这里添加一个简单的提示，告诉用户如何处理
    if (window.location.protocol === 'file:') {
        console.warn('您正在直接打开HTML文件，可能会遇到CORS问题。建议使用本地服务器运行前端，例如：python -m http.server 3000');
    }
}

// 获取本地存储的token
function getToken() {
    return localStorage.getItem('token');
}

// 保存token到本地存储
function saveToken(token) {
    localStorage.setItem('token', token);
}

// 清除本地存储的token
function clearToken() {
    localStorage.removeItem('token');
}

// 检查是否已登录
function checkLogin() {
    return getToken() !== null;
}

// 登录功能
if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const account = document.getElementById('account').value;
        const password = document.getElementById('password').value;
        
        fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `account=${encodeURIComponent(account)}&password=${encodeURIComponent(password)}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                saveToken(data.token);
                localStorage.setItem('user', JSON.stringify(data.user));
                window.location.href = 'home.html';
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('登录失败:', error);
            alert('登录失败，请稍后重试');
        });
    });
}

// 注册功能
if (document.getElementById('registerForm')) {
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const account = document.getElementById('account').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;
        
        fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `account=${encodeURIComponent(account)}&password=${encodeURIComponent(password)}&role=${encodeURIComponent(role)}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert('注册成功，请登录');
                window.location.href = 'index.html';
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('注册失败:', error);
            alert('注册失败，请稍后重试');
        });
    });
}

// 首页功能
if (window.location.pathname.includes('home.html')) {
    // 检查登录状态
    if (!checkLogin()) {
        window.location.href = 'index.html';
    }
    
    // 加载订单列表
    function loadOrders() {
        fetch(`${API_BASE_URL}/repair/list`, {
            headers: {
                'Authorization': `Bearer ${getToken()}`
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                renderOrders(data.orders);
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('获取订单列表失败:', error);
            alert('获取订单列表失败，请稍后重试');
        });
    }
    
    // 渲染订单列表
    function renderOrders(orders) {
        const orderList = document.getElementById('orderList');
        orderList.innerHTML = '';
        
        if (orders && orders.length > 0) {
            orders.forEach(order => {
                const orderItem = document.createElement('div');
                orderItem.className = 'order-item';
                orderItem.onclick = () => showOrderDetail(order.id);
                
                let statusText = '';
                let statusClass = '';
                switch (order.status) {
                    case 0:
                        statusText = '待处理';
                        statusClass = 'status-0';
                        break;
                    case 1:
                        statusText = '处理中';
                        statusClass = 'status-1';
                        break;
                    case 2:
                        statusText = '已完成';
                        statusClass = 'status-2';
                        break;
                    case 3:
                        statusText = '已取消';
                        statusClass = 'status-3';
                        break;
                }
                
                orderItem.innerHTML = `
                    <h3>订单 #${order.id}</h3>
                    <p>设备类型: ${order.deviceType}</p>
                    <p>故障描述: ${order.description}</p>
                    <p>创建时间: ${order.createdAt}</p>
                    <span class="order-status ${statusClass}">${statusText}</span>
                `;
                
                orderList.appendChild(orderItem);
            });
        } else {
            orderList.innerHTML = '<p>暂无报修单</p>';
        }
    }
    
    // 显示订单详情
    function showOrderDetail(orderId) {
        fetch(`${API_BASE_URL}/repair/detail/${orderId}`, {
            headers: {
                'Authorization': `Bearer ${getToken()}`
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const order = data.order;
                const orderDetail = document.getElementById('orderDetail');
                
                let statusText = '';
                switch (order.status) {
                    case 0:
                        statusText = '待处理';
                        break;
                    case 1:
                        statusText = '处理中';
                        break;
                    case 2:
                        statusText = '已完成';
                        break;
                    case 3:
                        statusText = '已取消';
                        break;
                }
                
                let imageHtml = '';
                if (order.imagePath) {
                    imageHtml = `<p>图片:</p><img src="../${order.imagePath}" alt="故障图片">`;
                }
                
                orderDetail.innerHTML = `
                    <p><strong>订单ID:</strong> ${order.id}</p>
                    <p><strong>设备类型:</strong> ${order.deviceType}</p>
                    <p><strong>故障描述:</strong> ${order.description}</p>
                    <p><strong>状态:</strong> ${statusText}</p>
                    <p><strong>创建时间:</strong> ${order.createdAt}</p>
                    <p><strong>更新时间:</strong> ${order.updatedAt}</p>
                    ${imageHtml}
                `;
                
                document.getElementById('orderDetailModal').style.display = 'block';
            } else {
                alert(data.message);
            }
        })
        .catch(error => {
            console.error('获取订单详情失败:', error);
            alert('获取订单详情失败，请稍后重试');
        });
    }
    
    // 创建订单功能
    if (document.getElementById('createOrderForm')) {
        document.getElementById('createOrderForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const deviceType = document.getElementById('deviceType').value;
            const description = document.getElementById('description').value;
            const image = document.getElementById('image').files[0];
            
            // 先创建订单
            fetch(`${API_BASE_URL}/repair/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': `Bearer ${getToken()}`
                },
                body: `deviceType=${encodeURIComponent(deviceType)}&description=${encodeURIComponent(description)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const orderId = data.order.id;
                    
                    // 如果有图片，上传图片
                    if (image) {
                        const formData = new FormData();
                        formData.append('orderId', orderId);
                        formData.append('file', image);
                        
                        fetch(`${API_BASE_URL}/repair/upload`, {
                            method: 'POST',
                            headers: {
                                'Authorization': `Bearer ${getToken()}`
                            },
                            body: formData
                        })
                        .then(response => response.json())
                        .then(uploadData => {
                            if (uploadData.code === 200) {
                                alert('订单创建成功');
                                loadOrders();
                                document.getElementById('createOrderModal').style.display = 'none';
                                document.getElementById('createOrderForm').reset();
                            } else {
                                alert('订单创建成功，但图片上传失败');
                                loadOrders();
                                document.getElementById('createOrderModal').style.display = 'none';
                                document.getElementById('createOrderForm').reset();
                            }
                        })
                        .catch(error => {
                            console.error('上传图片失败:', error);
                            alert('订单创建成功，但图片上传失败');
                            loadOrders();
                            document.getElementById('createOrderModal').style.display = 'none';
                            document.getElementById('createOrderForm').reset();
                        });
                    } else {
                        alert('订单创建成功');
                        loadOrders();
                        document.getElementById('createOrderModal').style.display = 'none';
                        document.getElementById('createOrderForm').reset();
                    }
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('创建订单失败:', error);
                alert('创建订单失败，请稍后重试');
            });
        });
    }
    
    // 退出登录
    if (document.getElementById('logoutBtn')) {
        document.getElementById('logoutBtn').addEventListener('click', function() {
            clearToken();
            localStorage.removeItem('user');
            window.location.href = 'index.html';
        });
    }
    
    // 打开创建订单模态框
    if (document.getElementById('createOrderBtn')) {
        document.getElementById('createOrderBtn').addEventListener('click', function() {
            document.getElementById('createOrderModal').style.display = 'block';
        });
    }
    
    // 关闭创建订单模态框
    if (document.getElementById('cancelCreateBtn')) {
        document.getElementById('cancelCreateBtn').addEventListener('click', function() {
            document.getElementById('createOrderModal').style.display = 'none';
            document.getElementById('createOrderForm').reset();
        });
    }
    
    // 关闭订单详情模态框
    if (document.getElementById('closeDetailBtn')) {
        document.getElementById('closeDetailBtn').addEventListener('click', function() {
            document.getElementById('orderDetailModal').style.display = 'none';
        });
    }
    
    // 宿舍绑定功能
    if (document.getElementById('dormBindForm')) {
        document.getElementById('dormBindForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const building = document.getElementById('building').value;
            const room = document.getElementById('room').value;
            
            fetch(`${API_BASE_URL}/dorm/bind`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': `Bearer ${getToken()}`
                },
                body: `building=${encodeURIComponent(building)}&room=${encodeURIComponent(room)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    alert('绑定成功');
                    window.location.href = 'home.html';
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('绑定失败:', error);
                alert('绑定失败，请稍后重试');
            });
        });
    }
    
    // 宿舍信息修改功能
    if (document.getElementById('dormUpdateForm')) {
        // 加载当前宿舍信息
        function loadDormInfo() {
            fetch(`${API_BASE_URL}/dorm/info`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${getToken()}`
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200 && data.binding) {
                    document.getElementById('building').value = data.binding.building;
                    document.getElementById('room').value = data.binding.room;
                } else {
                    alert('未绑定宿舍');
                }
            })
            .catch(error => {
                console.error('获取宿舍信息失败:', error);
                alert('获取宿舍信息失败，请稍后重试');
            });
        }
        
        // 页面加载时加载宿舍信息
        loadDormInfo();
        
        // 提交修改
        document.getElementById('dormUpdateForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const building = document.getElementById('building').value;
            const room = document.getElementById('room').value;
            
            fetch(`${API_BASE_URL}/dorm/update`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Authorization': `Bearer ${getToken()}`
                },
                body: `building=${encodeURIComponent(building)}&room=${encodeURIComponent(room)}`
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    alert('修改成功');
                    window.location.href = 'home.html';
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('修改失败:', error);
                alert('修改失败，请稍后重试');
            });
        });
    }
    
    // 点击模态框外部关闭
    window.onclick = function(event) {
        const createModal = document.getElementById('createOrderModal');
        const detailModal = document.getElementById('orderDetailModal');
        
        if (event.target == createModal) {
            createModal.style.display = 'none';
            document.getElementById('createOrderForm').reset();
        }
        
        if (event.target == detailModal) {
            detailModal.style.display = 'none';
        }
    };
    
    // 处理CORS问题
    handleCORS();
    
    // 页面加载时检查是否需要绑定宿舍
    function checkDormBinding() {
        // 获取用户信息
        const userStr = localStorage.getItem('user');
        if (!userStr) return;
        
        const user = JSON.parse(userStr);
        // 只有学生需要绑定宿舍
        if (user.role === 1) {
            fetch(`${API_BASE_URL}/dorm/info`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${getToken()}`
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200 && !data.binding) {
                    // 未绑定宿舍，跳转到绑定页面
                    window.location.href = 'dorm-bind.html';
                } else {
                    // 已绑定宿舍，加载订单列表
                    loadOrders();
                }
            })
            .catch(error => {
                console.error('检查宿舍绑定失败:', error);
                // 加载订单列表
                loadOrders();
            });
        } else {
            // 管理员不需要绑定宿舍，直接加载订单列表
            loadOrders();
        }
    }
    
    // 页面加载时检查是否需要绑定宿舍
    checkDormBinding();
}
