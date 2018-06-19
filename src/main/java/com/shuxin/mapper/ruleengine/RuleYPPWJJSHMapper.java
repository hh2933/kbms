package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 *药品配伍禁忌审核
 */
public interface RuleYPPWJJSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectCompatibilityTabooInfo();

}
