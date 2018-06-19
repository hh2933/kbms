package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 限定价格
 *
 */
public interface RuleXDYPJGMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectLimitProjectPrice(List<HospitalClaimDetail> hospitalClaimDetails);

}
