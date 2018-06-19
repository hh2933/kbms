package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 
 *限儿童
 */
public interface RuleXETMapper extends BaseMapper<CommonModel>{

	public List<Map<String, Object>> selectLimitAgeInfo(List<HospitalClaimDetail> list);
	
}
