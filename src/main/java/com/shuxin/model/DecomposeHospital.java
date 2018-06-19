package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 分解住院_项目对应表
 *
 */
@TableName("t_decompose_mapping")
public class DecomposeHospital implements Serializable{

	private static final long serialVersionUID = -8176239193383184476L;
	
	@TableId(type = IdType.UUID)
	private String id;
	
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
	 * 单位
	 */
	private String unit;
	
	/**
	 * 项目类型（1：ICU；2：手术材料项目）
	 */
	@TableField(value = "project_type")
	private String projectType;
	
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
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
