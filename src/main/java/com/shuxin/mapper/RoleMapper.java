package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.Menu;
import com.shuxin.model.Role;

/**
 *
 * Role 表数据库控制层接口
 *
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectResourceIdListByRoleId(@Param("id") String id);

    List<Menu> selectResourceListByRoleIdList(@Param("list") List<String> list);

    List<Map<String, String>> selectResourceListByRoleId(@Param("id") String id);

}