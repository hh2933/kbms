package com.shuxin.mapper.ruleengine;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
/**
 * 
 *分解住院
 */
public interface RuleFJZYMapper extends BaseMapper<CommonModel>{

	public Map<String, String> selectManyHospitalizationInfo();
	
}
