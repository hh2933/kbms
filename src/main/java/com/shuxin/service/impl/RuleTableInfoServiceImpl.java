package com.shuxin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shuxin.commons.shiro.ShiroUser;
import com.shuxin.commons.utils.Charsets;
import com.shuxin.commons.utils.DataValidationUtils;
import com.shuxin.commons.utils.PageInfo;
import com.shuxin.mapper.DictionaryMapper;
import com.shuxin.mapper.MenuMapper;
import com.shuxin.mapper.RoleMapper;
import com.shuxin.mapper.RoleMenuMapper;
import com.shuxin.mapper.RuleColumnInfoMapper;
import com.shuxin.mapper.RuleTableInfoMapper;
import com.shuxin.model.Menu;
import com.shuxin.model.Role;
import com.shuxin.model.RoleMenu;
import com.shuxin.model.RuleColumnInfo;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.service.IRuleTableInfoService;

@Service
public class RuleTableInfoServiceImpl  extends ServiceImpl<RuleTableInfoMapper, RuleTableInfo> implements IRuleTableInfoService{
    @Autowired
    private  RuleTableInfoMapper ruleTableInfoMapper;
	
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private RuleColumnInfoMapper ruleColumnInfoMapper;
    
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private DictionaryMapper dictionaryMapper;
	
	@Override
	public void selectRuleTable(PageInfo pageInfo) {
		// TODO Auto-generated method stub
		 	Page<RuleTableInfo> page = new Page<RuleTableInfo>(pageInfo.getNowpage(), pageInfo.getSize());
	        page.setOrderByField(pageInfo.getSort());
	        page.setAsc(pageInfo.getOrder().equalsIgnoreCase("asc"));
	        List<RuleTableInfo> list = ruleTableInfoMapper.selectRuleTableInfoDataGraid(page, pageInfo.getCondition());
	        pageInfo.setRows(list);
	        pageInfo.setTotal(page.getTotal());
	}


	@Override
	public void doRuleTable(RuleTableInfo table, List<RuleColumnInfo> cols) {
		// TODO Auto-generated method stub
		String tableName=table.getTableName();
		StringBuffer s=new StringBuffer("");
		for (int i=0;i<cols.size();i++) {
			RuleColumnInfo col=cols.get(i);
			if (i==cols.size()-1) {
				s.append(col.getColumnName()).append(" ").append(col.getColumnType());
				break;
			}
			s.append(col.getColumnName()).append(" ").append(col.getColumnType()).append(",");
		}
		String inner= s.toString();
		ruleTableInfoMapper.doRuleTable(tableName, inner);
		ruleTableInfoMapper.doOperationTable(tableName, inner);
	}


	@Override
	public Integer checkTableName(String tableName) {
		// TODO Auto-generated method stub
		tableName=tableName.toUpperCase();
		return ruleTableInfoMapper.checkTableName(tableName);
	}


