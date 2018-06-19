package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.LimitNumber;
import com.shuxin.model.vo.LimitNumberVo;

public interface ILimitNumberService extends IService<LimitNumber>{
	
	/**
	 * 查询数据列表
	 * @param pageInfo
	 */
	public void selectLimitNumberVoPage(PageInfo pageInfo);
	
	/**
	 * 查询该记录是否存在
	 * @param limitNumberVo
	 * @return
	 */
	public int selectExistLimitNumber(LimitNumberVo limitNumberVo);
	
	/**
	 * 编辑信息
	 * @param limitNumberVo
	 * @param user
	 */
	public void editLimitNumber(LimitNumberVo limitNumberVo,ShiroUser user);

	/**
	 * 查询需要编辑的信息
	 * @param id
	 * @return
	 */
	public LimitNumberVo selectEditLimitNumber(String id);
	
	/**
	 * 删除记录
	 * @param list
	 * @param user
	 */
	public void deleteLimitNumber(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectLimitNumberVoPage();
	
	/**
	 * 查询所有操作历史记录
	 * @return
	 */
	public List<Map<String, Object>> selectLimitNumberHistory();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importLimitNumber(List<Map<String, String>> list, String loginName);
}
