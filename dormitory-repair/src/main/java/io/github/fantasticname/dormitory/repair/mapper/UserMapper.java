package io.github.fantasticname.dormitory.repair.mapper;

import io.github.fantasticname.dormitory.repair.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 * 
 * @author FantasticName
 */
public interface UserMapper {
    /**
     * 根据账号查询用户
     * 
     * @param account 账号
     * @return 用户对象
     */
    User findUserByAccount(String account);

    /**
     * 插入用户
     * 
     * @param user 用户对象
     * @return 影响行数
     */
    int insertUser(User user);

    /**
     * 根据id查询用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    User findUserById(Long id);

    /**
     * 根据id更新密码
     * 
     * @param id 用户ID
     * @param password 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

}
