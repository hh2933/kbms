package com.shuxin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.Role;

/**
 *
 * Role 表数据服务层接口
 *
 */
public interface IRoleService extends IService<Role> {

    void selectDataGrid(PageInfo pageInfo);

    Object selectTree();

    List<String> selectResourceIdListByRoleId(String id);

    void updateRoleResource(String id, String resourceIds);
    
    Map<String, Set<String>> selectResourceMapByUserId(String userId);

}