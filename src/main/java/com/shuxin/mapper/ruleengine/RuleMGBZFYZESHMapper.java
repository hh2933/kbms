package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
/**
 * 
 *门规病种费用总额审核
 */
public interface RuleMGBZFYZESHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectSpecialDiseasesInfo(List<String> list);
	
	public String selectSpecialPatientYears(String patIdCard);

}
