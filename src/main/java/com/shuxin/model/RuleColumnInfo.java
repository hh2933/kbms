package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("T_RULE_COLUMN_INFO")
public class RuleColumnInfo  implements Serializable{
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	@TableField(exist = false)
	private String columnTypeShow;
	@TableField(exist = false)
	private String delbtn;
	@TableField(exist = false)
	private String htmlInputShow;
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	
	/**表ID */
	@TableField(value = "TABLE_ID")
	private String tableId;
	
	/**菜单ID */
	@TableField(value = "MENU_ID")
	private String menuId;
	
	/**表ID */
	@TableField(value = "COLUMN_NAME")
	private String columnName;
	/**表ID */
	@TableField(value = "TH_NAME")
	private String thName;
	/**表ID */
	@TableField(value = "HTML_INPUT")
	private String htmlInput;
	/**表ID */
	@TableField(value = "DICT_TYPE")
	private String dictType;
	/**表ID */
	@TableField(value = "IS_UNIQUE")
	private String isUnique;
	/**表ID */
	@TableField(value = "IS_FILTER")
	private String isFilter;
	/**表ID */
	@TableField(value = "CREATE_TIME")
	private Date createTime;
	/**表ID */
	@TableField(value = "CREATE_USER")
	private String createUser;
	/**表ID */
	@TableField(value = "UPDATE_TIME")
	private Date updateTime;
	
	@TableField(value = "ORDER_SEQ")
	private Integer orderSeq;
	/**表ID */
	@TableField(value = "COLUMN_TYPE")
	private String columnType;
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getThName() {
		return thName;
	}
	public void setThName(String thName) {
		this.thName = thName;
	}
	public String getHtmlInput() {
		return htmlInput;
	}
	public void setHtmlInput(String htmlInput) {
		this.htmlInput = htmlInput;
	}
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	public String getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}
	public String getIsFilter() {
		return isFilter;
	}
	public void setIsFilter(String isFilter) {
		this.isFilter = isFilter;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getColumnTypeShow() {
		return columnTypeShow;
	}
	public void setColumnTypeShow(String columnTypeShow) {
		this.columnTypeShow = columnTypeShow;
	}
	public String getHtmlInputShow() {
		return htmlInputShow;
	}
	public void setHtmlInputShow(String htmlInputShow) {
		this.htmlInputShow = htmlInputShow;
	}

	/**表ID */
	@TableField(value = "UPDATE_USER")
	private String updateUser;
	public String getDelbtn() {
		return delbtn;
	}
	public void setDelbtn(String delbtn) {
		this.delbtn = delbtn;
	}
	@TableField(exist = false)
	private String dictTypeShow;
	public String getDictTypeShow() {
		return dictTypeShow;
	}
	public void setDictTypeShow(String dictTypeShow) {
		this.dictTypeShow = dictTypeShow;
	}
	public Integer getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(Integer orderSeq) {
		this.orderSeq = orderSeq;
	}
	
}
