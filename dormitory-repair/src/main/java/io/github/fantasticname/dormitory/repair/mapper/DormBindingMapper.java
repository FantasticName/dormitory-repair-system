package io.github.fantasticname.dormitory.repair.mapper;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;

/**
 * 宿舍绑定Mapper接口
 * 
 * @author FantasticName
 */
public interface DormBindingMapper {
    /**
     * 根据用户id查询宿舍绑定的信息
     * 
     * @param userId 用户ID
     * @return 宿舍绑定信息
     */
    DormBinding findDormBindingInfoByUserId(Long userId);

    /**
     * 插入绑定信息
     * 
     * @param binding 宿舍绑定信息
     * @return 影响行数
     */
    int insertDormBindingInfo(DormBinding binding);

    /**
     * 更新绑定信息
     * 
     * @param binding 宿舍绑定信息
     * @return 影响行数
     */
    int updateDormBindingInfo(DormBinding binding);
}