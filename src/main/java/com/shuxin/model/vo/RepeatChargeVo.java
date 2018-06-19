package com.shuxin.model.vo;

import java.util.Date;

/**
 * 重复收费分组对应表
 *
 */
public class RepeatChargeVo{
	
	
	private String id;
	
	/**
	 * 项目编码组
	 */
	private String projectGroupCode;
	
	/**
	 * 项目编码
	 */
	private String projectCode;
	
	/**
	 * 项目名称
	 */
	private String projectName;
	
	/**
	 * 项目组别（A：项目编码A组；B：项目编码B组)
	 */
	private String projectType;
	
	/**
	 * 提示信息
	 */
	private String prompt;
	
	/**
	 * 备注信息
	 */
	private String remark;
	

	private String createUser;
	

	private Date createTime;
	

	private String updateUser;
	

	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectGroupCode() {
		return projectGroupCode;
	}

	public void setProjectGroupCode(String projectGroupCode) {
		this.projectGroupCode = projectGroupCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


}
