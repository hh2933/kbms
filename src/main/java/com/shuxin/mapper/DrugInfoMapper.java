package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.DrugInfo;
import com.shuxin.model.vo.DrugInfoVo;

public interface DrugInfoMapper extends BaseMapper<DrugInfo>{
	
	
	public List<Map<String, Object>> selectDrugInfoVoPage(Pagination page, Map<String, Object> params);

	public int selectExistDrugInfo(DrugInfoVo drugInfoVo);
	
	public void addDrugInfoHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectDrugInfoVoPage(DrugInfoVo drugInfoVo);
	
	public List<Map<String, Object>> selectDrugInfoHistory(DrugInfoVo drugInfoVo);
	
	public void importDrugInfo(Map<String, Object> map);
	
	public List<String> selectExceptDrugInfo(Map<String, Object> paramMap);
}
