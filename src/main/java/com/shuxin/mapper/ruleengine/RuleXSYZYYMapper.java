package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 限适应症用药
 *
 */
public interface RuleXSYZYYMapper extends BaseMapper<CommonModel>{

	public List<Map<String, String>> selectIndicationCode_illegal(List<HospitalClaimDetail> hospitalClaimDetails);
	
	public List<Map<String, String>> selectIndicationCode_suspicion(List<HospitalClaimDetail> hospitalClaimDetails);
}
