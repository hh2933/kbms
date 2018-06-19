package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DrugMapping;
import com.shuxin.model.vo.DrugCatalogVo;
import com.shuxin.model.vo.DrugMappingVo;

public interface IDrugMappingService extends IService<DrugMapping>{

	/**
	 * 查询药品标准映射编码列表
	 * @param pageInfo
	 */
	public void selectDrugMappingVoPage(PageInfo pageInfo);
	
	/**
	 * 查询药品标准映射编码是否存在
	 * @param drugMappingVo
	 * @return
	 */
	public int  selectExistDrugMapping(DrugMappingVo drugMappingVo);
	
	/**
	 * 编辑药品标准映射编码
	 * @param drugMappingVo
	 * @param user
	 */
	public void editDrugMapping(DrugMappingVo drugMappingVo, ShiroUser user);
	
	/**
	 * 根据ID查询药品标准映射编码
	 * @param id
	 * @return
	 */
	public DrugMappingVo selectDrugMappingVo(String id);
	
	/**
	 * 删除药品标准映射编码
	 * @param list
	 * @param user
	 */
	public void deleteDrugMapping(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectDrugMappingVoPage();
	
	/**
	 * 查询所有操作记录
	 * @return
	 */
	public List<Map<String, Object>> searchDrugMappingHistroy();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importDrugMapping(List<Map<String, String>> list, String loginName);
}
