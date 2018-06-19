package com.shuxin.mapper.ruleengine;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.ViolationDetail;

public interface ViolationDetailMapper  extends BaseMapper<CommonModel>{
	
	public void addViolationDetail(ViolationDetail violationDetail);
	
	public void deleteViolationDetail(Map<String, Object> paramMap);
	
	public void deleteViolationDetailById(String id);

	public void addViolationResult(Map<String, String> paramMap);
}
