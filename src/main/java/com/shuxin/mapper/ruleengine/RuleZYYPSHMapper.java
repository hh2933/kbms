package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 中药饮片审核
 */
public interface RuleZYYPSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>>  selectPiecesExamineInfo();
	
	public int selectCountByDrugCode(List<String> drugCode);

}
