package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.DecomposeHospital;
import com.shuxin.model.vo.DecomposeHospitalVo;

public interface IDecomposeHospitalService extends IService<DecomposeHospital>{

	/**
	 * 查询数据列表
	 * @param pageInfo
	 */
	public void selectDecomposeHospitalVoPage(PageInfo pageInfo);
	
	/**
	 * 查询已经存在的记录
	 * @param decomposeHospitalVo
	 * @return
	 */
	public int selectExistDecomposeHospital(DecomposeHospitalVo decomposeHospitalVo);
	
	/**
	 * 编辑信息
	 * @param decomposeHospitalVo
	 */
	public void editDecomposeHospital(DecomposeHospitalVo decomposeHospitalVo, ShiroUser user);
	
	/**
	 * 查询需要编辑的记录
	 * @param id
	 * @return
	 */
	public DecomposeHospitalVo selectEditDecomposeHospital(String id);
	
	/**
	 * 删除信息
	 * @param list
	 * @param user
	 */
	public void deleteDecomposeHospital(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>>  selectDecomposeHospitalVoPage();
	
	/**
	 * 查询所有操作历史记录
	 * @return
	 */
	public List<Map<String, Object>>  selectDecomposeHospitalHistory();
	
	/**
	 * 保存导入数据
	 * @param map
	 */
	public void importDecomposeHospital(List<Map<String, String>> list, String loginName);
}
