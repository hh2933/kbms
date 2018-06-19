package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 * 出院带药种类审核
 *
 */
public interface RuleCYDYZLSHMapper extends BaseMapper<CommonModel>{
	
	public Map<String, String> selectOutHospitalDrugType();

}
