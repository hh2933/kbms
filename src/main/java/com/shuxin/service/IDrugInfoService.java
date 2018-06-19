package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DrugInfo;
import com.shuxin.model.vo.DrugInfoVo;

public interface IDrugInfoService extends IService<DrugInfo>{
	
	/**
	 * 查询列表信息
	 * @param pageInfo
	 */
	public void selectDrugInfoVoPage(PageInfo pageInfo);
	
	/**
	 * 查询记录是否存在
	 * @param drugInfoVo
	 * @return
	 */
	public int selectExistDrugInfo(DrugInfoVo drugInfoVo);
	
	/**
	 * 编辑信息
	 * @param drugInfoVo
	 */
	public void editDrugInfo(DrugInfoVo drugInfoVo, ShiroUser user);
	
	/**
	 * 根据ID查询信息
	 * @param id
	 * @return
	 */
	public DrugInfoVo selectDrugInfoVo(String id);
	
	/**
	 * 删除信息
	 * @param list
	 * @param user
	 */
	public void deleteDrugInfo(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有信息
	 * @param ruleType
	 * @return
	 */
	public List<Map<String, Object>> selectDrugInfoVoPage(DrugInfoVo drugInfoVo);
	
	/**
	 * 查询所有历史操作信息
	 * @param ruleType
	 * @return
	 */
	public List<Map<String, Object>> selectDrugInfoHistory(DrugInfoVo drugInfoVo);
	
	/**
	 * 保存导入的数据
	 * @param map
	 */
	public void importDrugInfo(List<Map<String, String>> list,String loginName);
}
