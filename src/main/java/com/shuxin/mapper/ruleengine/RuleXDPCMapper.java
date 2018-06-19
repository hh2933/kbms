package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 限定频次
 *
 */
public interface RuleXDPCMapper extends BaseMapper<CommonModel> {
	
	public List<Map<String, Object>> selectLimitFrequencyIllegal(List<HospitalClaimDetail> hospitalClaimDetails);
	
	public List<Map<String, Object>> selectLimitFrequencySuspicion(List<HospitalClaimDetail> hospitalClaimDetails);
}
