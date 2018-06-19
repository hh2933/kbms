package com.shuxin.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.MinDrugType;
import com.shuxin.model.vo.MinDrugTypeVo;

public interface IMinDrugTypeService extends IService<MinDrugType>{

	/**
	 * 查询药品最小分类列表
	 * @param pageInfo
	 */
	public void selectMinDrugTypeVoPage(PageInfo pageInfo);
	
	/**
	 * 查询药品最小分类是否存在
	 * @param minDrugTypeVo
	 * @return
	 */
	public int selectExistMinDrugType(MinDrugTypeVo minDrugTypeVo);
	
	/**
	 * 编辑药品最小分类列表
	 * @param minDrugTypeVo
	 * @param user
	 */
	public void  editMinDrugType(MinDrugTypeVo minDrugTypeVo, ShiroUser user);
	
	/**
	 * 根据ID查询药品最小分类
	 * @param id
	 * @return
	 */
	public MinDrugTypeVo selectMinDrugTypeVo(String id);
	
	/**
	 * 删除药品最小分类
	 * @param list
	 * @param user
	 */
	public void deleteMinDrugType(List<String> list,ShiroUser user);
	
	/**
	 * 查询所有历史记录
	 * @return
	 */
	public List<Map<String, Object>> searchMinDrugTypeHistroy();
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<Map<String, Object>> selectMinDrugTypeVoPage();
	
	/**
	 * 保存导入信息
	 * @param list
	 * @param loginName
	 */
	public void importMinDrugType(List<Map<String, String>> list, String loginName);
}
