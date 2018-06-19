package com.shuxin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.mapper.RoleUserMapper;

import com.shuxin.model.RoleUser;

import com.shuxin.service.IRoleUserService;


/**
 *
 * UserRole 表数据服务层接口实现类
 *
 */
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements IRoleUserService {

}