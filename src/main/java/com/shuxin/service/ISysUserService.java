package com.shuxin.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.SysUser;
import com.shuxin.model.User;
import com.shuxin.model.vo.SysUserVo;
import com.shuxin.model.vo.UserVo;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> selectByLoginName(SysUserVo userVo);

    void insertByVo(SysUserVo userVo);

    SysUserVo selectVoById(String id);

    void updateByVo(SysUserVo userVo);

    void updatePwdByUserId(String userId, String md5Hex);

    void selectDataGrid(PageInfo pageInfo);

    void deleteUserById(List<String> userIds);
    
    /**
     * 根据登录名查询
     * @param loginName
     * @return
     */
    public String selectUserIdByLoginName(String loginName);
}