package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 
 *限性别(违规/可疑)
 */
public interface RuleXDXBMapper  extends BaseMapper<CommonModel> {
	
	public List<Map<String, String>> selectLimitSexInfoSuspicious(List<HospitalClaimDetail> hospitalClaimDetails);

	public List<Map<String, String>> selectLimitSexInfoIllegal(List<HospitalClaimDetail> hospitalClaimDetails);
}
