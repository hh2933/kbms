package com.shuxin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.utils.BeanUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.SysUserMapper;
import com.shuxin.mapper.SysUserRoleMapper;
import com.shuxin.model.SysUser;
import com.shuxin.model.SysUserRole;
import com.shuxin.model.vo.SysUserVo;
import com.shuxin.service.ISysUserService;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @Override
    public List<SysUser> selectByLoginName(SysUserVo userVo) {
    	SysUser user = new SysUser();
        user.setUserName(userVo.getUserName());
        EntityWrapper<SysUser> wrapper = new EntityWrapper<SysUser>(user);
        if (null != userVo.getId()) {
            wrapper.where("id != {0}", userVo.getId());
        }
        return this.selectList(wrapper);
    }

    @Override
    public void insertByVo(SysUserVo userVo) {
        SysUser user = BeanUtils.copy(userVo, SysUser.class);
        user.setCreateTime(new Date());
        this.insert(user);
        
        String id = user.getId();
        String[] roles = userVo.getRoleIds().split(",");
       

        for (String string : roles) { 
        	SysUserRole userRole = new  SysUserRole();
        	
            userRole.setUserId(id);
            userRole.setRoleId(string);
            userRoleMapper.insert(userRole);
        }
    }
 
  @Override
    public SysUserVo selectVoById(String id) {
        return userMapper.selectUserVoById(id);
    }

    @Override
    public void updateByVo(SysUserVo userVo) {
    	SysUser user = BeanUtils.copy(userVo, SysUser.class);
        if (StringUtils.isBlank(user.getUserPassword())) {
            user.setUserPassword(null);
        }
        this.updateById(user);
        
        String id = userVo.getId();
        List<SysUserRole> userRoles = userRoleMapper.selectByUserId(id);
        if (userRoles != null && !userRoles.isEmpty()) {
            for (SysUserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }

        String[] roles = userVo.getRoleIds().split(",");
        for (String string : roles) {
        	SysUserRole userRole = new SysUserRole();
            userRole.setUserId(id);
            userRole.setRoleId(string);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void updatePwdByUserId(String userId, String md5Hex) {
    	SysUser user = new SysUser();
        user.setId(userId);
        user.setUserPassword(md5Hex);
        this.updateById(user);
    }

    @Override
    public void selectDataGrid(PageInfo pageInfo) {
        Page<SysUserVo> page = new Page<SysUserVo>(pageInfo.getNowpage(), pageInfo.getSize());
        String orderField = com.baomidou.mybatisplus.toolkit.StringUtils.camelToUnderline(pageInfo.getSort());
        page.setOrderByField(orderField);
        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
        List<SysUserVo> list = userMapper.selectUserVoPage(page, pageInfo.getCondition());
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
    }

    @Override
    public void deleteUserById(List<String> userIds) {
        this.deleteBatchIds(userIds);
        for (String id : userIds) {
        	List<SysUserRole> userRoles = userRoleMapper.selectByUserId(id);
            if (userRoles != null && !userRoles.isEmpty()) {
                for (SysUserRole userRole : userRoles) {
                    userRoleMapper.deleteById(userRole.getId());
                }
            }
		}
        
    }

	@Override
	public String selectUserIdByLoginName(String loginName) {
		return userMapper.selectUserIdByLoginName(loginName);
	}

	

}