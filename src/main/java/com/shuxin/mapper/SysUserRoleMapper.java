package com.shuxin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.SysUserRole;

/**
 *
 * UserRole 表数据库控制层接口
 *
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysUserRole> selectByUserId(@Param("userId") String userId);

    List<String> selectRoleIdListByUserId(@Param("userId") String userId);

}