package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.shuxin.model.CommonModel;

public interface KnowledgeBaseMapper extends BaseMapper<CommonModel> {

	public List<Map<String, Object>> selectKnowledgeBaseInfo(Pagination page, Map<String, Object> params);
	
	public int selectExistKnowledgeBase(Map<String, Object> params);
	
	public void addKnowledgeBase(Map<String, Object> params);
	
	public void updateKnowledgeBase(Map<String, Object> params);
	
	public void delKnowledgeBase(Map<String, Object> params);

	public void daoruData(Map<String, Object> mapCondition);
	
	public void addKnowledgeBaseOpt(List<Map<String, Object>> list);
	
	public List<String> selectColumnOfKnowledgeBase(String tableName);
	
	public void addKnowledgeBaseHistory(Map<String, Object> params);
	
	public List<Map<String, String>> selectKnowledgeBaseInfo(Map<String, Object> params);
}