	@Override
	public String createTable (RuleTableInfo ruleTableInfo,ShiroUser shiroUser) throws Exception{
		// TODO Auto-generated method stub
		Menu menu= domenu(ruleTableInfo);
		if(menu==null){
			return "该规则名已存在，请确认后在输入";
		}
		List<RuleColumnInfo> cols=doColumns(ruleTableInfo, shiroUser, menu);
		try {
			RoleMenu roleMenu=new RoleMenu();
			String roleId=doRole(shiroUser);
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menu.getId());
			roleMenuMapper.insert(roleMenu);
			doRuleTable(ruleTableInfo, cols);
			return "新增规则成功";
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	private Menu domenu(RuleTableInfo ruleTableInfo){
		String menuName=ruleTableInfo.getMenuName();
		if(menuMapper.checkMenuName(menuName)>0){
			return null;
		}
		//如果表名重复就给这个表后面加上_1,_2...
		int appendIndex=1;
		String transTableName= "t_"+Charsets.getPinYinHeadChar(menuName);
		String resultTableName=transTableName;
		while(true){
			if(this.checkTableName(resultTableName)>0){
				//大于零说明存在重复
				resultTableName=transTableName+"_"+appendIndex;
				appendIndex++;
			}else{
				break;
			}
		}
		//将规则名变为缩写变为数据库中的表名
		ruleTableInfo.setTableName(resultTableName);
		//在菜单表中增加字段
		Menu menu=new Menu();
		menu.setName(menuName);
		menu.setUrl("/knowledgeBase/manager");
		menu.setDescription("用户新增规则菜单");
		menu.setOpenMode("ajax");
		menu.setPid("2");
		menu.setSeq(20);
		menu.setStatus(1);
		menu.setLevels(2);
		menu.setIshaschildren("0");
		//将这个新的菜单（menu）加入数据库，加入完毕后，menu对象中的id自动赋值了这次新增的ID
		menuMapper.insert(menu);
		return menu;
	}
	private String doRole(ShiroUser shiroUser){
		Set<String> s=shiroUser.getRoles();
		if(s.size()==0){
			return "err";
		}
		String roleName="";
		for (String string : s) {
			roleName=string;
		}
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("name", roleName);
		List<Role> list= roleMapper.selectByMap(map);
		if(list.size()==0){
			return "err";
		}
		return	list.get(0).getId();
	}
	
	private List<RuleColumnInfo> doColumns(RuleTableInfo ruleTableInfo,ShiroUser shiroUser,Menu menu){
		Date date=new Date();
		String menuId= menu.getId();//得到新增的这一条菜单的ID
		String createUser= shiroUser.getLoginName();//创建人
		ruleTableInfo.setMenuId(menuId);
		ruleTableInfo.setCreateUser(createUser);
		ruleTableInfo.setCreateTime(date);//创建时间
		ruleTableInfo.setUpdateUser(createUser);//创建时，创建人和更新人一样
		ruleTableInfo.setUpdateTime(date);
		//菜单添加完毕后，在存放表信息的表里面添加表信息
		ruleTableInfoMapper.insert(ruleTableInfo);
		//将menu的ID返回给页面，进行ajax方法读取页面中datagrid中的所有列保存作为新表的列名
		List<RuleColumnInfo> cols= ruleTableInfo.getRows();
		//临时列名仓库，为了避免列名重复
		List<String> colNames=new ArrayList<String>();

		for (int i=0;i<cols.size();i++) {
			RuleColumnInfo col = cols.get(i);
			//往重复列的后面添加的数字，初始值为1
			int appendIndex=1;
			//得到表头的中文
			String thName= col.getThName();
			//得到转换后的表头(英文)
			String transThName=Charsets.getPinYinHeadChar(thName);
			//首先核对列名仓库中是否已经有了该列名
			String resultThName=transThName;
			while(true){
				if(Collections.frequency(colNames, resultThName)>0){
					//大于0表示有重复值，将改列之后加_1,_2...
					resultThName= transThName+"_"+appendIndex;
					appendIndex++;
				}else{
					//等于0表示没有重复，将被修改完的列名存放入列名数组，跳出循环
					colNames.add(resultThName);
					break;
				}
			}
			//将列名的小写拼音赋值到真实列名中
			col.setColumnName(resultThName);
			//createTime
			col.setCreateTime(date);
			//createUser
			col.setCreateUser(createUser);
			//updateTime
			col.setUpdateTime(date);
			//updateUser
			col.setUpdateUser(createUser);
			//isFilter
			col.setIsFilter(col.getIsFilter().equals("是")?"1":"0");
			//isUnique
			col.setIsUnique(col.getIsUnique().equals("是")?"1":"0");
			//menuId
			col.setMenuId(ruleTableInfo.getMenuId());
			//tableId
			col.setTableId(ruleTableInfo.getId());
			//orderSequence
			col.setOrderSeq(i);
			//insert
			ruleColumnInfoMapper.insert(col);
		}
		return cols;
	}


	@Override
	public String createTableUpdate(RuleTableInfo ruleTableInfo, ShiroUser shiroUser){
		// TODO Auto-generated method stub
		List<RuleColumnInfo> list = ruleTableInfo.getRows();
		Date date = new Date();
		for (int i=0;i<list.size();i++) {
			RuleColumnInfo col=list.get(i);
			col.setUpdateUser(shiroUser.getLoginName());
			col.setUpdateTime(date);
			col.setIsFilter(col.getIsFilter().equals("是") ? "1" : "0");
			col.setIsUnique(col.getIsUnique().equals("是") ? "1" : "0");
			col.setOrderSeq(i);
			ruleColumnInfoMapper.updateById(col);
		}
		ruleTableInfo.setUpdateTime(date);
		ruleTableInfo.setUpdateUser(shiroUser.getLoginName());
		if(ruleTableInfo.getIsDetail().equals("0")){
			ruleTableInfo.setDatailType("");
		}
		this.updateById(ruleTableInfo);
		Menu menu = new Menu();
		menu.setId(ruleTableInfo.getMenuId());
		menu.setStatus(Integer.parseInt(ruleTableInfo.getIsUsed()));
		menu.setName(ruleTableInfo.getMenuName());
		menuMapper.updateById(menu);
		return "修改规则成功";
	}
	
	@Override
	public List<Map<String, String>> selectRuleTableInfoForMenuId(String menuId) {
		
		return ruleTableInfoMapper.selectRuleTableInfoForMenuId(menuId);
	}



}
