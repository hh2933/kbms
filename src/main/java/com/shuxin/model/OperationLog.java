package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_operation_log")
public class OperationLog implements Serializable{

	private static final long serialVersionUID = -1376510407733253364L;

	/**
	 * 用户名
	 */
	@TableField(value = "user_name")
	private String userName;
	
	/**
	 * 操作类型
	 */
	@TableField(value = "operation_type")
	private String operationType;
	
	/**
	 * 表名
	 */
	@TableField(value = "operation_table")
	private String operationTable;
	
	/**
	 * 操作内容
	 */
	@TableField(value = "operation_content")
	private String operationContent;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperationTable() {
		return operationTable;
	}

	public void setOperationTable(String operationTable) {
		this.operationTable = operationTable;
	}

	public String getOperationContent() {
		return operationContent;
	}

	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}
	

}
