package com.shuxin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.utils.DataValidationUtils;
import com.shuxin.commons.utils.ExcelUtil;
import com.shuxin.commons.utils.ImportErrorExcelUtils;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.vo.DrugInfoVo;
import com.shuxin.service.IDrugInfoService;

@Controller
@RequestMapping("/singleFee")
public class SingleFeeController extends BaseController{
	
	@Autowired
	private IDrugInfoService drugInfoService;
	
	@GetMapping("/manager")
    public String manager(HttpServletRequest request,  Model model){
		  getMenuId(request, model);
        return "admin/singleFee";
    }
	
	@RequestMapping("/searchDrugInfo")
    @ResponseBody
	public Object searchDrugInfo(DrugInfoVo drugInfoVo,Integer page, Integer rows, String sort, String order)
	{
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		
		if (StringUtils.isNotBlank(drugInfoVo.getDrugCode())) {
            condition.put("drugCode", drugInfoVo.getDrugCode());
        }
		
		if (StringUtils.isNotBlank(drugInfoVo.getDrugName())) {
            condition.put("drugName", drugInfoVo.getDrugName());
        }
		condition.put("ruleType", "2");
		pageInfo.setCondition(condition);
		drugInfoService.selectDrugInfoVoPage(pageInfo);
		return pageInfo;
	}
	
	@PostMapping("/editDrugInfo")
    @ResponseBody
	public Object editDrugInfo(DrugInfoVo drugInfoVo)
	{
		drugInfoVo.setRuleType("2");
		int existDrugInfo = drugInfoService.selectExistDrugInfo(drugInfoVo);
		if(existDrugInfo>0)
		{
			return renderError("该药品已经存在!");
		}
		
		drugInfoService.editDrugInfo(drugInfoVo, getShiroUser());
		return renderSuccess("操作成功");
	}
	
	@PostMapping("/selectEditDrugInfo")
    @ResponseBody
	public Object selectEditDrugInfo(String id)
	{
		DrugInfoVo drugInfoVo = drugInfoService.selectDrugInfoVo(id);
		String jsonStr = JsonUtils.toJson(drugInfoVo); 
		return jsonStr;
	}
	
	@RequestMapping("/deleteDrugInfo")
    @ResponseBody
	public Object deleteDrugInfo(@RequestParam("id[]")List<String> list)
	{
		drugInfoService.deleteDrugInfo(list, getShiroUser());
		return renderSuccess("删除成功！");
	}
	
	@RequestMapping("/exportDrugInfo")
    @ResponseBody
	public void exportDrugInfo(HttpServletResponse response)
	{
		DrugInfoVo drugInfoVo = new DrugInfoVo();
		drugInfoVo.setRuleType("2");
		List<Map<String, Object>> list = drugInfoService.selectDrugInfoVoPage(drugInfoVo);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		ExcelUtil.exportExcel(response, "drugInfoExpTemp", "药品除外表", map);
	}
	
	@RequestMapping("/exportDrugInfoHistory")
    @ResponseBody
	public void exportDrugInfoHistory(HttpServletResponse response)
	{
		DrugInfoVo drugInfoVo = new DrugInfoVo();
		drugInfoVo.setRuleType("2");
		List<Map<String, Object>> list =drugInfoService.selectDrugInfoHistory(drugInfoVo);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		ExcelUtil.exportExcel(response, "drugInfoHistroyTemp", "药品除外表历史记录", map);
	}
	
	@RequestMapping("/exportTemp")
    @ResponseBody
	public void exportTemp(HttpServletResponse response)
	{
		ExcelUtil.exportExcel(response, "drugInfoImpTemp", "药品除外表导入模版", new HashMap());
	}
	
	@RequestMapping("/importExcel")
    @ResponseBody
	public Object importExcel(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response)
	{
		try 
		{
			String fileName = file.getOriginalFilename();
			String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			
			 Workbook workbook =null;
			 
			 if("xlsx".equals(fileSuffix)||"xls".equals(fileSuffix))
			 {
				workbook=	new XSSFWorkbook(file.getInputStream());
			 }
			else
			 {
				return renderSuccess("导入文件格式不正确");
			 }
			 
			 List<Map<String, String>> exportList = new ArrayList<Map<String, String>>();
			 List<Map<String, String>> errorList = validateExportData(workbook,exportList);
			 if(errorList.size()>0)
			 {
				 ImportErrorExcelUtils
					.creatErrorExcel(response, errorList);
				 return renderError("导入失败");
			 }
			 
			 drugInfoService.importDrugInfo(exportList,getShiroUser().getLoginName());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			return renderError("导入失败");
		}
		return renderSuccess("导入成功！");
	}
	
	private List<Map<String, String>> validateExportData(Workbook workbook,List<Map<String, String>> exportList)
	{
		Sheet sheet = workbook.getSheetAt(0);
		Row title = sheet.getRow(0);
		
		List<String> tempTitle = ExcelUtil.readExcelTempTitle("drugInfoImpTemp");
		Map<String, String> resultMap = null;
		List<Map<String, String>> errorList = ExcelUtil.validateImpTempTitle(title,tempTitle);
		
		if(errorList.size()>0)
		{
			return errorList;
		}
		
		Map<String, String> map = null;
		String cellContent="";
		for (int rowNum = 1; rowNum <=sheet.getLastRowNum(); rowNum++) 
		{
			Row rows = sheet.getRow(rowNum);
			if(rows==null)
			{
				continue;
			}
			map = new HashMap<String,String>();
			
			for(int i=0;i<tempTitle.size();i++)
			{
				Cell cell = rows.getCell(i);
				if(cell != null )
				{
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if(!StringUtils.isEmpty(cell.getStringCellValue()))
					{
						cellContent=cell.getStringCellValue();
					}
				}
				
				if(StringUtils.isEmpty(cellContent))
			  	{
			  		resultMap = new HashMap<String, String>();
			  		resultMap.put("rows", "第" + rowNum + "行");
			  		resultMap.put("cols", "第" + (i+1) + "列");
			  		resultMap.put("info", tempTitle.get(i)+"不能为空");
					errorList.add(resultMap);
			  	}
				else if(i==0)
				{
					if (DataValidationUtils.isContainChinese(cellContent))
			  		{
			  			resultMap = new HashMap<String, String>();
				  		resultMap.put("rows", "第" + rowNum + "行");
				  		resultMap.put("cols", "第" + (i+1) + "列");
				  		resultMap.put("info", tempTitle.get(i)+"不能包含中文");
						errorList.add(resultMap);
			  		}
				}
				int length = validateLength(i,cellContent);
				if(length>0)
				{
					resultMap = new HashMap<String, String>();
			  		resultMap.put("rows", "第" + rowNum + "行");
			  		resultMap.put("cols", "第" + (i+1) + "列");
			  		resultMap.put("info", tempTitle.get(i)+"长度不能超过"+length+"个字符");
					errorList.add(resultMap);
				}
				map.put("field"+i, cellContent);	
				cellContent="";
			}
			map.put("field"+tempTitle.size(), UUID.randomUUID().toString().replace("-", ""));
			map.put("ruleType", "2");
			exportList.add(map);
		}
		return errorList;
	}
	
	private int validateLength(int index,String cellContent)
	{
		int length=0;
		if(StringUtils.isEmpty(cellContent))
		{
			return length;
		}
		
		switch(index)
		{
		  case 0:
			  if(cellContent.length()>20)
			  {
				  length = 20;
			  }
			  break;
		  case 1:
			  if(cellContent.length()>30)
			  {
				  length = 30;
			  }
			  break;		  		 
		}
		return length;
		
	}

}
