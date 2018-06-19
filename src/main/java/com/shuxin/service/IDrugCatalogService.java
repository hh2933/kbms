package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DrugCatalog;
import com.shuxin.model.vo.DrugCatalogVo;
import com.shuxin.model.vo.ProjectCatalogVo;

public interface IDrugCatalogService extends IService<DrugCatalog>{

	/**
	 * 查询药品目录列表
	 * @param pageInfo
	 */
	public void selectDrugCatalogVoPage(PageInfo pageInfo);
	
	/**
	 * 查询药品目录是否存在
	 * @param drugCatalogVo
	 * @return
	 */
	public int  selectExistDrugCatalog(DrugCatalogVo drugCatalogVo);
	
	/**
	 * 编辑药品目录
	 * @param drugCatalogVo
	 * @param user
	 */
	public void editDrugCatalog(DrugCatalogVo drugCatalogVo,ShiroUser user);
	
	/**
	 * 根据ID查询药品目录
	 * @param id
	 * @return
	 */
	public DrugCatalogVo selectDrugCatalogVo(String id);
	
	/**
	 * 删除药品目录
	 * @param list
	 * @param user
	 */
	public void deleteDrugCatalog(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectDrugCatalogVoPage();
	
	/**
	 * 查询所有操作记录
	 * @return
	 */
	public List<Map<String, Object>> searchDrugCatalogHistroy();
	
	/**
	 * 保存导入数据
	 * @param list
	 * @param loginName
	 */
	public void importDrugCatalog(List<Map<String, String>> list,String loginName);

}
