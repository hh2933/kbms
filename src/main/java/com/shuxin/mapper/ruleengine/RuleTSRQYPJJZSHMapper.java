package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 
 *特殊人群用药禁忌审核
 */
public interface RuleTSRQYPJJZSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectSpecialPeopleInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
