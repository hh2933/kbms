package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.LimitTreatment;
import com.shuxin.model.vo.LimitTreatmentVo;

public interface ILimitTreatmentService extends IService<LimitTreatment>{

	/**
	 * 查询数据列表
	 * @param pageInfo
	 */
	public void selectLimitTreatmentVoPage(PageInfo pageInfo);
	
	/**
	 * 查询已经存在的记录
	 * @param limitTreatmentVo
	 * @return
	 */
	public int selectExistLimitTreatment(LimitTreatmentVo limitTreatmentVo);
	
	/**
	 * 编辑信息 
	 * @param limitTreatmentVo
	 * @param user
	 */
	public void editLimitTreatment(LimitTreatmentVo limitTreatmentVo,ShiroUser user);
	
	/**
	 * 查询需要编辑的信息
	 * @param id
	 * @return
	 */
	public LimitTreatmentVo selectEditLimitTreatment(String id);
	
	/**
	 * 删除记录
	 * @param list
	 * @param user
	 */
	public void deleteLimitTreatment(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectLimitTreatmentVoPage();
	
	/**
	 * 查询所有操作记录
	 * @return
	 */
	public List<Map<String, Object>> selectLimitTreatmentHistory();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importLimitTreatment(List<Map<String, String>> list, String loginName);
}
