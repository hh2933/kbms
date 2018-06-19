package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 提前取药（违规/可疑）
 *
 */
public interface RuleTQQYMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, Object>> selectAdvanceMedicineIllegal(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> selectAdvanceMedicineSuspicious(Map<String, Object> paramMap);

}
