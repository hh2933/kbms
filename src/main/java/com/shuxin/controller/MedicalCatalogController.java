package com.shuxin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.result.Result;
import com.shuxin.commons.utils.ExcelUtil;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.MedicalCatalog;
import com.shuxin.service.IMedicalCatalogService;

@Controller
@RequestMapping("/catalog")
public class MedicalCatalogController extends BaseController {
	
	@Autowired
	IMedicalCatalogService catalogService;
	@Autowired
	HttpServletResponse response;
	 
	@RequestMapping("/manager")
	public String  toRuleTableInfo(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
		return "admin/medicalCatalog";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Object  getCatalogTableInfo(MedicalCatalog catalog, Integer page, Integer rows, String sort, String order){
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(catalog.getYyxmbm())) {
            condition.put("yyxmbm", catalog.getYyxmbm());
        }
        if (StringUtils.isNotBlank(catalog.getYbxmbm())) {
            condition.put("ybxmbm", catalog.getYbxmbm());
        }
        if (StringUtils.isNotBlank(catalog.getXmmc())) {
            condition.put("xmmc", catalog.getXmmc());
        }
        condition.put("yblx", catalog.getYblx());
        condition.put("czlx", catalog.getCzlx());
        condition.put("clzt", catalog.getClzt());
        pageInfo.setCondition(condition);
		catalogService.getCatalogTableInfo(pageInfo);
		return pageInfo;
	}
	
	@RequestMapping("/handle")
	@ResponseBody
	public Object updateClzt(String clzt,String ids){
		Boolean flag = catalogService.updateClzt(clzt,ids);
		if(flag){
			return renderSuccess("处理状态更新成功");
		}else {
			return renderError("处理异常");
		}
	}
	
	@RequestMapping("/export")
	@ResponseBody
	public void exportCatalogInfo(MedicalCatalog catalog){

		List<Map<String, String>> Info = catalogService.getCatalogExportInfo(catalog);
		
		Map<String, Object> map = new HashMap<String,Object>();	
		
		map.put("list", Info);
		
		ExcelUtil.exportExcel(response, "catalogInfoExcel", "医保目录更新统计信息", map);	
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
