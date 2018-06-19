package com.shuxin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.Dictionary;
import com.shuxin.model.RuleColumnInfo;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.vo.RuleTableInfoVo;
import com.shuxin.service.IDictionaryService;
import com.shuxin.service.IMenuService;
import com.shuxin.service.IRuleColumnInfoService;
import com.shuxin.service.IRuleTableInfoService;

@Controller
@RequestMapping("/rule")
public class RuleTableInfoController extends BaseController  {

	 @Autowired
	 private IRuleTableInfoService ruleTableInfoService;
	 
	 @Autowired
	 private IMenuService menuService;

	 @Autowired
	 private IRuleColumnInfoService ruleColumnInfoService;
	 
	 @Autowired
	 private IDictionaryService dictionaryService;
	
	@RequestMapping("/manager")
	public String  toRuleTableInfo(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
		return "ruleManager/ruleTable";
	}
	
	@RequestMapping("/showInsert")
	public String ruleInsertPage(Model m){
		List<Dictionary> dicList=  dictionaryService.groupByDictType();
		m.addAttribute("dicList",  JsonUtils.toJson(dicList));
		return "ruleManager/ruleInsert";
	}
	@RequestMapping("/showUpdate")
	public String ruleUpdatePage(Model m, String id){
		//根据id查询到table放入到model中
		List<Dictionary> dicList=  dictionaryService.groupByDictType();
		m.addAttribute("dicList", JsonUtils.toJson(dicList));
		RuleTableInfo  r= ruleTableInfoService.selectById(id);
		m.addAttribute("table", r);
		return "ruleManager/ruleUpdate";
	}
	@RequestMapping("/loadUpdateGridData")
	@ResponseBody
	public Object loadUpdateGridData(String id){
		EntityWrapper<RuleColumnInfo> wrapper=new EntityWrapper<RuleColumnInfo>();
		wrapper.eq("table_id", id);
		wrapper.orderBy("ORDER_SEQ");
		List<RuleColumnInfo> list= ruleColumnInfoService.selectList(wrapper);
		return JsonUtils.toJson(list);
	}
	
	@RequestMapping("/ruleTableDataGrid")
	  @ResponseBody
	public Object findRuleTableInfo (RuleTableInfoVo roleTableInfo, Integer page, Integer rows, String sort, String order){
	     PageInfo pageInfo = new PageInfo(page, rows, sort, order);
	        Map<String, Object> condition = new HashMap<String, Object>();

	        if (StringUtils.isNotBlank(roleTableInfo.getMenuName())) {
	            condition.put("menuName", roleTableInfo.getMenuName());
	        }
	       /* if (userVo.getOrganizationId() != null) {
	            condition.put("organizationId", userVo.getOrganizationId());
	        }*/
	        if (roleTableInfo.getRuleType() != null) {
	            condition.put("ruleType", roleTableInfo.getRuleType());
	        }
	        if (roleTableInfo.getIsUsed() != null) {
	            condition.put("isUsed", roleTableInfo.getIsUsed());
	        }
	        pageInfo.setCondition(condition);
	        ruleTableInfoService.selectRuleTable(pageInfo);
	        return pageInfo;
		
		
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public Object insertTable(HttpSession httpSession,@RequestBody RuleTableInfo ruleTableInfo){
		ShiroUser shiroUser= getShiroUser();
		try {
			String msg= ruleTableInfoService.createTable(ruleTableInfo,shiroUser);
			if(msg.equals("新增规则成功")){
				httpSession.removeAttribute("tree");
			}
			return renderSuccess(msg);
		} catch (Exception e) {
			return e.getMessage();
		} 
	}

	@RequestMapping("/update")
	@ResponseBody
	public Object updateTable(HttpSession httpSession,@RequestBody RuleTableInfo ruleTableInfo){
		ShiroUser shiroUser=getShiroUser();
			String msg= ruleTableInfoService.createTableUpdate(ruleTableInfo,shiroUser);
			httpSession.removeAttribute("tree");
			return renderSuccess(msg);
	}
	
	@PostMapping("/selectRuleTableInfoForMenuId")
    @ResponseBody
	public String selectRuleTableInfoForMenuId(String menuId)
	{
		List<Map<String, String>> list=ruleTableInfoService.selectRuleTableInfoForMenuId(menuId);
		
		String jsonStr = JsonUtils.toJson(list); 
		return jsonStr;
	}
}
