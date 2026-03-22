package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.User;
import io.github.fantasticname.dormitory.repair.mapper.UserMapper;
import io.github.fantasticname.dormitory.repair.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class UserService {

    /**
     *注册
     *@return 返回一个布尔，true表示注册成功，false表示失败
     */
    public boolean register(String account, String password, Integer role) {
        SqlSession session = null;
        try {
            // 创建mapper代理对象   // 手动事务
            session = SqlSessionUtil.getSqlSession(false);
            UserMapper mapper = session.getMapper(UserMapper.class);

            // 检查账号是否已存在
            User exist = mapper.findUserByAccount(account);
            if (exist != null) {
                return false;
            }


            // 创建User实体对象
            User user = new User();
            user.setAccount(account);
            user.setPassword(password);
            user.setRole(role);
            int rows = mapper.insertUser(user);

            // 提交事务（原子性操作：检查账号存在与否，以及insert用户到数据库用户表）
            session.commit();
            return rows > 0;
        } catch (Exception e) {
            if (session != null) {
                session.rollback(); // 回滚
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 登录
    public User login(String account, String password) {
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {

            // 创建mapper代理对象
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.findUserByAccount(account);

            // 如果账号存在且密码正确，返回用户对象
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }

            // 否则返回空引用
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    // 修改密码
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        SqlSession session = null;
        try {

            // 创建session对象 和 mapper代理对象
            session = SqlSessionUtil.getSqlSession(false);
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.findUserById(userId);

            // 如果用户不存在或旧密码错误，返回false
            if (user == null || !user.getPassword().equals(oldPassword)) {
                return false;
            }

            // 否则更新密码
            int rows = mapper.updatePassword(userId, newPassword);

            // 提交事务
            session.commit();

            // 更新成功，返回true
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

    // 根据ID获取用户信息
    public User getUserById(Long userId) {
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            return mapper.findUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}