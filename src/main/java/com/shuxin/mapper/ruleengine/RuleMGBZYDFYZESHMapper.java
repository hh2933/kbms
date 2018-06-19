package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 
 *门规综合病种费用总额审核
 */
public interface RuleMGBZYDFYZESHMapper extends BaseMapper<CommonModel>{
	
	public Map<String, String> selectSpecialMultipleDiseasesInfo(int aiagnosisCodeCount);
	public List<String> selectDiagnosisCodeByPatIdCard(String patIdCard);

}
