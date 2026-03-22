package io.github.fantasticname.dormitory.repair.service;

import io.github.fantasticname.dormitory.repair.entity.DormBinding;
import io.github.fantasticname.dormitory.repair.mapper.DormBindingMapper;
import io.github.fantasticname.dormitory.repair.util.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

public class DormBindingService {

    /** 绑定宿舍 */
    public boolean bindDorm(Long userId, String building, String room) {
        SqlSession session = null;
        try {
            // 创建sqlsession对象和mapper代理 （手动提交事务）
            session = SqlSessionUtil.getSqlSession(false);
            DormBindingMapper mapper = session.getMapper(DormBindingMapper.class);
            DormBinding exist = mapper.findDormBindingInfoByUserId(userId);

            // 检查用户是否已经绑定过宿舍
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
            int rows = mapper.insertDormBindingInfo(binding);

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



    /** 获取用户宿舍绑定信息 */
    public DormBinding getByUserId(Long userId) {
        try (SqlSession session = SqlSessionUtil.getSqlSession()) {
            // 创建mapper代理对象
            DormBindingMapper mapper = session.getMapper(DormBindingMapper.class);
            // 查信息，返回实体对象
            return mapper.findDormBindingInfoByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /** 修改用户宿舍绑定信息 */
    public boolean updateBind(Long userId, String building, String room) {
        SqlSession session = null;
        try {
            // 创建mapper代理对象   // 手动事务
            session = SqlSessionUtil.getSqlSession(false);
            DormBindingMapper mapper = session.getMapper(DormBindingMapper.class);
            DormBinding binding = mapper.findDormBindingInfoByUserId(userId);

            // 如果没绑定过宿舍，就不能修改绑定信息
            if (binding == null) {
                return false;
            }

            // 更新绑定信息（修改实体对象的属性）
            binding.setBuilding(building);
            binding.setRoom(room);

            // 更新数据库
            int rows = mapper.updateDormBindingInfo(binding);

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
}