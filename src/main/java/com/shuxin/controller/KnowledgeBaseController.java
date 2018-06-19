package com.shuxin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.DataValidationUtils;
import com.shuxin.commons.utils.ImportErrorExcelUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.model.vo.KnowledgeBaseVo;
import com.shuxin.service.IKnowledgeBaseService;
import com.shuxin.service.IRuleTableInfoService;

/**
 * @description：知识库管理
 */
@Controller
@RequestMapping("/knowledgeBase")
public class KnowledgeBaseController extends BaseController{
	private   Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private IKnowledgeBaseService knowledgeBaseService;	
	
	
	@Autowired
	private  IRuleTableInfoService ruleTableInfoService;
	
	@GetMapping("/manager")
    public String manager(HttpServletRequest request,  Model model){
		  getMenuId(request, model);
        return "knowledgeBase/knowledgeBase";
    }
	
	@RequestMapping("/searchKnowledgeBase")
    @ResponseBody
	public Object searchKnowledgeBase(@RequestBody KnowledgeBaseVo knowledgeBaseVo)
	{
		PageInfo pageInfo = new PageInfo(knowledgeBaseVo.getPage(), knowledgeBaseVo.getRows(), knowledgeBaseVo.getSort(), knowledgeBaseVo.getOrder());
		knowledgeBaseService.selectKnowledgeBaseVoPage(pageInfo, knowledgeBaseVo);
		return pageInfo;
	}
	
	@RequestMapping("/editKnowledgeBase")
    @ResponseBody
	public Object editKnowledgeBase(@RequestBody KnowledgeBaseVo knowledgeBaseVo)
	{
		int existKnowledgeBase = knowledgeBaseService.selectExistKnowledgeBase(knowledgeBaseVo);
		if(existKnowledgeBase >0)
		{
			return renderError("该信息已经存在!");
		}
		knowledgeBaseVo.setLoginName(getShiroUser().getLoginName());
		knowledgeBaseService.editKnowledgeBase(knowledgeBaseVo);
		
		return renderSuccess("操作成功");
	}
	
	@RequestMapping("/delKnowledgeBase")
    @ResponseBody
	public Object delKnowledgeBase(@RequestParam("id[]")List<String> list,String tableName)
	{
		knowledgeBaseService.delKnowledgeBase(list, tableName,getShiroUser().getLoginName());
		
		return renderSuccess("删除成功！");
	}
	@RequestMapping("/exportExcel")
    @ResponseBody
	public void exportExcel(HttpServletRequest request,
			HttpServletResponse response, String id) {
		List<String> fieldList = new ArrayList<String>();

		List<Map<String, String>> tableinfo = ruleTableInfoService
				.selectRuleTableInfoForMenuId(id);
		for (int i = 0; i < tableinfo.size(); i++) {

			fieldList.add(tableinfo.get(i).get("TH_NAME").toString());
		}

		String menuName = tableinfo.get(0).get("MENU_NAME");
		response.setContentType("application/vnd.ms-excel");
		OutputStream fOut = null;
		try {

			String fileName = java.net.URLEncoder.encode(menuName, "UTF-8");
			response.setHeader("content-disposition", "attachment;filename="
					+ fileName + ".xls");
			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet(menuName + "模板");
			Font font = workbook.createFont();
			font.setFontName("仿宋_GB2312");
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
			font.setFontHeightInPoints((short) 16);

			CellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);// 前景颜色
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// 填充方式，前色填充
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
			style.setFont(font);
			Row row = sheet.createRow((int) 0);// 创建一行
			row.setHeightInPoints((short) 30);
			for (int j = 0; j < fieldList.size(); j++) {

				Cell cell = row.createCell((int) j);// 创建一列
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(fieldList.get(j));
				cell.setCellStyle(style);
				sheet.autoSizeColumn(j);
			}

			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	
	
	@RequestMapping("/importExcel")
    @ResponseBody
	public Object importExcel(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request, HttpServletResponse response,String id) throws  Exception
 {
		InputStream in = file.getInputStream();
		String fileName = file.getOriginalFilename();
		String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<Map<String, String>> tableinfo = ruleTableInfoService
				.selectRuleTableInfoForMenuId(id);

		ShiroUser user = getShiroUser();
		if ( "xlsx".equals(lastname)|| "xls".equals(lastname)) {
			
		  Workbook workbook =	new XSSFWorkbook(in);
				
				  
		  
		  List<Map<String, String>> errorlist=DataValidationUtils.importDataValidate(tableinfo, workbook);
			if (errorlist.size() > 0) {
				ImportErrorExcelUtils
						.creatErrorExcel(response, errorlist);
				return renderError("导入失败");
			}
			String result = knowledgeBaseService.importData(
					tableinfo, workbook, user);
		

			return renderSuccess(result);
		}  else {
			return renderSuccess("导入文件格式不正确");
		}

	}

	@RequestMapping("/exportKnowledgeBaseHistory")
    @ResponseBody
	public void exportKnowledgeBaseHistory(String menuId,HttpServletResponse response)
	{
		List<Map<String, String>> tableinfo = ruleTableInfoService
				.selectRuleTableInfoForMenuId(menuId);
		KnowledgeBaseVo knowledgeBaseVo = new KnowledgeBaseVo();
		List<String> selectColumns = new ArrayList<String>();
		List<String> thNames = new ArrayList<String>();
		for(Map<String, String> map:tableinfo)
		{
			selectColumns.add(map.get("COLUMN_NAME"));
			thNames.add(map.get("TH_NAME"));
		}
		knowledgeBaseVo.setSelectColumns(selectColumns);
		knowledgeBaseVo.setThNames(thNames);
		knowledgeBaseVo.setTableName(tableinfo.get(0).get("TABLE_NAME"));
		knowledgeBaseVo.setMenuName(tableinfo.get(0).get("MENU_NAME"));
		knowledgeBaseService.exportKnowledgeBaseHistory(knowledgeBaseVo, response);
		
	}

}
