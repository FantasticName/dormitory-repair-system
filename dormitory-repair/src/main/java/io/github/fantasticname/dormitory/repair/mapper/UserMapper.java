package io.github.fantasticname.dormitory.repair.mapper;



import io.github.fantasticname.dormitory.repair.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    // 根据账号查询用户
    User findUserByAccount(String account);

    // 插入用户（返回自增id）
    int insertUser(User user);

    // 根据id查询用户
    User findUserById(Long id);

    // 根据id更新密码
    int updatePassword(@Param("id") Long id, @Param("password") String password);


}
