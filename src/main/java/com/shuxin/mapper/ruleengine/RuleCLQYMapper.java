package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 超量取药
 */
public interface RuleCLQYMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String,String>> selectMaxPrescriptionNum(Map<String, Object> paramMap);

	public List<Map<String,Object>> selectOverPrescriptionIllegal(Map<String, Object> paramMap);
	
	public List<Map<String,Object>> selectOverPrescriptionSuspicious(Map<String, Object> paramMap);
}
