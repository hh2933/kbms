package com.shuxin.controller;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.sql.visitor.functions.Length;
import com.shuxin.commons.base.BaseController;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.JsonUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.model.Dictionary;
import com.shuxin.model.vo.DictionaryListVo;
import com.shuxin.service.IDictionaryService;
import com.shuxin.service.IRuleTableInfoService;

@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController {

	
	@Autowired
	private  IDictionaryService dictionaryService;
	@Autowired
	private  IRuleTableInfoService ruleTableInfoService;
	
	
	@RequestMapping("/manager")
	public String  toRuleTableInfo(HttpServletRequest request,  Model model){
		  model=getMenuId(request, model);
		return "admin/dataDictionary";
	}
	
	
	
	@RequestMapping("/dictionaryDataGrid")
	  @ResponseBody
	public Object findDictionaryDataGrid (Dictionary dictionary, Integer page, Integer rows, String sort, String order){
	     PageInfo pageInfo = new PageInfo(page, rows, sort, order);
	        Map<String, Object> condition = new HashMap<String, Object>();

	        if (StringUtils.isNotBlank(dictionary.getDictType())) {
	            condition.put("dictType", dictionary.getDictType());
	        }
	       /* if (userVo.getOrganizationId() != null) {
	            condition.put("organizationId", userVo.getOrganizationId());
	        }*/
	        if (dictionary.getDictCode() != null) {
	            condition.put("dictCode", dictionary.getDictCode());
	        }
	        if (dictionary.getDictName() != null) {
	            condition.put("dictName", dictionary.getDictName());
	        }
	        pageInfo.setCondition(condition);
	        dictionaryService.findDictionaryDataGrid(pageInfo);
	        return pageInfo;
		
		
	}
	
	
	
	   @RequestMapping("/getType")
	    @ResponseBody
	    public Object getType(String clum) { 
		 List<Dictionary> paramterType=dictionaryService.findDepartType(clum);    
	         String json= JsonUtils.toJson(paramterType);
	        return json;
	        
	    }
	   @RequestMapping("/groupByDictType")
	   @ResponseBody
	   public Object groupByDictType() {
		   List<Dictionary> paramterType=dictionaryService.groupByDictType();	
		   String json= JsonUtils.toJson(paramterType);   
		   return json;
		   
	   }
		
	   @RequestMapping("/selectbyType")
		@ResponseBody
		public Object selectbyType( String type ){
		   List<Dictionary> finddic=dictionaryService.selectByType(type);
         String json= JsonUtils.toJson(finddic);
		   return json;
	     }
	
	
		
		@RequestMapping("/add")
		@ResponseBody
		public Object addDictionary(@RequestBody DictionaryListVo vo){
		//  List<Dictionary> finddic=	 dictionaryService.selectByType(vo);
		ShiroUser user = getShiroUser();
		if (vo.getList().get(0).getId() != null
				&& !vo.getList().get(0).getId().equals("")) {
			if (isrepeat(vo)) {
				return renderError("该字典类型key或name重复");
			}
			 dictionaryService.deleteDictionaryByType(user, vo);
			
			dictionaryService.editDictionary(user, vo);

		} else {
			List<Dictionary> finddic = dictionaryService.selectByType(vo);
			if (finddic.size() > 0) {
				return renderError("该字典类型已存在");
			}
			if (isrepeat(vo)) {
				return renderError("该字典类型key或name重复");
			}
			dictionaryService.editDictionary(user, vo);
		}

		if (StringUtils.isNotBlank(vo.getList().get(0).getId())) {
			return renderSuccess("修改成功");
		} else {
			return renderSuccess("添加成功");
		}

		}
		public boolean isrepeat(DictionaryListVo vo) {
			for (int i = 0; i <vo.getList().size(); i++) {
				String key=vo.getList().get(i).getDictCode();
				String name=vo.getList().get(i).getDictName();
				for (int j = i+1; j < vo.getList().size(); j++) {
					if(key.equals(vo.getList().get(j).getDictCode())||name.equals(vo.getList().get(j).getDictName())){
						return true;
					}
				}
			}
			 return false;
		}
		
		@RequestMapping("/delete")
		@ResponseBody
		public Object deleteDictionary(@RequestBody DictionaryListVo vo){
			
			ShiroUser user=getShiroUser();
			dictionaryService.deleteDictionary(user,vo);
			insertOperationLog(user.getLoginName(),
			"delete","t_dictionary".toUpperCase(),vo.getDictType());			
			return renderSuccess("删除成功");
	
		}
	
	
	

	/*	@GetMapping("/exportExcel")
		@ResponseBody
		public void exportExcel(HttpServletRequest request, HttpServletResponse response,List<String>fieldList)
		{
			InputStream in=null;  
	        OutputStream out=null;
			try { 
			 String templateFileName= Thread.currentThread().getContextClassLoader().getResource("").getPath() + "excleTemplate/templateFileName.xls";  
			 String destFileName= java.net.URLEncoder.encode("测试", "UTF-8")+".xls"; 
		     
		     List<Map<String, String>> staff = new ArrayList<Map<String, String>>(); 
		     Map<String,String> map = new HashMap<String,String>();
		     map.put("name", "Derek");
		     map.put("age", "35");
	        staff.add(map);  
	        map = new HashMap<String,String>();
		     map.put("name", "Elsa");
		     map.put("age", "28");
		     staff.add(map);   
		     map = new HashMap<String,String>();
		     map.put("name", "Oleg");
		     map.put("age", "32");
		     staff.add(map);
		     Map<String,Object> beans = new HashMap<String,Object>();  
		        beans.put("employees", staff); 
		        XLSTransformer transformer = new XLSTransformer();
		          
		        //设置响应  
		        response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);  
		        response.setContentType("application/vnd.ms-excel");  
		        
		         
		            in=new BufferedInputStream(new FileInputStream(templateFileName));  
	            Workbook workbook=transformer.transformXLS(in, beans);  
		            out=response.getOutputStream();  
		            //将内容写入输出流并把缓存的内容全部发出去  
		            workbook.write(out);  
		            out.flush();  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  finally {  
	            if (in!=null){try {in.close();} catch (IOException e) {}}  
		            if (out!=null){try {out.close();} catch (IOException e) {}}  
		        }
		}
		*/
	
	
}
