package com.shuxin.commons.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 上传参数判断
 */
public class DataValidationUtils {

	
	/**
	 * 判断字符串是否数字，包括小数点
	 * @author summer
	 * @param id
	 * @return
	 */
	public static boolean  isNumber( String id){
	    Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");  
         Matcher isNum = pattern.matcher(id);
         if( !isNum.matches() ){
             return false;
         }
    
         return true;
	}	

	/**
	 * 判断是否包含中文
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str)
	{
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
	}

	/**
	 * 判断字符串是否数字，下划线，英文组
	 * @author summer
	 * @param id
	 * @return
	 */
	public static boolean  isCode( String id){
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z_]+$"); 
		Matcher isNum = pattern.matcher(id);
		if( !isNum.matches() ){
			return false;
		}
		
		return true;
	}
	

	public static  List<Map<String, String>> importDataValidate(List<Map<String, String>> tableinfo, Workbook workbook){
		 
		List<String> clumType = new ArrayList<String>();
		Sheet sheet = workbook.getSheetAt(0);
		String p = sheet.getSheetName();
		Row title = sheet.getRow(0);
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < tableinfo.size(); i++) {
			// HSSFCell c = null;
			String c = title.getCell(i).getStringCellValue();
			String oneClum = null;	
				if (tableinfo.get(i).get("TH_NAME").toString().trim().equals(c)) {
					oneClum = tableinfo.get(i).get("COLUMN_TYPE").toString();			
				}	
			if (oneClum != null) {
				clumType.add(oneClum);
			}else if (oneClum == null) {
				// return "该模板第"+i+"列头不正确";
				Map<String, String> resultmap = new HashMap<String, String>();
				resultmap.put("rows", "第一行");
				resultmap.put("cols", "第" + i + "列");
				resultmap.put("info", "列头信息不对");
				errorList.add(resultmap);
				return errorList;
			}

		}

		List<String> unique = new ArrayList<String>();
		List<Integer> uniqueIndex = new ArrayList<Integer>();
		List<String> columnName = new ArrayList<String>();
		for (int j = 0; j < tableinfo.size(); j++) {
			if (tableinfo.get(j).get("IS_UNIQUE").toString().equals("1")) {
				unique.add(tableinfo.get(j).get("COLUMN_NAME").toString());
				uniqueIndex.add(j);
			}
			columnName.add(tableinfo.get(j).get("COLUMN_NAME").toString());
		}
		for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {

			Row rows = sheet.getRow(rowNum);
			if (isRowNotEmpty(rows)) {
				for (int i = 0; i < clumType.size(); i++) {
					Cell cell = rows.getCell(i);
					String celltext = null;
					if (cell != null) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						celltext = cell.getStringCellValue();
					}

					if (celltext == null || celltext.equals("")) {
						for (int j = 0; j < uniqueIndex.size(); j++) {
							if (i == j) {
								Map<String, String> resultmap = new HashMap<String, String>();
								int cols = i + 1;
								resultmap.put("rows", "第" + rowNum + "行");
								resultmap.put("cols", "第" + cols + "列");
								resultmap.put("info", "必填项为空");
								errorList.add(resultmap);
							}
						}

					} else if (cell != null && !celltext.equals("")) {
						if (clumType.get(i).equals("number")) {

							if (!DataValidationUtils.isNumber(celltext)) {
								// return "该模板第"+rowNum+"行第"+i+"列数据类型不正确";
								Map<String, String> resultmap = new HashMap<String, String>();
								int cols = i + 1;
								resultmap.put("rows", "第" + rowNum + "行");
								resultmap.put("cols", "第" + cols + "列");
								resultmap.put("info", "数据类型不正确");
								errorList.add(resultmap);
							}

						} else {
							if (cell.getStringCellValue().length() > 50) {
								// return "该模板第"+rowNum+"行第"+i+"列字符过长";
								Map<String, String> resultmap = new HashMap<String, String>();
								int cols = i + 1;
								resultmap.put("rows", "第" + rowNum + "行");
								resultmap.put("cols", "第" + cols + "列");
								resultmap.put("info", "字符不能超过50个");
								errorList.add(resultmap);
							}
						}
					}

				}
			}
		}
		
		
		return errorList;
		
	}

	/*
	 * @author zengj
	 * @Description 判断row是否为空
	 * @Date 2018-6-8 17:02
	 * @Param [row]
	 * @return boolean
	 */
	public static boolean isRowNotEmpty(Row row) {
		if (row != null){
			for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
				Cell cell = row.getCell(c);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK){
					return true;
				}
			}
		}
		return false;
	}


}
