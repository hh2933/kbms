package com.shuxin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.result.Tree;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.mapper.MenuMapper;
import com.shuxin.mapper.RoleMapper;
import com.shuxin.mapper.RoleUserMapper;
import com.shuxin.model.Menu;
import com.shuxin.service.IMenuService;

/**
 *
 * Resource 表数据服务层接口实现类
 *
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    private static final int RESOURCE_MENU = 0; // 菜单

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public List<Menu> selectAll() {
        EntityWrapper<Menu> wrapper = new EntityWrapper<Menu>();
        wrapper.orderBy("seq");
        return menuMapper.selectList(wrapper);
    }
    
    public List<Menu> selectByType(Integer type) {
        EntityWrapper<Menu> wrapper = new EntityWrapper<Menu>();
        Menu menu = new Menu();
        wrapper.setEntity(menu);
        //因为他是通过resource_type来判断是否为权限菜单，这里我们的表没有这个判断  所以注释掉
        //wrapper.addFilter("resource_type = {0}", type);
        wrapper.orderBy("seq");
        wrapper.eq("status", "1");
        return menuMapper.selectList(wrapper);
    }
    
    @Override
    public List<Tree> selectAllMenu() {
        List<Tree> trees = new ArrayList<Tree>();
        // 查询所有菜单
        List<Menu> menus = this.selectByType(RESOURCE_MENU);
        if (menus == null) {
            return trees;
        }
        for (Menu menu : menus) {
            Tree tree = new Tree();
            tree.setId(menu.getId());
            tree.setPid(menu.getPid());
            tree.setText(menu.getName());
            tree.setIconCls(menu.getIcon());
            tree.setAttributes(menu.getUrl());
            tree.setHaschildren(menu.getIshaschildren());
            //这里数据表中没有这一列
            tree.setState("open");
            trees.add(tree);
        }
        return trees;
    }
    
    @Override
    public List<Tree> selectAllTree() {
        // 获取所有的资源 tree形式，展示
        List<Tree> trees = new ArrayList<Tree>();
        EntityWrapper<Menu> wrapper = new EntityWrapper<Menu>();
        wrapper.where("status={0}", "1");
        List<Menu> menus = this.selectList(wrapper);
        if (menus == null) {
            return trees;
        }
        for (Menu menu : menus) {
            Tree tree = new Tree();
            tree.setId(menu.getId());
            tree.setPid(menu.getPid());
            tree.setText(menu.getName());
            tree.setIconCls(menu.getIcon());
            tree.setAttributes(menu.getUrl());
            tree.setState("open");
            trees.add(tree);
        }
        return trees;
    }
    
    @Override
    public List<Tree> selectTree(ShiroUser shiroUser) {
        List<Tree> trees = new ArrayList<Tree>();
        // shiro中缓存的用户角色
        Set<String> roles = shiroUser.getRoles();
        if (roles == null) {
            return trees;
        }
        // 如果有超级管理员权限
        if (roles.contains("admin")) {
            List<Menu> menuList = this.selectByType(RESOURCE_MENU);
            if (menuList == null) {
                return trees;
            }
            for (Menu menu : menuList) {
                Tree tree = new Tree();
                tree.setId(menu.getId());
                tree.setPid(menu.getPid());
                tree.setText(menu.getName());
                tree.setIconCls(menu.getIcon());
                tree.setAttributes(menu.getUrl());
                tree.setHaschildren(menu.getIshaschildren());
                tree.setOpenMode(menu.getOpenMode());
                tree.setState("open");
                trees.add(tree);
            }
            return trees;
        }
        // 普通用户
        //根据用户ID获取用户的角色（一个用户可以有多个角色）
        List<String> roleIdList = roleUserMapper.selectRoleIdListByUserId(shiroUser.getId());
        if (roleIdList == null) {
            return trees;
        }
        List<Menu> menuLists = roleMapper.selectResourceListByRoleIdList(roleIdList);
        if (menuLists == null) {
            return trees;
        }
        for (Menu menu : menuLists) {
            Tree tree = new Tree();
            tree.setId(menu.getId());
            tree.setPid(menu.getPid());
            tree.setText(menu.getName());
            tree.setIconCls(menu.getIcon());
            tree.setAttributes(menu.getUrl());
            tree.setHaschildren(menu.getIshaschildren());
            tree.setOpenMode(menu.getOpenMode());
            tree.setState("open");
            trees.add(tree);
        }
        return trees;
    }

	
		 @Override
		    public void selectTreeByCondition(PageInfo pageInfo,int id) {
			  Page<Menu> page = new Page<Menu>(pageInfo.getNowpage(), pageInfo.getSize());
		        
		        EntityWrapper<Menu> wrapper = new EntityWrapper<Menu>();
		        wrapper.orderBy(pageInfo.getSort(), pageInfo.getOrder().equalsIgnoreCase("ASC"));
		        wrapper.eq("p_id", id);
		        selectPage(page, wrapper);
		        
		        pageInfo.setRows(page.getRecords());
		        pageInfo.setTotal(page.getTotal());
		    }

		@Override
		public Integer checkMenuName(String MenuName) {
			// TODO Auto-generated method stub
			return menuMapper.checkMenuName(MenuName);
		}
		
	

    
}