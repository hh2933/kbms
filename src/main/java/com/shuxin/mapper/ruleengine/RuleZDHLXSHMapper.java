package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 
 *疾病诊断合理性审核
 */
public interface RuleZDHLXSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String,String>> selectDiagnosisIllegalInfo(List<String> list);

}
