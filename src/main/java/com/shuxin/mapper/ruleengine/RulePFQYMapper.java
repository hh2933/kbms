package com.shuxin.mapper.ruleengine;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 *频繁取药
 */
public interface RulePFQYMapper extends BaseMapper<CommonModel>{
	
	public List<String> selectFrequentlyExceptDrugs(List<HospitalClaimDetail> list);

}
