package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;

/**
 * 中药饮片超量
 */
public interface RuleZYYPCLMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectPiecesExcessInfo();

}
