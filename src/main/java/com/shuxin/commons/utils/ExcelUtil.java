package com.shuxin.commons.utils;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUtil {
	
	private static final Logger logger = LogManager.getLogger(ExcelUtil.class);
	
	/**
	 * 导出excle文件
	 * @param response
	 * @param tempFileName
	 * @param exportFileName
	 * @param map
	 */
	public static void exportExcel(HttpServletResponse response,String tempFileName,
			String exportFileName,Map<String, Object> map)
	{
		InputStream in = null;
		OutputStream out = null;
		try
		{
			String templateFileName = Thread.currentThread()
					.getContextClassLoader().getResource("").getPath()
					+ "excleTemplate/"+tempFileName+ ".xls";
			String destFileName = java.net.URLEncoder.encode(exportFileName, "UTF-8")
					+ ".xls";
			XLSTransformer transformer = new XLSTransformer();
			
			// 设置响应
			response.setHeader("Content-Disposition", "attachment;filename="
					+ destFileName);
			response.setContentType("application/vnd.ms-excel");
			
			in = new BufferedInputStream(new FileInputStream(templateFileName));
			Workbook workbook = transformer.transformXLS(in, map);
			out = response.getOutputStream();
			// 将内容写入输出流并把缓存的内容全部发出去
			workbook.write(out);
			out.flush();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 读取Excel模版列头
	 * @param tempFileName
	 * @return
	 */
	public  static List<String> readExcelTempTitle(String tempFileName)
	{
		String templateFileName = Thread.currentThread()
				.getContextClassLoader().getResource("").getPath()
				+ "excleTemplate/"+tempFileName+ ".xls";
		List<String> titleList = new ArrayList<String>();
		try {
			Workbook workbook=	new XSSFWorkbook(new FileInputStream(new File(templateFileName)));
			Sheet sheet = workbook.getSheetAt(0);
			Row title = sheet.getRow(0);
			Iterator<Cell> iterator=title.cellIterator();
			while (iterator.hasNext()) 
			{
				titleList.add(iterator.next().getStringCellValue());				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return titleList;
	}
	
	/**
	 * 验证导入文件中title是否与模版中的title一样
	 * @param title,tempTitle
	 * @return
	 */
	public static List<Map<String, String>> validateImpTempTitle(Row title,List<String> tempTitle)
	{		
		Map<String, String> resultMap = null;
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		for(int i=0;i<tempTitle.size();i++)
		{
			if(title.getCell(i)==null  ||
					!tempTitle.get(i).trim().equals(title.getCell(i).getStringCellValue().trim()))
			{
				resultMap = new HashMap<String, String>();
				resultMap.put("rows", "第一行");
				resultMap.put("cols", "第" + (i+1) + "列");
				resultMap.put("info", "列头信息不对");
				errorList.add(resultMap);
			}
		}
		return errorList;
	}

	/*
	 * @author zengj
	 * @Description 对errorList进行处理
	 * @Date 2018-6-12 16:40
	 * @Param [errorList, rowNum, cols, message]
	 * @return void
	 */
	public static void addErrorList(List<Map<String, String>> errorList,int rowNum,int cols,String message){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("rows", "第" + (rowNum + 1) + "行");
		resultMap.put("cols", "第" + (cols + 1) + "列");
		resultMap.put("info", message);
		errorList.add(resultMap);
	}

	/*
	 * @author zengj
	 * @Description 获取cell的值
	 * @Date 2018-6-12 16:36
	 * @Param [cell]
	 * @return java.lang.String
	 */
	public static String getStringValueFromCell(Cell cell) {
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat decimalFormat = new DecimalFormat("######.####");
		String cellValue = "";
		if(cell == null) {
			return cellValue;
		}
		else if(cell.getCellType() == Cell.CELL_TYPE_STRING) {
			cellValue = cell.getStringCellValue();
		}else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			if(HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sFormat.format(date);
			}
			else {
				cellValue = decimalFormat.format((cell.getNumericCellValue()));
			}
		}else if(cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			cellValue = "";
		}else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		}else if(cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			cellValue = "";
		}else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			cellValue = cell.getCellFormula().toString();
		}
		return cellValue;
	}
}
