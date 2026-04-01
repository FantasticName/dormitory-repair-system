package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;
import io.github.fantasticname.dormitory.repair.mapper.DormBindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 宿舍绑定服务类
 * 
 * @author FantasticName
 */
@Service
public class DormBindingService {

    @Autowired
    private DormBindingMapper dormBindingMapper;

    /**
     * 绑定宿舍
     * 
     * @param userId 用户ID
     * @param building 楼栋
     * @param room 房间
     * @return 是否成功
     */
    @Transactional
    public boolean bindDorm(Long userId, String building, String room) {
        // 检查用户是否已经绑定过宿舍
        DormBinding exist = dormBindingMapper.findDormBindingInfoByUserId(userId);
        if (exist != null) {
            // 已经绑定过
            return false;
        }

        // 创建实体对象。用于写入数据库
        DormBinding binding = new DormBinding();
        binding.setUserId(userId);
        binding.setBuilding(building);
        binding.setRoom(room);

        // 写进数据库
        int rows = dormBindingMapper.insertDormBindingInfo(binding);
        return rows > 0;
    }

    /**
     * 获取用户宿舍绑定信息
     * 
     * @param userId 用户ID
     * @return 宿舍绑定信息
     */
    public DormBinding getByUserId(Long userId) {
        // mapper代理会封装好entity对象，配置在映射文件.xml中
        return dormBindingMapper.findDormBindingInfoByUserId(userId);
    }



    
    /**
     * 修改用户宿舍绑定信息
     * 
     * @param userId 用户ID
     * @param building 楼栋
     * @param room 房间
     * @return 是否成功
     */
    @Transactional
    public boolean updateBind(Long userId, String building, String room) {
        // 检查用户是否已经绑定过宿舍
        DormBinding binding = dormBindingMapper.findDormBindingInfoByUserId(userId);
        if (binding == null) {
            return false;
        }

        // 更新绑定信息（修改实体对象的属性）
        binding.setBuilding(building);
        binding.setRoom(room);

        // 更新数据库
        int rows = dormBindingMapper.updateDormBindingInfo(binding);
        return rows > 0;
    }
}