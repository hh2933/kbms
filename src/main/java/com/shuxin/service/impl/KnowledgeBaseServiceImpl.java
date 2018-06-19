package com.shuxin.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import com.shuxin.commons.utils.DataValidationUtils;
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
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.Charsets;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.mapper.KnowledgeBaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.vo.EditKnowledgeBaseVo;
import com.shuxin.model.vo.KnowledgeBaseVo;
import com.shuxin.service.IKnowledgeBaseService;

@Service
public class KnowledgeBaseServiceImpl extends ServiceImpl<KnowledgeBaseMapper, CommonModel> implements IKnowledgeBaseService {

	private   Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private KnowledgeBaseMapper knowledgeBaseMapper;
	
	@Override
	public void selectKnowledgeBaseVoPage(PageInfo pageInfo, KnowledgeBaseVo knowledgeBaseVo) {
		Page page = new Page(pageInfo.getNowpage(), pageInfo.getSize());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", getSelectSql(knowledgeBaseVo));
		List<Map<String,Object>> list = knowledgeBaseMapper.selectKnowledgeBaseInfo(page, params);
        pageInfo.setRows(list);
        pageInfo.setTotal(page.getTotal());
	}

	/**
	 * 组装查询sql
	 * @param knowledgeBaseVo
	 * @return
	 */
	private String getSelectSql(KnowledgeBaseVo knowledgeBaseVo)
	{
		StringBuffer sql = new StringBuffer("select id");
		for(String selectColumn:knowledgeBaseVo.getSelectColumns())
		{
			sql.append(",").append(selectColumn);
		}
		sql.append(" from ").append(knowledgeBaseVo.getTableName());
		
		for(int i=0;i<knowledgeBaseVo.getColumnName().size();i++)
		{
			if(i==0)
			{
				sql.append(" where")
				.append(" ");
			}
			else
			{
				sql.append(" and")
				.append(" ");
			}

			sql.append(knowledgeBaseVo.getColumnName().get(i))
			.append(" like '%")
			.append(knowledgeBaseVo.getColumnValue().get(i))
			.append("%'");
		}
		sql.append(" order by update_time desc");
		return sql.toString();
	}

