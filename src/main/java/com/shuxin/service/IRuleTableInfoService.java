package com.shuxin.service;

import java.util.List;
import java.util.Map;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.baomidou.mybatisplus.service.IService;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.RuleColumnInfo;
import com.shuxin.model.RuleTableInfo;

public interface IRuleTableInfoService  extends IService<RuleTableInfo>{

	public void selectRuleTable(PageInfo pageInfo) ;

	/**
	 * 根据菜单ID查询规则表信息
	 * @param menuId
	 * @return
	 */
	public List<Map<String, String>> selectRuleTableInfoForMenuId(String menuId);
	
	public void doRuleTable(RuleTableInfo table,List<RuleColumnInfo> cols);

	public Integer checkTableName(String tableName);
	
	public String createTable(RuleTableInfo ruleTableInfo,ShiroUser shiroUser) throws Exception;
	
	public String createTableUpdate(RuleTableInfo ruleTableInfo,ShiroUser shiroUser);
	
	
}
