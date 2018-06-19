package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.ProjectCatalog;
import com.shuxin.model.vo.ProjectCatalogVo;

public interface IProjectCatalogService extends IService<ProjectCatalog>{

	/**
	 * 查询项目目录列表
	 * @param pageInfo
	 */
	public void selectProjectCatalogVoPage(PageInfo pageInfo);
	
	/**
	 * 查询项目目录是否存在
	 * @param projectCatalogVo
	 * @return
	 */
	public int selectExistProjectCatalog(ProjectCatalogVo projectCatalogVo);
	
	/**
	 * 编辑项目目录
	 * @param projectCatalogVo
	 */
	public void editProjectCatalog(ProjectCatalogVo projectCatalogVo,ShiroUser user);
	
	/**
	 * 根据ID查询项目目录
	 * @param id
	 * @return
	 */
	public ProjectCatalogVo selectProjectCatalogVo(String id);
	
	/**
	 * 删除项目目录
	 * @param list
	 * @param user
	 */
	public void deleteProjectCatalog(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectProjectCatalogVoPage();
	
	/**
	 * 查询操作记录
	 * @return
	 */
	public List<Map<String, Object>> searchProjectCatalogHistroy();
	
	/**
	 * 保存导入数据
	 * @param list
	 * @param loginName
	 */
	public void importProjectCatalog(List<Map<String, String>> list,String loginName);
}
