package com.shuxin.mapper.ruleengine;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
/**
 * 重复超量用药
 *
 */
public interface RuleZFCLYYMapper extends BaseMapper<CommonModel>{
	
	public List<String> selectExistsDrugCode(List<String> list);

}
