package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.ProjectsMapping;
import com.shuxin.model.vo.ProjectsMappingVo;

public interface IProjectsMappingService extends IService<ProjectsMapping>{

	/**
	 * 查询项目目录列表
	 * @param pageInfo
	 */
	public void selectProjectsMappingVoPage(PageInfo pageInfo);
	
	/**
	 * 查询记录是否存在
	 * @param projectsMappingVo
	 * @return
	 */
	public int selectExistProjectsMapping(ProjectsMappingVo projectsMappingVo);
	
	/**
	 * 编辑信息
	 * @param projectsMappingVo
	 * @param user
	 */
	public void editProjectsMapping(ProjectsMappingVo projectsMappingVo, ShiroUser user);
	
	/**
	 * 查询需要编辑的记录
	 * @param id
	 * @return
	 */
	public ProjectsMappingVo selectEditProjectsMapping(String id);
	
	/**
	 * 删除信息
	 * @param list
	 * @param user
	 */
	public void deleteProjectsMapping(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有信息
	 * @return
	 */
	public List<Map<String, Object>> selectProjectsMappingVoPage();
	
	/**
	 * 查询所有操作记录
	 * @return
	 */
	public List<Map<String, Object>> selectProjectsMappingHistory();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importProjectsMapping(List<Map<String, String>> list, String loginName);
}
