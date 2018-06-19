package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 *出院带检治审核
 */
public interface RuleCYDJCZLSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectOutHospitalCheckInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
