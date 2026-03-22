package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import io.github.fantasticname.dormitory.repair.mapper.RepairOrderMapper;
import io.github.fantasticname.dormitory.repair.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class RepairOrderService {

    // 创建报修单
    public boolean createOrder(Long userId, String deviceType, String description) {
        SqlSession session = null;
        try {
            // 创建session对象 and mapper代理 // 手动控制事务
            session = SqlSessionUtil.getSqlSession(false);
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);


            // 创建报修单实体对象
            RepairOrder order = new RepairOrder();
            order.setUserId(userId);
            order.setDeviceType(deviceType);
            order.setDescription(description);
            order.setStatus(0); // 待处理

            // 写入数据库
            int rows = mapper.insertOrder(order);
            // 提交事务
            session.commit();

            return rows > 0;

        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 查询学生的所有报修单
    public List<RepairOrder> getOrdersByUser(Long userId) {

        // 创建session对象 和 mapper代理 // 自动提交事务
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            // 返回查询结果的列表，泛型为RepairOrder报修单实体对象
            return mapper.findOrderByUserId(userId);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 按照状态查询所有报修单（管理员用）
    public List<RepairOrder> getAllOrders(Integer status) {

        // 创建session对象 和 mapper代理 // 自动提交事务
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            // 返回查询结果的列表，泛型为RepairOrder报修单实体对象
            return mapper.findAllByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查看单个报修单详情
    public RepairOrder getOrderById(Long orderId) {

        // 创建session对象 和 mapper代理 // 自动提交事务
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            // 返回查询结果的报修单实体对象
            return mapper.findOrderById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 修改状态
    public boolean updateStatus(Long orderId, Integer newStatus) {
        SqlSession session = null;
        try {
            // 创建session对象 and mapper代理 // 手动控制事务
            session = SqlSessionUtil.getSqlSession(false);
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            RepairOrder order = mapper.findOrderById(orderId);

            // 如果报修单不存在，就没法修改状态
            if (order == null) {
                return false;
            }

            // 如果报修单存在，那么修改状态
            int rows = mapper.updateStatus(orderId, newStatus);

            // 提交事务
            session.commit();

            return rows > 0;
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 删除报修单
    public boolean deleteOrder(Long orderId) {
        SqlSession session = null;
        try {
            // 创建session对象 and mapper代理 // 手动控制事务
            session = SqlSessionUtil.getSqlSession(false);
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);

            // ！！！这里我没写判断报修单是否存在，因为删除报修单时，会自动检查是否存在
            // ！！！然鹅可能导致代码风格不一致，后续再改吧。


            // 如果报修单存在，那么删除
            int rows = mapper.deleteOrderById(orderId);
            session.commit();

            // 如果删除成功，rows>0返回true，否则rows==0返回false
            return rows > 0;
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 取消报修单（只有状态为0待处理的可以取消）
    public boolean cancelOrder(Long orderId) {
        SqlSession session = null;
        try {
            session = SqlSessionUtil.getSqlSession(false);
            RepairOrderMapper mapper = session.getMapper(RepairOrderMapper.class);
            RepairOrder order = mapper.findOrderById(orderId);
            if (order == null || order.getStatus() != 0) {
                return false;
            }
            int rows = mapper.updateStatus(orderId, 3); // 3表示已取消
            session.commit();
            return rows > 0;
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}