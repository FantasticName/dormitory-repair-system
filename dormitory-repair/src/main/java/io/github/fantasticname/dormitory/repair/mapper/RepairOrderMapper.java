package io.github.fantasticname.dormitory.repair.mapper;

import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RepairOrderMapper {
    // 新增报修单
    int insertOrder(RepairOrder order);

    // 根据用户id查询所有报修单
    List<RepairOrder> findOrderByUserId(Long userId);


    // 查询所有报修单（可选按状态筛选，传null表示全部）
    List<RepairOrder> findAllByStatus(@Param("status") Integer status);

    // 根据id查询
    RepairOrder findOrderById(Long id);

    // 更新状态（会自动更新updated_at字段）
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    // 删除报修单
    int deleteOrderById(Long id);
}