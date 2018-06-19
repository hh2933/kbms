package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.LimitTreatment;
import com.shuxin.model.vo.LimitTreatmentVo;

public interface LimitTreatmentMapper extends BaseMapper<LimitTreatment>{

	public List<Map<String, Object>>  selectLimitTreatmentVoPage(Pagination page, Map<String, Object> params);
	
	public int selectExistLimitTreatment(LimitTreatmentVo limitTreatmentVo);
	
	public void addLimitTreatmentHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectLimitTreatmentVoPage();
	
	public List<Map<String, Object>> selectLimitTreatmentHistory();
	
	public void importLimitTreatment(Map<String, Object> map);
}
