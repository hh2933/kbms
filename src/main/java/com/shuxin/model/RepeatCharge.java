package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 重复收费分组对应表
 *
 */
@TableName("t_repeat_charge_mapping")
public class RepeatCharge implements Serializable{

	private static final long serialVersionUID = -7556942236890026370L;
	
	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 项目编码组
	 */
	@TableField(value = "project_group_code")
	private String projectGroupCode;
	
	/**
	 * 项目编码
	 */
	@TableField(value = "project_code")
	private String projectCode;
	
	/**
	 * 项目名称
	 */
	@TableField(value = "project_name")
	private String projectName;
	
	/**
	 * 项目组别（A：项目编码A组；B：项目编码B组)
	 */
	@TableField(value = "project_type")
	private String projectType;
	
	/**
	 * 提示信息
	 */
	@TableField(validate=FieldStrategy.IGNORED)
	private String prompt;
	
	/**
	 * 备注信息
	 */
	@TableField(validate=FieldStrategy.IGNORED)
	private String remark;
	
	@TableField(value = "create_user")
	private String createUser;
	
	@TableField(value = "create_time")
	private Date createTime;
	
	@TableField(value = "update_user")
	private String updateUser;
	
	@TableField(value = "update_time")
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
