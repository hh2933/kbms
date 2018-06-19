package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.StepDrug;
import com.shuxin.model.vo.StepDrugVo;

public interface IStepDrugService extends IService<StepDrug>{

	/**
	 * 查询列表信息
	 * @param pageInfo
	 */
	public void selectStepDrugVoPage(PageInfo pageInfo);
	
	/**
	 * 查询记录是否存在
	 * @param stepDrugVo
	 * @return
	 */
	public int selectExistStepDrug(StepDrugVo stepDrugVo);
	
	/**
	 * 编辑信息
	 * @param stepDrugVo
	 * @param user
	 */
	public void editStepDrug(StepDrugVo stepDrugVo,ShiroUser user);
	
	/**
	 * 根据ID查询信息
	 * @param id
	 * @return
	 */
	public StepDrugVo selectStepDrugVo(String id);
	
	/**
	 * 删除记录
	 * @param list
	 * @param user
	 */
	public void deleteStepDrug(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectStepDrugVoPage();
	
	/**
	 * 查询所有操作历史记录
	 * @return
	 */
	public List<Map<String, Object>> selectStepDrugHistory();
	
	/**
	 * 保存导入数据
	 * @param list
	 * @param loginName
	 */
	public void importStepDrug(List<Map<String, String>> list,String loginName);
}
