package com.shuxin.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.shuxin.model.DiagnosticCatalog;
import com.shuxin.service.IDiagnosisCatalogService;

@Controller
@RequestMapping("/diagnosisCatalog")
public class DiagnosisCatalogController extends BaseController {

	  @Autowired
	  private IDiagnosisCatalogService diagnosisCatalogService;
	
	  
		@RequestMapping("/manager")
		public String  toRuleTableInfo(HttpServletRequest request,  Model model){
			  model=getMenuId(request, model);
			return "admin/diagnosisCatalog";
		}
		
	  @RequestMapping("/diagnosisCatalogDataGrid")
	  @ResponseBody
	public Object findbzDiagnosisDataGrid (DiagnosticCatalog  diagnosticCatalog, Integer page, Integer rows, String sort, String order){
	     PageInfo pageInfo = new PageInfo(page, rows, sort, order);
	        Map<String, Object> condition = new HashMap<String, Object>();

	        if (diagnosticCatalog.getDiagnosisCode()!=null&&StringUtils.isNotBlank(diagnosticCatalog.getDiagnosisCode())) {
	            condition.put("diagnosisCode", diagnosticCatalog.getDiagnosisCode());
	        }
	       /* if (userVo.getOrganizationId() != null) {
	            condition.put("organizationId", userVo.getOrganizationId());
	        }*/
	       
	        if (diagnosticCatalog.getDiagnosisName()!=null&&StringUtils.isNotBlank(diagnosticCatalog.getDiagnosisName())) {
	            condition.put("diagnosisName", diagnosticCatalog.getDiagnosisName());
	        }
	        pageInfo.setCondition(condition);
	        diagnosisCatalogService.findDiagnosticCatalogDataGrid(pageInfo);
	        return pageInfo;
		
		
	}

	@RequestMapping("/updateDiagnosisCatalog")
	@ResponseBody
	public Object addDictionary( DiagnosticCatalog  diagnosticCatalog){
	//  List<Dictionary> finddic=	 dictionaryService.selectByType(vo);
	ShiroUser user = getShiroUser();
	  List<DiagnosticCatalog> list = diagnosisCatalogService.selectBycode(diagnosticCatalog);
    if (list != null && !list.isEmpty()) {
        return renderError("该诊断已存在!");
    }

    diagnosisCatalogService.editDiagnosis(diagnosticCatalog,user);
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
  @RequestMapping("/deleteDiagnosticCatalog")
  @ResponseBody
  public Object delete(  HttpServletRequest request) {
  	ShiroUser user = getShiroUser();
  	 String  ids=request.getParameter("id");
  		String[]  allIds=ids.split(",");
 		 List<String>  Ids=new  ArrayList<String>();
 		  for (int j = 0; j < allIds.length; j++) {
 			Ids.add(allIds[j]);
 		}
 		diagnosisCatalogService.deleteDiagnosticCatalog(Ids,user);
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

		  DiagnosticCatalog diagnosisBZ = diagnosisCatalogService.selectById(Id);
     
 //  
  	String jsonStr = JsonUtils.toJson(diagnosisBZ); 
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
		  List<Map<String, String>> errorlist=importDataValidate( workbook,"diagnosisCatalogTemp",exportList);
			if (errorlist.size() > 0) {
				ImportErrorExcelUtils
						.creatErrorExcel( response, errorlist);
				return renderError("导入失败");
			}
			diagnosisCatalogService.importData( exportList, user);
		
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
     ExcelUtil.exportExcel(response, "diagnosisCatalogTemp", "诊断目录导入模板", map);
  	
	}
  /**
   * 导出
   * @param request
   * @param response
   */
  @RequestMapping("/exportDiagnosisCatalog")
  @ResponseBody
  public void exportDiagnosisCatalog( HttpServletResponse response)   {
  	HashMap<String, Object> map=new HashMap<String, Object>();
  	List<Map<String, Object>> detailList=diagnosisCatalogService.selectDetailDiagnosisCatalog();
  	map.put("detailList", detailList);
  	ExcelUtil.exportExcel(response, "diagnosisCatalogDetail", "诊断目录明细", map);
  	
  }
  /**
   * 导出
   * @param request
   * @param response
   */
  @RequestMapping("/exportDiagnosisCatalogHistory")
  @ResponseBody
  public void exportDiagnosisCatalogHistory( HttpServletResponse response)   {
  	HashMap<String, Object> map=new HashMap<String, Object>();
  	List<Map<String, Object>> historyList=diagnosisCatalogService.selectDiagnosisCatalogHistory();
  	map.put("historyList", historyList);
  	ExcelUtil.exportExcel(response, "diagnosisCatalogHistory", "诊断目录历史记录", map);
  	
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
						if (i == 0 ) {

							if (DataValidationUtils.isContainChinese(cellContent)) {
								// return "该模板第"+rowNum+"行第"+i+"列数据类型不正确";
								addErroList(errorList, rowNum, i + 1,
										"数据不能包含中文");
							}
							
							
								if (cell.getStringCellValue().length() > 40) {
									// return "该模板第"+rowNum+"行第"+i+"列字符过长";
									addErroList(errorList, rowNum, i + 1,
											"长度不能超过20字符");
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
