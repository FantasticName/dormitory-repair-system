package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.User;
import io.github.fantasticname.dormitory.repair.mapper.UserMapper;
import io.github.fantasticname.dormitory.repair.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务类
 * 
 * @author FantasticName
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     * 
     * @param account 账号
     * @param password 密码
     * @param role 角色
     * @return 返回一个布尔，true表示注册成功，false表示失败
     */
    @Transactional
    public boolean register(String account, String password, Integer role) {
        // 检查账号是否已存在
        User exist = userMapper.findUserByAccount(account);
        if (exist != null) {
            logger.warn("账号已存在: {}", account);
            return false;
        }

        // 创建User实体对象
        User user = new User();
        user.setAccount(account);
        user.setPassword(PasswordUtil.encrypt(password));
        user.setRole(role);
        int rows = userMapper.insertUser(user);

        logger.info("用户注册成功: {}", account);
        return rows > 0;
    }

    /**
     * 登录
     * 
     * @param account 账号
     * @param password 密码
     * @return 用户对象
     */
    public User login(String account, String password) {
        User user = userMapper.findUserByAccount(account);

        // 如果账号存在且密码正确，返回用户对象
        if (user != null && PasswordUtil.matches(password, user.getPassword())) {
            logger.info("用户登录成功: {}", account);
            return user;
        }

        logger.warn("登录失败: {}", account);
        return null;
    }

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findUserById(userId);

        // 如果用户不存在或旧密码错误，返回false
        if (user == null || !PasswordUtil.matches(oldPassword, user.getPassword())) {
            logger.warn("修改密码失败: 用户不存在或密码错误");
            return false;
        }

        // 否则更新密码
        int rows = userMapper.updatePassword(userId, PasswordUtil.encrypt(newPassword));

        logger.info("用户密码修改成功: {}", userId);
        return rows > 0;
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(Long userId) {
        return userMapper.findUserById(userId);
    }
}