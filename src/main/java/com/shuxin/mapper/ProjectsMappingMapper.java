package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.ProjectsMapping;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.vo.ProjectsMappingVo;

public interface ProjectsMappingMapper extends BaseMapper<ProjectsMapping>{

	public List<Map<String, Object>> selectProjectsMappingVoPage(Pagination page, Map<String, Object> params);
	
	public int selectExistProjectsMapping(ProjectsMappingVo projectsMappingVo);
	
	public void addProjectsMappingHistory(Map<String, Object> map);
	
	public List<Map<String, Object>> selectProjectsMappingVoPage();
	
	public List<Map<String, Object>> selectProjectsMappingHistory();
	
	public void importProjectsMapping(Map<String, Object> map);
	
	public List<String> selectBProjectMappingInfo(List<HospitalClaimDetail> list);
	
	public List<Map<String,String>> selectBProjectMappingInfoInHos();
	
	public List<String> selectBProjectMappingHistoryInfo(Map<String, String> paramMap);
}
