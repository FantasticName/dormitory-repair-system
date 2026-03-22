package io.github.fantasticname.dormitory.repair.controller;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;
import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import io.github.fantasticname.dormitory.repair.entity.User;
import io.github.fantasticname.dormitory.repair.service.DormBindingService;
import io.github.fantasticname.dormitory.repair.service.RepairOrderService;
import io.github.fantasticname.dormitory.repair.service.UserService;
import io.github.fantasticname.dormitory.repair.util.AccountValidatorWithRegularExpressionUtil;
import io.github.fantasticname.dormitory.repair.util.ConsoleUtil;

import java.util.List;

public class ConsoleController {

    private UserService userService = new UserService();
    private DormBindingService dormBindingService = new DormBindingService();
    private RepairOrderService repairOrderService = new RepairOrderService();
    private User currentUser;

    public void start() {
        while (true) {
            showMainMenu();
            int choice = ConsoleUtil.readInt("请选择操作（输入 1-3）：");
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("退出系统，再见！");
                    return;
                default:
                    System.out.println("无效选择，请重新输入。");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("===========================");
        System.out.println("宿舍报修管理系统");
        System.out.println("===========================");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 退出");
    }

    private void login() {
        System.out.println("===== 用户登录 =====");
        String account = ConsoleUtil.readLine("请输入账号：");
        String password = ConsoleUtil.readPassword("请输入密码：");
        User user = userService.login(account, password);
        if (user == null) {
            System.out.println("登录失败，账号或密码错误。");
            return;
        }
        currentUser = user;
        System.out.println("登录成功！角色：" + (user.getRole() == 1 ? "学生" : "管理员"));
        if (user.getRole() == 1) {
            studentMenu();
        } else {
            adminMenu();
        }
    }

    private void register() {
        final int MAX_ATTEMPTS_ACCOUNT_INPUT = 3;
        final int MAX_ATTEMPTS_PASSWORD_INPUT = 3;


        // 选择角色
        System.out.println("===== 用户注册 =====");
        int roleChoice = ConsoleUtil.readRole("请选择角色（1-学生，2-维修人员）：");

        // 提前声明账号和密码变量
        String account = null;
        String password = null;

        // 根据角色输入账号，验证账号格式是否正确
        boolean accountValid = false;
        if (roleChoice == 1) {
            // 细节，不要写魔法值！！ for (int i = 3 错误的写法
            for (int i = MAX_ATTEMPTS_ACCOUNT_INPUT; i > 0; i--) {
                account = ConsoleUtil.readLine("请输入学号（前缀3125或3225）：");
                if (AccountValidatorWithRegularExpressionUtil.isValidStudentAccount(account)) {
                    accountValid = true;
                    break;
                } else  {
                    System.out.println("学号格式错误，请重新输入。你还剩下" + i + "次机会。");
                }
            }

        } else if (roleChoice == 2) {
            for (int i = MAX_ATTEMPTS_ACCOUNT_INPUT; i > 0; i--) {
                account = ConsoleUtil.readLine("请输入工号（前缀0025）：");
                if (AccountValidatorWithRegularExpressionUtil.isValidAdminAccount(account)) {
                    accountValid = true;
                    break;
                } else {
                    System.out.println("工号格式错误，请重新输入。你还剩下" + i + "次机会。");
                }
            }
        } else {
            System.out.println("无效角色选择，注册取消。");
            return;
        }

        if (!accountValid) {
            System.out.println("账号格式错误，注册取消。");
            return;
        }



        // 输入密码和确认密码，验证前后两次输入是否一致
        boolean passwordInputTwiceConsistent = false;
        for (int attemp = MAX_ATTEMPTS_PASSWORD_INPUT; attemp > 0; attemp--) {
            password = ConsoleUtil.readPassword("请输入密码：");
            String confirm = ConsoleUtil.readPassword("请再次输入,确认密码：");
            if (!password.equals(confirm)) {
                System.out.println("两次输入的密码不一致，重新输入。你还剩下" + attemp + "次机会。");
            } else {
                passwordInputTwiceConsistent = true;
                break;
            }
        }
        if (!passwordInputTwiceConsistent) {
            System.out.println("密码输入失败，注册取消。");
            return;
        }


        // 注册用户
        boolean success = userService.register(account, password, roleChoice);
        if (success) {
            System.out.println("注册成功！请返回主界面登录。");
        } else {
            System.out.println("注册失败，账号可能已存在。");
        }
    }

    private void studentMenu() {
        // 检查是否已绑定宿舍，如果没有，则提示绑定
        DormBinding binding = dormBindingService.getByUserId(currentUser.getId());
        if (binding == null) {
            System.out.println("检测到你尚未绑定宿舍，请先绑定。");
            bindDorm();
        }
        while (true) {
            System.out.println("\n===== 学生菜单 =====");
            System.out.println("1. 绑定/修改宿舍");
            System.out.println("2. 创建报修单");
            System.out.println("3. 查看我的报修记录");
            System.out.println("4. 取消报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 退出");
            int choice = ConsoleUtil.readInt("请选择操作（输入 1-6）：");
            switch (choice) {
                case 1:
                    bindDorm();
                    break;
                case 2:
                    createRepairOrder();
                    break;
                case 3:
                    viewMyOrders();
                    break;
                case 4:
                    cancelOrder();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    System.out.println("退出登录。");
                    currentUser = null;
                    return;
                default:
                    System.out.println("无效选择。");
            }
        }
    }

    private void bindDorm() {
        String building = ConsoleUtil.readLine("请输入楼栋号：");
        String room = ConsoleUtil.readLine("请输入房间号：");
        DormBinding existing = dormBindingService.getByUserId(currentUser.getId());
        boolean success;
        if (existing == null) {
            success = dormBindingService.bindDorm(currentUser.getId(), building, room);
        } else {
            success = dormBindingService.updateBind(currentUser.getId(), building, room);
        }
        if (success) {
            System.out.println("宿舍绑定/修改成功！");
        } else {
            System.out.println("操作失败，请重试。");
        }
    }

    private void createRepairOrder() {
        String deviceType = ConsoleUtil.readLine("请输入设备类型（如：水龙头、灯管）：");
        String description = ConsoleUtil.readLine("请输入问题描述：");
        boolean success = repairOrderService.createOrder(currentUser.getId(), deviceType, description);
        if (success) {
            System.out.println("报修单创建成功！");
        } else {
            System.out.println("创建失败，请重试。");
        }
    }

    private void viewMyOrders() {
        List<RepairOrder> orders = repairOrderService.getOrdersByUser(currentUser.getId());
        if (orders == null || orders.isEmpty()) {
            System.out.println("暂无报修记录。");
            return;
        }
        for (RepairOrder order : orders) {
            System.out.printf("ID: %d, 设备: %s, 状态: %s, 创建时间: %s\n",
                    order.getId(), order.getDeviceType(), getStatusText(order.getStatus()), order.getCreatedAt());
        }
        String input = ConsoleUtil.readLine("输入报修单ID查看详情，或直接回车返回：");
        if (!input.isEmpty()) {
            try {
                Long id = Long.parseLong(input);
                RepairOrder detail = repairOrderService.getOrderById(id);
                if (detail == null || !detail.getUserId().equals(currentUser.getId())) {
                    System.out.println("无权查看或订单不存在。");
                } else {
                    System.out.println("=== 报修单详情 ===");
                    System.out.println("ID: " + detail.getId());
                    System.out.println("设备: " + detail.getDeviceType());
                    System.out.println("描述: " + detail.getDescription());
                    System.out.println("状态: " + getStatusText(detail.getStatus()));
                    System.out.println("创建时间: " + detail.getCreatedAt());
                    System.out.println("最后修改: " + detail.getUpdatedAt());
                }
            } catch (NumberFormatException e) {
                System.out.println("无效ID。");
            }
        }
    }

    private void cancelOrder() {
        List<RepairOrder> orders = repairOrderService.getOrdersByUser(currentUser.getId());
        if (orders == null || orders.isEmpty()) {
            System.out.println("没有可取消的报修单。");
            return;
        }
        System.out.println("可取消的报修单（状态为待处理）：");
        for (RepairOrder order : orders) {
            if (order.getStatus() == 0) {
                System.out.printf("ID: %d, 设备: %s\n", order.getId(), order.getDeviceType());
            }
        }
        Long orderId = Long.parseLong(ConsoleUtil.readLine("请输入要取消的报修单ID："));
        boolean success = repairOrderService.cancelOrder(orderId);
        if (success) {
            System.out.println("取消成功！");
        } else {
            System.out.println("取消失败，可能订单不存在或状态不允许取消。");
        }
    }

    private void changePassword() {
        String oldPwd = ConsoleUtil.readPassword("请输入旧密码：");
        String newPwd = ConsoleUtil.readPassword("请输入新密码：");
        String confirm = ConsoleUtil.readPassword("请确认新密码：");
        if (!newPwd.equals(confirm)) {
            System.out.println("两次输入不一致。");
            return;
        }
        boolean success = userService.changePassword(currentUser.getId(), oldPwd, newPwd);
        if (success) {
            System.out.println("密码修改成功，请重新登录。");
            currentUser = null;
        } else {
            System.out.println("密码修改失败，旧密码错误。");
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n===== 管理员菜单 =====");
            System.out.println("1. 查看所有报修单");
            System.out.println("2. 查看报修单详情");
            System.out.println("3. 更新报修单状态");
            System.out.println("4. 删除报修单");
            System.out.println("5. 修改密码");
            System.out.println("6. 退出");
            int choice = ConsoleUtil.readInt("请选择操作（输入 1-6）：");
            switch (choice) {
                case 1:
                    viewAllOrders();
                    break;
                case 2:
                    viewOrderDetail();
                    break;
                case 3:
                    updateOrderStatus();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    changePassword();
                    break;
                case 6:
                    System.out.println("退出登录。");
                    currentUser = null;
                    return;
                default:
                    System.out.println("无效选择。");
            }
        }
    }

    private void viewAllOrders() {
        System.out.println("筛选状态（0-待处理,1-处理中,2-已完成,3-已取消，输入其他数字查看全部）：");
        int status = ConsoleUtil.readInt("请输入状态值：");
        List<RepairOrder> orders = repairOrderService.getAllOrders(status >= 0 && status <= 3 ? status : null);
        if (orders == null || orders.isEmpty()) {
            System.out.println("没有报修单。");
            return;
        }
        for (RepairOrder order : orders) {
            System.out.printf("ID: %d, 学生ID: %d, 设备: %s, 状态: %s, 创建时间: %s\n",
                    order.getId(), order.getUserId(), order.getDeviceType(), getStatusText(order.getStatus()), order.getCreatedAt());
        }
    }

    private void viewOrderDetail() {
        Long orderId = Long.parseLong(ConsoleUtil.readLine("请输入报修单ID："));
        RepairOrder order = repairOrderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("订单不存在。");
            return;
        }
        System.out.println("=== 报修单详情 ===");
        System.out.println("ID: " + order.getId());
        System.out.println("学生ID: " + order.getUserId());
        System.out.println("设备: " + order.getDeviceType());
        System.out.println("描述: " + order.getDescription());
        System.out.println("状态: " + getStatusText(order.getStatus()));
        System.out.println("创建时间: " + order.getCreatedAt());
        System.out.println("最后修改: " + order.getUpdatedAt());
    }

    private void updateOrderStatus() {
        Long orderId = Long.parseLong(ConsoleUtil.readLine("请输入报修单ID："));
        int newStatus = ConsoleUtil.readInt("请输入新状态（0-待处理,1-处理中,2-已完成,3-已取消）：");
        boolean success = repairOrderService.updateStatus(orderId, newStatus);
        if (success) {
            System.out.println("状态更新成功！");
        } else {
            System.out.println("更新失败，订单不存在。");
        }
    }

    private void deleteOrder() {
        Long orderId = Long.parseLong(ConsoleUtil.readLine("请输入报修单ID："));
        boolean success = repairOrderService.deleteOrder(orderId);
        if (success) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败，订单不存在。");
        }
    }

    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已完成";
            case 3: return "已取消";
            default: return "未知";
        }
    }
}