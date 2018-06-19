package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 * 月取药超量
 *
 */
public interface RuleYQYCLQYMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String,Object>> selectMonthOverPrescription(Map<String, Object> paramMap);

}
