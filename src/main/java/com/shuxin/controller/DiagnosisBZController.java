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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.DataValidationUtils;
import com.shuxin.commons.utils.DigestUtils;
import com.shuxin.commons.utils.ExcelUtil;
import com.shuxin.commons.utils.ImportErrorExcelUtils;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.DiagnosticmaBZMpping;
import com.shuxin.model.Dictionary;
import com.shuxin.model.Role;
import com.shuxin.model.SysUser;
import com.shuxin.model.vo.DictionaryListVo;
import com.shuxin.model.vo.SysUserVo;
import com.shuxin.service.IDiagnosisBZService;


@Controller
@RequestMapping("/diagnosisBZ")
public class DiagnosisBZController extends BaseController {

	  @Autowired
	  private IDiagnosisBZService bzService;
	
	  
		@RequestMapping("/manager")
		public String  toRuleTableInfo(HttpServletRequest request,  Model model){
			  model=getMenuId(request, model);
			return "admin/bzDiagnosis";
		}
		
	  @RequestMapping("/bzDiagnosisDataGrid")
	  @ResponseBody
	public Object findbzDiagnosisDataGrid (DiagnosticmaBZMpping diagnosisBZ, Integer page, Integer rows, String sort, String order){
	     PageInfo pageInfo = new PageInfo(page, rows, sort, order);
	        Map<String, Object> condition = new HashMap<String, Object>();

	        if (diagnosisBZ.getDiagnosisCode()!=null&&StringUtils.isNotBlank(diagnosisBZ.getDiagnosisCode())) {
	            condition.put("diagnosisCode", diagnosisBZ.getDiagnosisCode());
	        }
	       /* if (userVo.getOrganizationId() != null) {
	            condition.put("organizationId", userVo.getOrganizationId());
	        }*/
	       
	        if (diagnosisBZ.getDiagnosisName()!=null&&StringUtils.isNotBlank(diagnosisBZ.getDiagnosisName())) {
	            condition.put("diagnosisName", diagnosisBZ.getDiagnosisName());
	        }
	        pageInfo.setCondition(condition);
	        bzService.findbzDiagnosisDataGrid(pageInfo);
	        return pageInfo;
		
		
	}

	@RequestMapping("/updateDiagnosisBZ")
	@ResponseBody
	public Object addDictionary( DiagnosticmaBZMpping diagnosisBZ){
	//  List<Dictionary> finddic=	 dictionaryService.selectByType(vo);
	ShiroUser user = getShiroUser();
	  List<DiagnosticmaBZMpping> list = bzService.selectBycode(diagnosisBZ);
      if (list != null && !list.isEmpty()) {
          return renderError("该诊断已存在!");
      }

      bzService.editDiagnosis(diagnosisBZ,user);
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
    @RequestMapping("/deleteDiagnosisBZ")
    @ResponseBody
    public Object delete(  HttpServletRequest request) {
    	ShiroUser user = getShiroUser();
    	 String  ids=request.getParameter("id");
    		String[]  allIds=ids.split(",");
   		 List<String>  Ids=new  ArrayList<String>();
   		  for (int j = 0; j < allIds.length; j++) {
   			Ids.add(allIds[j]);
   		}
   		bzService.deleteDiagnosisBZ(Ids,user);
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

		  DiagnosticmaBZMpping diagnosisBZ = bzService.selectById(Id);
       
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
	public Object importExcel(
			@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response, String id) {
		try {

			InputStream in = file.getInputStream();
			String fileName = file.getOriginalFilename();
			String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);
			ShiroUser user = getShiroUser();
			Workbook workbook = null;
			if ("xlsx".equals(lastname) || "xls".equals(lastname)) {

				workbook = new XSSFWorkbook(in);
	
			} else {
				return renderSuccess("导入文件格式不正确");
			}

			List<Map<String, String>> exportList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> errorlist = importDataValidate(workbook,
					"diagnosisBZTemp", exportList);
			if (errorlist.size() > 0) {
				ImportErrorExcelUtils.creatErrorExcel(response, errorlist);
				return renderError("导入失败");
			}
		 bzService.importData(exportList, user);

			
		} catch (Exception e) {
			// TODO: handle exception
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
	public void exportTempExcel( HttpServletResponse response)   {
	  HashMap<String, Object> map=new HashMap<String, Object>();
       ExcelUtil.exportExcel(response, "diagnosisBZTemp", "诊断标准映射编码导入模板", map);
    	
	}
    /**
     * 导出
     * @param request
     * @param response
     */
    @RequestMapping("/exportBzDiagnosis")
    @ResponseBody
    public void exportBzDiagnosis( HttpServletResponse response)   {
    	HashMap<String, Object> map=new HashMap<String, Object>();
    	List<Map<String, Object>> detailList=bzService.selectDetailDiagnosisBZ();
    	map.put("detailList", detailList);
    	ExcelUtil.exportExcel(response, "diagnosisBZDetail", "诊断标准映射编码明细", map);
    	
    }
    /**
     * 导出
     * @param request
     * @param response
     */
    @RequestMapping("/exportBzDiagnosisHistory")
    @ResponseBody
    public void exportBzDiagnosisHistory( HttpServletResponse response)   {
    	HashMap<String, Object> map=new HashMap<String, Object>();
    	List<Map<String, Object>> historyList=bzService.selectDiagnosisBZHistory();
    	map.put("historyList", historyList);
    	ExcelUtil.exportExcel(response, "diagnosisBZHistory", "诊断标准映射编码历史记录", map);
    	
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
						if (i == 0 || i == 2) {

							if (DataValidationUtils.isContainChinese(cellContent)) {
								// return "该模板第"+rowNum+"行第"+i+"列数据类型不正确";
								addErroList(errorList, rowNum, i + 1,
										"数据不能包含中文");
							}
							
							
								if (cell.getStringCellValue().length() > 40) {
									// return "该模板第"+rowNum+"行第"+i+"列字符过长";
									addErroList(errorList, rowNum, i + 1,
											"长度不能超过40字符");
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
