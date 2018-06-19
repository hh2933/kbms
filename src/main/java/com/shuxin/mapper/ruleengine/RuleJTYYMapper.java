package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 阶梯用药
 *
 */
public interface RuleJTYYMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String,Object>> selectSecondLineDrugInfo(List<HospitalClaimDetail> list);

}
