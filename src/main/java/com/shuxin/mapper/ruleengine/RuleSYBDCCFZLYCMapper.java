package com.shuxin.mapper.ruleengine;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
/**
 * 
 * 省医保单次处方种类异常
 *
 */
public interface RuleSYBDCCFZLYCMapper extends BaseMapper<CommonModel>{
	
	public Map<String,String> selectProvincialMedicalInsuranceInfo();

}
