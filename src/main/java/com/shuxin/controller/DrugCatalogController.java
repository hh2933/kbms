package com.shuxin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.shuxin.model.vo.DrugCatalogVo;
import com.shuxin.service.IDrugCatalogService;


@Controller
@RequestMapping("/drugCatalog")
public class DrugCatalogController extends BaseController{
	
	@Autowired
	private IDrugCatalogService drugCatalogService;
	
	private   Logger logger = LogManager.getLogger(getClass());
	
	@GetMapping("/manager")
    public String manager(HttpServletRequest request,  Model model){
		  getMenuId(request, model);
        return "admin/drugCatalog";
    }

	@RequestMapping("/searchDrugCatalog")
    @ResponseBody
	public Object searchDrugCatalog(DrugCatalogVo drugCatalogVo, Integer page, Integer rows, String sort, String order)
	{
		PageInfo pageInfo = new PageInfo(page, rows, sort, order);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		
		if (StringUtils.isNotBlank(drugCatalogVo.getDrugCode())) {
            condition.put("drugCode", drugCatalogVo.getDrugCode());
        }
		
		if (StringUtils.isNotBlank(drugCatalogVo.getDrugName())) {
            condition.put("drugName", drugCatalogVo.getDrugName());
        }
		
		pageInfo.setCondition(condition);
		drugCatalogService.selectDrugCatalogVoPage(pageInfo);
		return pageInfo;
	}
	
	@PostMapping("/editDrugCatalog")
    @ResponseBody
	public Object editDrugCatalog(DrugCatalogVo drugCatalogVo)
	{
		int existDrugCatalog = drugCatalogService.selectExistDrugCatalog(drugCatalogVo);
		if(existDrugCatalog >0)
		{
			return renderError("该药品目录已经存在!");
		}
		drugCatalogService.editDrugCatalog(drugCatalogVo, getShiroUser());
		return renderSuccess("操作成功");
	}
	
	@PostMapping("/selectEditDrugCatalog")
    @ResponseBody
	public Object selectEditDrugCatalog(String id)
	{
		DrugCatalogVo drugCatalogVo = drugCatalogService.selectDrugCatalogVo(id);
		String jsonStr = JsonUtils.toJson(drugCatalogVo); 
		return jsonStr;
	}
	
	@RequestMapping("/deleteDrugCatalog")
    @ResponseBody
    public Object deleteDrugCatalog(@RequestParam("id[]")List<String> list) {
		drugCatalogService.deleteDrugCatalog(list,  getShiroUser());
		return renderSuccess("删除成功！");
	}
	
	@RequestMapping("/exportDrugCatalog")
    @ResponseBody
	public void exportDrugCatalog(HttpServletResponse response)
	{
		List<Map<String, Object>> list =drugCatalogService.selectDrugCatalogVoPage();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		ExcelUtil.exportExcel(response, "drugCatalogExpTemp", "药品目录", map);
	}
	
	@RequestMapping("/exportDrugCatalogHistory")
    @ResponseBody
	public void exportDrugCatalogHistory(HttpServletResponse response)
	{
		List<Map<String, Object>> list =drugCatalogService.searchDrugCatalogHistroy();
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		ExcelUtil.exportExcel(response, "drugCatalogHistroyTemp", "药品目录历史记录", map);
	}
	
	@RequestMapping("/exportTemp")
    @ResponseBody
	public void exportTemp(HttpServletResponse response)
	{
		ExcelUtil.exportExcel(response, "drugCatalogImpTemp", "药品目录导入模版", new HashMap());
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
			 drugCatalogService.importDrugCatalog(exportList, getShiroUser().getLoginName());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return renderError("导入失败");
		}
		return renderSuccess("导入成功！");
	}
	
	private List<Map<String, String>> validateExportData(Workbook workbook,List<Map<String, String>> exportList)
	{
		Sheet sheet = workbook.getSheetAt(0);
		Row title = sheet.getRow(0);
		
		List<String> tempTitle = ExcelUtil.readExcelTempTitle("drugCatalogImpTemp");
		
		Map<String, String> resultMap = null;
		List<Map<String, String>> errorList = ExcelUtil.validateImpTempTitle(title,tempTitle);
		
		if(errorList.size()>0)
		{
			return errorList;
		}
		
		Map<String, String> drugCatalogMap = null;
		String cellContent="";
		for (int rowNum = 1; rowNum <=sheet.getLastRowNum(); rowNum++) 
		{
			Row rows = sheet.getRow(rowNum);
			if(rows==null)
			{
				continue;
			}
			drugCatalogMap = new HashMap<String,String>();
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
				if(i!=1&&i!=13)
				{
					if(StringUtils.isEmpty(cellContent))
				  	{
				  		resultMap = new HashMap<String, String>();
				  		resultMap.put("rows", "第" + rowNum + "行");
				  		resultMap.put("cols", "第" + (i+1) + "列");
				  		resultMap.put("info", tempTitle.get(i)+"不能为空");
						errorList.add(resultMap);
				  	}
					else if(i==7||i==11)
				  	{
				  		if (!DataValidationUtils.isNumber(cellContent))
				  		{
				  			resultMap = new HashMap<String, String>();
					  		resultMap.put("rows", "第" + rowNum + "行");
					  		resultMap.put("cols", "第" + (i+1) + "列");
					  		resultMap.put("info", tempTitle.get(i)+"只能是数字");
							errorList.add(resultMap);
				  		}
				  	}
					else if(i==0||i==3||i==12)
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
				}
				else if(i==1)
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
				drugCatalogMap.put("field"+i, cellContent);	
				cellContent="";
			}
			drugCatalogMap.put("field"+tempTitle.size(), UUID.randomUUID().toString().replace("-", ""));
			exportList.add(drugCatalogMap);
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
		  case 1:
			  if(cellContent.length()>20)
			  {
				  length = 20;
			  }
			  break;
		  case 2:
			  if(cellContent.length()>30)
			  {
				  length = 30;
			  }
			  break; 
		  case 3:
			  if(cellContent.length()>1)
			  {
				  length = 1;
			  }
			  break;
		  case 4:
			  if(cellContent.length()>20)
			  {
				  length = 20;
			  }
			  break;
		  case 5:
			  if(cellContent.length()>25)
			  {
				  length = 25;
			  }
			  break;
		  case 6:
			  if(cellContent.length()>5)
			  {
				  length = 5;
			  }
			  break;
		  case 8:			  
		  case 9:
			  if(cellContent.length()>10)
			  {
				  length = 10;
			  }
			  break;
		  case 10:
			  if(cellContent.length()>50)
			  {
				  length = 50;
			  }
			  break;
		  case 12:
			  if(cellContent.length()>2)
			  {
				  length = 2;
			  }
			  break;
		  case 13:
			  if(cellContent.length()>100)
			  {
				  length = 100;
			  }
			  break;
		}
		return length;
		
	}
	
}
