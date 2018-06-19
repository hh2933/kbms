package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 * 出院带药超量审核
 *
 */
public interface RuleCYDYCLSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectOutHospitalDrugInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
