
package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.MinDrugType;
import com.shuxin.model.vo.MinDrugTypeVo;

public interface MinDrugTypeMapper  extends BaseMapper<MinDrugType>{

	public List<Map<String, Object>>  selectMinDrugTypeVoPage(Pagination page, Map<String, Object> params);
	
	public int selectExistMinDrugType(MinDrugTypeVo minDrugTypeVo);
	
	public void addMinDrugTypeHistory(Map<String, Object> map);
	
	
	public List<Map<String, Object>> searchMinDrugTypeHistroy();
	
	public List<Map<String, Object>> selectMinDrugTypeVoPage();
	
	public void importMinDrugType(Map<String, Object> map);
	
	public List<Map<String, String>> selectMinDrugTypeForDrugCode(List<String> list);
}
