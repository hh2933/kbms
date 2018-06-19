package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 
 *限定数量
 *
 */
public interface RuleXSLFZBMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectLimitProjectSortInfo();

}
