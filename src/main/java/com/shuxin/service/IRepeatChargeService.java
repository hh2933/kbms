package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.RepeatCharge;
import com.shuxin.model.vo.RepeatChargeVo;

public interface IRepeatChargeService extends IService<RepeatCharge>{
	
	/**
	 * 查询数据列表
	 * @param pageInfo
	 */
	public void selectRepeatChargeVoPage(PageInfo pageInfo);
	
	/**
	 * 查询已经记录是否存在
	 * @param repeatChargeVo
	 * @return
	 */
	public int selectExistRepeatCharge(RepeatChargeVo repeatChargeVo);
	
	/**
	 * 编辑信息
	 * @param repeatChargeVo
	 */
	public void editRepeatCharge(RepeatChargeVo repeatChargeVo, ShiroUser user);

	/**
	 * 查询需要编辑的记录
	 * @param id
	 * @return
	 */
	public RepeatChargeVo selectEditRepeatCharge(String id);
	
	/**
	 * 删除信息
	 * @param list
	 * @param user
	 */
	public void deleteRepeatCharge(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>>  selectRepeatChargeVoPage();
	
	/**
	 * 查询所有历史操作
	 * @return
	 */
	public List<Map<String, Object>>  selectRepeatChargeHistory();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importRepeatCharge(List<Map<String, String>> list, String loginName);
}
