package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.StepDrug;
import com.shuxin.model.vo.StepDrugVo;

public interface StepDrugMapper  extends BaseMapper<StepDrug>{
	
	public List<Map<String, Object>> selectStepDrugVoPage(Pagination page, Map<String, Object> params);

	public int selectExistStepDrug(StepDrugVo stepDrugVo);
	
	public void addStepDrugHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectStepDrugVoPage();
	
	public List<Map<String, Object>> selectStepDrugHistory();
	
	public void importStepDrug(Map<String, Object> map);
}
