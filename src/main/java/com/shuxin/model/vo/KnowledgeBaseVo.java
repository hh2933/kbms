package com.shuxin.model.vo;

import java.util.List;

public class KnowledgeBaseVo {

	private Integer page;
    private Integer rows; 
    private String sort; 
    private String order;
    
    private List<String> columnName;
    
    private List<String> columnValue;
    
    private List<String> selectColumns;
    
    private List<String> thNames;
    
    private String tableName;
    
    private String menuName;
    
    private String id;
    
    private List<EditKnowledgeBaseVo> editKnowledgeBaseList;
    
    private String loginName;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<String> getColumnName() {
		return columnName;
	}

	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}

	public List<String> getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(List<String> columnValue) {
		this.columnValue = columnValue;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getSelectColumns() {
		return selectColumns;
	}

	public void setSelectColumns(List<String> selectColumns) {
		this.selectColumns = selectColumns;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<EditKnowledgeBaseVo> getEditKnowledgeBaseList() {
		return editKnowledgeBaseList;
	}

	public void setEditKnowledgeBaseList(List<EditKnowledgeBaseVo> editKnowledgeBaseList) {
		this.editKnowledgeBaseList = editKnowledgeBaseList;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<String> getThNames() {
		return thNames;
	}

	public void setThNames(List<String> thNames) {
		this.thNames = thNames;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
}
