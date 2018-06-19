package com.shuxin.controller;

import java.io.InputStream;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.DataValidationUtils;
import com.shuxin.commons.utils.ExcelUtil;
import com.shuxin.commons.utils.ImportErrorExcelUtils;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.MIDiagnosis;
import com.shuxin.service.IMIDiagnosisService;

@Controller
@RequestMapping("/miDiagnosis")
public class MIDiagnosisController extends BaseController{

	@Autowired 
	private IMIDiagnosisService diagnosisService;
	
	@RequestMapping("/manager")
	public String  toRuleTableInfo(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
		return "admin/miDiagnosis";
	}
	
  @RequestMapping("/miDiagnosisDataGrid")
  @ResponseBody
public Object findMIDiagnosisDataGrid (MIDiagnosis  diagnosis, Integer page, Integer rows, String sort, String order){
     PageInfo pageInfo = new PageInfo(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<String, Object>();

        if (diagnosis.getDiagnosisCode()!=null&&StringUtils.isNotBlank(diagnosis.getDiagnosisCode())) {
            condition.put("diagnosisCode", diagnosis.getDiagnosisCode());
        }
       /* if (userVo.getOrganizationId() != null) {
            condition.put("organizationId", userVo.getOrganizationId());
        }*/
       
        if (diagnosis.getDiagnosisName()!=null&&StringUtils.isNotBlank(diagnosis.getDiagnosisName())) {
            condition.put("diagnosisName", diagnosis.getDiagnosisName());
        }
        pageInfo.setCondition(condition);
        diagnosisService.findMIDiagnosisDataGrid(pageInfo);
        return pageInfo;
	
	
}

@RequestMapping("/updateMIDiagnosis")
@ResponseBody
public Object updateMIDiagnosis( MIDiagnosis diagnosis ){
//  List<Dictionary> finddic=	 dictionaryService.selectByType(vo);
ShiroUser user = getShiroUser();
  List<MIDiagnosis> list = diagnosisService.selectBycode(diagnosis);
if (list != null && !list.isEmpty()) {
    return renderError("该诊断已存在!");
}

diagnosisService.editDiagnosis(diagnosis,user);
/*     insertOperationLog(getShiroUser().getLoginName(),
		"add","T_USER",userVo.toString());*/
return renderSuccess("操作成功");



}


/**
* 删除用户
*
* @param id
* @return
*/
@RequestMapping("/deleteMIDiagnosis")
@ResponseBody
public Object delete(  HttpServletRequest request) {
	ShiroUser user = getShiroUser();
	 String  ids=request.getParameter("id");
		String[]  allIds=ids.split(",");
		 List<String>  Ids=new  ArrayList<String>();
		  for (int j = 0; j < allIds.length; j++) {
			Ids.add(allIds[j]);
		}
		diagnosisService.deleteMIDiagnosis(Ids,user);
/*     insertOperationLog(getShiroUser().getLoginName(),
			"delete","T_USER",userIds.toString());*/
  return renderSuccess("删除成功！");
}
/**
* 编辑用户页
*
* @param id
* @param model
* @return
*/
@PostMapping("/editPage")
@ResponseBody
public String editPage(Model model, String Id,HttpServletRequest request){
	  model=getMenuId(request, model);

	  MIDiagnosis diagnosis = diagnosisService.selectById(Id);
 
//  
	String jsonStr = JsonUtils.toJson(diagnosis); 
	return jsonStr;

}
/**
* 导入数据
* @param file
* @param request
* @param response
* @param id
* @return
* @throws Exception
*/
@RequestMapping("/importExcel")
@ResponseBody
public Object importExcel(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request, HttpServletResponse response,String id)   
{
	try {
  InputStream in = file.getInputStream();
	String fileName = file.getOriginalFilename();
	String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);
	ShiroUser user=getShiroUser();
	 Workbook workbook =null;
	if ("xlsx".equals(lastname )||"xls".equals(lastname)) {
				workbook=	new XSSFWorkbook(in);
			
	}  else {
		return renderSuccess("导入文件格式不正确");
	}
	List<Map<String, String>> exportList = new ArrayList<Map<String, String>>();
	  List<Map<String, String>> errorlist=importDataValidate( workbook,"miDiagnosisTemp",exportList);
		if (errorlist.size() > 0) {
			ImportErrorExcelUtils
					.creatErrorExcel( response, errorlist);
			return renderError("导入失败");
		}
		diagnosisService.importData( exportList, user);
	
	}catch(Exception e){
		logger.error(e.getMessage(), e);
		return renderError("导入失败");
	}
	return renderSuccess("导入成功！");
}

/**
* 导出模板
* @param request
* @param response
*/
@RequestMapping("/exportTemp")
@ResponseBody
public void exportTemp( HttpServletResponse response)   {
  HashMap<String, Object> map=new HashMap<String, Object>();
 ExcelUtil.exportExcel(response, "miDiagnosisTemp", "适应症诊断导入模板", map);
	
}
/**
* 导出
* @param request
* @param response
*/
@RequestMapping("/exportMIDiagnosis")
@ResponseBody
public void exportMIDiagnosis( HttpServletResponse response)   {
	HashMap<String, Object> map=new HashMap<String, Object>();
	List<Map<String, Object>> detailList=diagnosisService.selectDetailMIDiagnosis();
	map.put("detailList", detailList);
	ExcelUtil.exportExcel(response, "miDiagnosisDetail", "适应症诊断明细", map);
	
}
/**
* 导出
* @param request
* @param response
*/
@RequestMapping("/exportMIDiagnosisHistory")
@ResponseBody
public void exportMIDiagnosisHistory( HttpServletResponse response)   {
	HashMap<String, Object> map=new HashMap<String, Object>();
	List<Map<String, Object>> historyList=diagnosisService.selectMIDiagnosisHistory();
	map.put("historyList", historyList);
	ExcelUtil.exportExcel(response, "miDiagnosisHistory", "适应症诊断历史记录", map);
	
}

private List<Map<String, String>> importDataValidate(Workbook workbook, String fileName,List<Map<String, String>> exportList) {
	// TODO Auto-generated method stub
	Sheet sheet = workbook.getSheetAt(0);
	String p = sheet.getSheetName();
	List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
	Row title = sheet.getRow(0);
	Map<String, String> resultMap = null;
	 List<String> titleName=ExcelUtil.readExcelTempTitle(fileName);
	for (int i = 0; i < titleName.size(); i++) {
		// HSSFCell c = null;
		if(title.getCell(i)==null  ||
				!titleName.get(i).trim().equals(title.getCell(i).getStringCellValue().trim()))
		{
			resultMap = new HashMap<String, String>();
			resultMap.put("rows", "第一行");
			resultMap.put("cols", "第" + (i+1) + "列");
			resultMap.put("info", "列头信息不对");
			errorList.add(resultMap);
		}
	}
	if(errorList.size()>0)
	{
		return errorList;
	}
	List<Integer> uniqueIndex = new ArrayList<Integer>();
	uniqueIndex.add(0);
	Map<String, String> projectCatalogMap = null;
	String cellContent="";
	for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

		Row rows = sheet.getRow(rowNum);
	
		if (rows != null) {
			projectCatalogMap = new HashMap<String,String>();
			for (int i = 0; i < titleName.size(); i++) {
				Cell cell = rows.getCell(i);
			//	String celltext = null;
				if (cell != null) {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellContent = cell.getStringCellValue();
				}

				if (cellContent == null || cellContent.equals("")) {
					for (int j = 0; j < uniqueIndex.size(); j++) {
						if (i == j) {
							addErroList(errorList, rowNum, i + 1, "必填项为空");
						}
					}

				} else if (cell != null && !cellContent.equals("")) {
					if (i == 0 || i == 1) {
						if (DataValidationUtils.isContainChinese(cellContent)) {
							// return "该模板第"+rowNum+"行第"+i+"列数据类型不正确";
							addErroList(errorList, rowNum, i + 1,
									"数据不能包含中文");
						}
						
						if (i == 0) {
							if (cell.getStringCellValue().length() > 20) {
								// return "该模板第"+rowNum+"行第"+i+"列字符过长";
								addErroList(errorList, rowNum, i + 1,
										"长度不能超过20字符");
							}
						} else {
							if (cell.getStringCellValue().length() > 5) {
								// return "该模板第"+rowNum+"行第"+i+"列字符过长";
								addErroList(errorList, rowNum, i + 1,
										"长度不能超过5字符");
							}
						}

					} else {
						if (cell.getStringCellValue().length() > 50) {
							// return "该模板第"+rowNum+"行第"+i+"列字符过长";
							addErroList(errorList, rowNum, i + 1,
									"长度不能超过50字符");
						}
					}
				}

				projectCatalogMap.put("field"+i, cellContent);	
				cellContent="";
				
				
			}
			projectCatalogMap.put("field"+titleName.size(), UUID.randomUUID().toString().replace("-", ""));
			exportList.add(projectCatalogMap);
		}
	}

	return errorList;

}




public void addErroList(List<Map<String, String>> errorList,int rowNum,int cols,String message){
	Map<String, String> resultmap = new HashMap<String, String>();

	resultmap.put("rows", "第" + rowNum + "行");
	resultmap.put("cols", "第" + cols + "列");
	resultmap.put("info", message);
	errorList.add(resultmap);
}

	
	
	
	
	
	
	
	
	
	
	
}
