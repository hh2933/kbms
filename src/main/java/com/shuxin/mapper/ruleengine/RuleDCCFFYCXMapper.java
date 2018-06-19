package com.shuxin.mapper.ruleengine;

import java.math.BigDecimal;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
/**
 * 
 *单次处方费用超限
 */
public interface RuleDCCFFYCXMapper extends BaseMapper<CommonModel>{

	
	public Map<String, BigDecimal> selectSingleFeeOverInfo();
	
}
