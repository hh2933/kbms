package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.LimitNumber;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.vo.LimitNumberVo;

public interface LimitNumberMapper extends BaseMapper<LimitNumber>{
	
	public List<Map<String, Object>>  selectLimitNumberVoPage(Pagination page, Map<String, Object> params);

	public int selectExistLimitNumber(LimitNumberVo limitNumberVo);
	
	public void addLimitNumberHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectLimitNumberVoPage();
	
	public List<Map<String, Object>> selectLimitNumberHistory();
	
	public void importLimitNumber(Map<String, Object> map);
	
	public List<Map<String, String>> selectProjectMappingInfo(List<HospitalClaimDetail> hospitalClaimDetails);
}
