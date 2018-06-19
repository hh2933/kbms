package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.RuleColumnInfo;
import com.shuxin.model.RuleTableInfo;

public interface RuleTableInfoMapper extends BaseMapper<RuleTableInfo> {

	List<RuleTableInfo> selectRuleTableInfoDataGraid(Page<RuleTableInfo> page, Map<String, Object> condition);

	void doRuleTable(String tableName, String inner);

	void doOperationTable(String tableName, String inner);

	public List<Map<String, String>> selectRuleTableInfoForMenuId(String menuId);

	void createRuleTable(String tableName, String inner);

	Integer checkTableName(String tableName);

	void daoruData(Map<String, Object> mapCondition);
	
	List<RuleTableInfo> selectRuleTableInfoAll(String applyType);

}
