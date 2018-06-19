package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 限定医院级别
 * @author shuxin
 *
 */
public interface RuleXDYYLXJBMapper extends BaseMapper<CommonModel> {
	
	public List<Map<String, String>> selectIndicationCode(List<HospitalClaimDetail> hospitalClaimDetails);

}
