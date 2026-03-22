package io.github.fantasticname.dormitory.repair.mapper;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;
import org.apache.ibatis.annotations.Param;

public interface DormBindingMapper {
    // 根据用户id查询宿舍绑定的信息
    DormBinding findDormBindingInfoByUserId(Long userId);

    // 插入绑定信息
    int insertDormBindingInfo(DormBinding binding);

    // 更新绑定信息
    int updateDormBindingInfo(DormBinding binding);
}