package com.shuxin.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.RoleMenu;

/**
 *
 * RoleResource 表数据库控制层接口
 *
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    Long selectIdListByRoleId(@Param("id") Long id);

}