package io.github.fantasticname.dormitory.repair.mapper;

import io.github.fantasticname.dormitory.repair.entity.RepairOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 维修订单Mapper接口
 * 
 * @author FantasticName
 */
public interface RepairOrderMapper {
    /**
     * 新增报修单
     * 
     * @param order 维修订单对象
     * @return 影响行数
     */
    int insertOrder(RepairOrder order);

    /**
     * 根据用户id查询所有报修单
     * 
     * @param userId 用户ID
     * @return 维修订单列表
     */
    List<RepairOrder> findOrderByUserId(Long userId);

    /**
     * 查询所有报修单（可选按状态筛选，传null表示全部）
     * 
     * @param status 订单状态：0待处理 1处理中 2已完成 3已取消
     * @return 维修订单列表
     */
    List<RepairOrder> findAllByStatus(@Param("status") Integer status);

    /**
     * 根据id查询维修订单
     * 
     * @param id 订单ID
     * @return 维修订单对象
     */
    RepairOrder findOrderById(Long id);

    /**
     * 更新订单状态（会自动更新updated_at字段）
     * 
     * @param id 订单ID
     * @param status 新状态：0待处理 1处理中 2已完成 3已取消
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除报修单
     * 
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteOrderById(Long id);
    
    /**
     * 更新订单图片路径
     * 
     * @param id 订单ID
     * @param imagePath 图片路径
     * @return 影响行数
     */
    int updateImagePath(@Param("id") Long id, @Param("imagePath") String imagePath);
}