	@Override
	public void editKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo) {
		
		if(StringUtils.isEmpty(knowledgeBaseVo.getId()))
		{
			addKnowledgeBase(knowledgeBaseVo);
		}
		else
		{
			updateKnowledgeBase(knowledgeBaseVo);
		}
	}
	
	private void addKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo)
	{
		StringBuffer columnName = new StringBuffer("id");
		StringBuffer columnValue = new StringBuffer("'"+UUID.randomUUID().toString().replace("-", "")+"'");
		for(EditKnowledgeBaseVo editKnowledgeBaseVo:knowledgeBaseVo.getEditKnowledgeBaseList())
		{				
			columnName.append(",")
			.append(editKnowledgeBaseVo.getColumnName());
			columnValue.append(",")
			.append("'")
			.append(editKnowledgeBaseVo.getColumnValue())
			.append("'");
		}
		columnName.append(",create_time,create_user,update_time,update_user");
		columnValue.append(",sysdate,'").append(knowledgeBaseVo.getLoginName())
		.append("',sysdate,'").append(knowledgeBaseVo.getLoginName()).append("'");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", knowledgeBaseVo.getTableName());
		params.put("columnName", columnName.toString());
		params.put("columnValue", columnValue.toString());		
		knowledgeBaseMapper.addKnowledgeBase(params);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		params.put("optType", "添加");
		list.add(params);		
		knowledgeBaseMapper.addKnowledgeBaseOpt(list);
	}
	
	private void updateKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo)
	{
		List<String> list =new ArrayList<String>();
		list.add(knowledgeBaseVo.getId());
		addKnowledgeBaseHistory(knowledgeBaseVo.getTableName(),list,knowledgeBaseVo.getLoginName(),"更新");
		StringBuffer columnName = new StringBuffer();
		for(EditKnowledgeBaseVo editKnowledgeBaseVo:knowledgeBaseVo.getEditKnowledgeBaseList())
		{
			if(!StringUtils.isEmpty(columnName.toString()))
			{
				columnName.append(",");
			}
			columnName.append(editKnowledgeBaseVo.getColumnName())
			.append("='").append(editKnowledgeBaseVo.getColumnValue())
			.append("'");			
		}
		columnName.append(",update_time=sysdate,update_user='")
		.append(knowledgeBaseVo.getLoginName()).append("'");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", knowledgeBaseVo.getTableName());
		params.put("columnName", columnName.toString());
		params.put("id", knowledgeBaseVo.getId());
		knowledgeBaseMapper.updateKnowledgeBase(params);
	}

	@Override
	public int selectExistKnowledgeBase(KnowledgeBaseVo knowledgeBaseVo) {
		StringBuffer condition = new StringBuffer();
		int count=0;
		for(EditKnowledgeBaseVo editKnowledgeBaseVo:knowledgeBaseVo.getEditKnowledgeBaseList())
		{
			if("1".equals(editKnowledgeBaseVo.getIsUnique()))
			{
				if(!StringUtils.isEmpty(condition.toString()))
				{
					condition.append(" and ");
				}
				condition.append(editKnowledgeBaseVo.getColumnName())
				.append("='")
				.append(editKnowledgeBaseVo.getColumnValue())
				.append("'");
			}
		}
		
		if(StringUtils.isEmpty(condition.toString()))
		{
			return count;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("condition", condition.toString());
		params.put("tableName", knowledgeBaseVo.getTableName());
		params.put("id", knowledgeBaseVo.getId());
		count = knowledgeBaseMapper.selectExistKnowledgeBase(params);
		return count;
	}

	@Override
	public void delKnowledgeBase(List<String> list, String tableName,String loginName) {
		addKnowledgeBaseHistory(tableName,list,loginName,"删除");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put("list", list);
		knowledgeBaseMapper.delKnowledgeBase(params);
	}
	
	

	@Override
	public String importData(List<Map<String, String>> tableinfo,
   Workbook workbook, ShiroUser user) {  
		List<String> clumType = new ArrayList<String>();
		Sheet sheet = workbook.getSheetAt(0);
		String p = sheet.getSheetName();
		Row title = sheet.getRow(0);
		List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < tableinfo.size(); i++) {
			// HSSFCell c = null;
			String c = title.getCell(i).getStringCellValue();
			String oneClum = null;
			for (int j = 0; j < tableinfo.size(); j++) {
				if (tableinfo.get(j).get("TH_NAME").toString().trim().equals(c)) {
					oneClum = tableinfo.get(j).get("COLUMN_TYPE").toString();
					break;
				}
			}
			if (oneClum != null) {
				clumType.add(oneClum);
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
		//将所有的marMERGE INTO 的字段提前拼出，，然后随意组合
			StringBuffer setsql = new StringBuffer("");
			StringBuffer conditonsql = new StringBuffer("");
			StringBuffer insertsql = new StringBuffer("id");
			StringBuffer insertsql2 =new StringBuffer("id"); 
			StringBuffer optinsertsql = new StringBuffer("");
			StringBuffer valuessql = new StringBuffer("");
			StringBuffer valuessql2 = new StringBuffer("");
			StringBuffer wheresql = new StringBuffer(" where ");
			StringBuffer selectql = new StringBuffer(" select ");
			//用于操作表
			List<List<Map<String, Object>>> alloptlist=new ArrayList<List<Map<String, Object>>>();
			List<Map<String, Object>> optlist = new ArrayList<Map<String, Object>>();
			int f = 0;
			for (int i = 0; i < columnName.size(); i++) {

				boolean isunique = false;
				for (int j = 0; j < unique.size(); j++) {
					String r = columnName.get(i);
					if (columnName.get(i).equals(unique.get(j))) {
						isunique = true;
						break;
					}
				}
				if (!isunique) {
					if (f != 0) {
						setsql.append(" , ");

					}
					setsql.append("t1." + columnName.get(i) + "=t2."
							+ columnName.get(i) + "");
					f = 1;
				}
				insertsql.append("," + columnName.get(i));
				insertsql2.append("," + columnName.get(i));
				
				valuessql.append(", t2." + columnName.get(i));
			}
			for (int i = 0; i < unique.size(); i++) {
				if (i != 0) {
					conditonsql.append(" and ");
				}
				conditonsql.append("t1." + unique.get(i) + "=t2."
						+ unique.get(i) + "");
			}
			setsql.append(", t1.update_time= sysdate");
			setsql.append(", t1.update_user= '" + user.getLoginName() + "'");
			
			
			insertsql.append(",CREATE_TIME");
			insertsql.append(",CREATE_user");
			insertsql.append(",update_time");
			insertsql.append(",update_user");
			valuessql.append(", sysdate");
			valuessql.append(", '" + user.getLoginName() + "'");
			valuessql.append(", sysdate");
			valuessql.append(",  '" + user.getLoginName() + "'");
			List<String> listsql = new ArrayList<String>();
			StringBuffer seletsql = new StringBuffer("begin ");
			Row rows = null;
			int num = 0;
			String uuid = "";
			StringBuffer rowsql = new StringBuffer("");	
			StringBuffer rowoptsql = new StringBuffer("");	
			StringBuffer rowoptsql2 = new StringBuffer("");	
			StringBuffer insteroptsql = new StringBuffer("");	
			StringBuffer datasql= new StringBuffer("");	
			StringBuffer datasql2= new StringBuffer("");	
			Map<String, Object> params =null;
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				int flagisUnique = 0;
				rows = sheet.getRow(rowNum);
				
				if (DataValidationUtils.isRowNotEmpty(rows)) {
					rowsql = new StringBuffer(" MERGE INTO "
							+ tableinfo.get(0).get("TABLE_NAME").toString()
							+ " t1 USING (SELECT ");
					rowoptsql = new StringBuffer(" insert into "
							+ tableinfo.get(0).get("TABLE_NAME").toString()
							+ "_opt   (   "+insertsql+",opt_type ) select  ");  
					
					datasql=new StringBuffer("");
					datasql2=new StringBuffer("");
					insteroptsql.append("insert into "+tableinfo.get(0).get("TABLE_NAME").toString()+"_opt ("+insertsql+",opt_type ) select "+insertsql2+",sysdate create_time,'" + user.getLoginName() + "' create_user,sysdate update_time,'" + user.getLoginName() + "' update_user , '导入更新' opt_type from "+tableinfo.get(0).get("TABLE_NAME").toString());
					  
					for (int i = 0; i < columnName.size(); i++) {
						String celltext = "";
						if (rows.getCell(i) != null) {
							rows.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
							celltext = Charsets.filterSpecialChar(rows.getCell(i).getStringCellValue());
						}
						for (int k = 0; k < uniqueIndex.size(); k++) {
							if (i == uniqueIndex.get(k)) {
								if (celltext.equals("")) {
									flagisUnique = 1;
									break;
								}
							
							}
						}

						for (int j = 0; j < unique.size(); j++) {
							if(unique.get(j).equals(columnName.get(i))){
								if(j!=0){
									wheresql.append(" and ");
								}
								wheresql.append(unique.get(j)+"= '"+celltext+"'");
							}
						}
						
						/*if (flagisUnique == 1) {
							break;
						}*/
						if (i != 0) {
							rowsql.append(" , ");
							rowoptsql2.append(" , ");
						}
						datasql.append(" , ");
						rowsql.append("'" + celltext + "' as "
								+ columnName.get(i));
						rowoptsql2.append("'" + celltext + "' as "
								+ columnName.get(i));
						datasql.append("'" + celltext + "' ");
					}
					/*if (flagisUnique == 1) {
						continue;
					}*/
					insteroptsql.append(wheresql+";");
					seletsql.append(insteroptsql);
					
					
					uuid = UUID.randomUUID().toString().replace("-", "").trim();
					valuessql2.append("'" + uuid + "'");
					datasql2.append("'" + uuid + "'");
					datasql.append(", sysdate");
					datasql.append(", '" + user.getLoginName() + "'");
					datasql.append(", sysdate");
					datasql.append(",  '" + user.getLoginName() + "'");
					valuessql2.append(valuessql);
					seletsql.append(rowoptsql);
					seletsql.append("'" +uuid+ "'"+insertsql2+",sysdate create_time,'" + user.getLoginName() + "' create_user,sysdate update_time,'" + user.getLoginName() + "' update_user , '导入新增' opt_type   from (select"+rowoptsql2+"   from dual) t2 where not exists	(select 1 from "+ tableinfo.get(0).get("TABLE_NAME").toString()
							+ "  t1 where    "+ conditonsql+"); "		 );
					seletsql.append(rowsql);
					seletsql.append(" FROM dual) t2 on (" + conditonsql
							+ ") when MATCHED THEN   UPDATE SET " + setsql
							+ " WHEN NOT MATCHED THEN" + "  INSERT ( "
							+ insertsql + ") values (" + valuessql2 + "); ");				
					valuessql2 = new StringBuffer("");
					/*params= new HashMap<String, Object>();
					params.put("tableName", tableinfo.get(0).get("TABLE_NAME").toString());
					params.put("columnName", insertsql);
					params.put("columnValue", datasql2.append(datasql));		
					params.put("optType", "import");
					optlist.add(params);*/
					insteroptsql=new StringBuffer("");
					rowoptsql2=new StringBuffer("");
					wheresql=new StringBuffer(" where ");
					num++;
					if (num % 100 == 0) {
						seletsql.append(" end;");
						listsql.add(seletsql.toString());
						seletsql = new StringBuffer("begin ");
						/*alloptlist.add(optlist);
						optlist=null;*/
					}
				}
			}
			if (num % 100 != 0) {
				seletsql.append(" end;");
			}
		/*	if(optlist!=null){
				alloptlist.add(optlist);
			}*/
			listsql.add(seletsql.toString());		
			for (int i = 0; i < listsql.size(); i++) {
				Map<String, Object> mapCondition = new HashMap<String, Object>();
				mapCondition.put("sql", listsql.get(i));
				System.out.println(listsql.get(i));
				knowledgeBaseMapper.daoruData(mapCondition);
			}		
			/*for (int i = 0; i < alloptlist.size(); i++) {
				knowledgeBaseMapper.addKnowledgeBaseOpt(alloptlist.get(i));
			}*/
			return "导入成功";
		}
	
	
	private void addKnowledgeBaseHistory(String tableName,List<String> list,String loginName,String optType)
	{
		List<String> columnList = knowledgeBaseMapper.selectColumnOfKnowledgeBase(tableName);
		StringBuffer columnName = new StringBuffer();
		for(String column:columnList)
		{
			columnName.append(column).append(",");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put("columnName", columnName.toString());
		params.put("list", list);
		params.put("optType", optType);
		params.put("loginName", loginName);
		knowledgeBaseMapper.addKnowledgeBaseHistory(params);
	}

	@Override
	public void exportKnowledgeBaseHistory(KnowledgeBaseVo knowledgeBaseVo, HttpServletResponse response) {
		
		String selectSql = getHistorySql(knowledgeBaseVo);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sql", selectSql);
		
		List<Map<String, String>> list=knowledgeBaseMapper.selectKnowledgeBaseInfo(params);
		OutputStream fOut = null;
		try {
			String fileName = java.net.URLEncoder.encode(knowledgeBaseVo.getMenuName()+"历史记录", "UTF-8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-disposition", "attachment;filename="
					+ fileName + ".xls");
			
			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet();
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
			knowledgeBaseVo.getThNames().add("操作类型");
			knowledgeBaseVo.getThNames().add("操作人");
			knowledgeBaseVo.getThNames().add("操作时间");
			knowledgeBaseVo.getSelectColumns().add("opt_type");			
			knowledgeBaseVo.getSelectColumns().add("create_user");
			knowledgeBaseVo.getSelectColumns().add("create_time");
			for (int j = 0; j < knowledgeBaseVo.getThNames().size(); j++) {

				Cell cell = row.createCell((int) j);// 创建一列
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(knowledgeBaseVo.getThNames().get(j));
				cell.setCellStyle(style);
				sheet.autoSizeColumn(j);
			}	
			Map<String, String> map =null;
			Cell cell = null;
			for(int i=0;i<list.size();i++)
			{
				row = sheet.createRow(i+1);// 创建一行
				row.setHeightInPoints((short) 30);
				map = list.get(i);
				for(int j=0;j<knowledgeBaseVo.getSelectColumns().size();j++)
				{
					cell = row.createCell((int) j);// 创建一列
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(map.get(knowledgeBaseVo.getSelectColumns().get(j).toUpperCase()));
					//cell.setCellStyle(style);
					sheet.autoSizeColumn(j);
				}
			}
			fOut = response.getOutputStream();
			workbook.write(fOut);
		}
		catch (Exception e) {
			
			logger.error(e.getMessage(), e);
		} 
		finally 
		{
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private String getHistorySql(KnowledgeBaseVo knowledgeBaseVo)
	{
		StringBuffer selectSql = new StringBuffer("select ");
		for(String column:knowledgeBaseVo.getSelectColumns())		
		{
			selectSql.append("to_char(");
			selectSql.append(column).append(") ");
			selectSql.append(column).append(",");
		}
		selectSql.append("to_char(opt_type) opt_type,");
		selectSql.append("to_char(create_time,'yyyy-mm-dd hh24:mi:ss') create_time,");
		selectSql.append("to_char(create_user) create_user");
		selectSql.append(" from ");
		selectSql.append(knowledgeBaseVo.getTableName()+"_opt");
		selectSql.append(" order by create_time desc");
		
		return selectSql.toString();
	}
	
}
