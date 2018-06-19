package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 限定单次项目数量
 *
 */
public interface RuleXDDCXMSLMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectSingleProjectLimitInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
