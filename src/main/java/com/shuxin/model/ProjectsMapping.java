package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 项目与项目匹配分组对应表
 *
 */
@TableName("t_projects_mapping")
public class ProjectsMapping implements Serializable{

	private static final long serialVersionUID = -4172490724428660365L;
	
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
	 * 项目编码组
	 */
	@TableField(value = "project_group_code")
	private String projectGroupCode;
	
	/**
	 * 是否查B组历史记录
	 */
	@TableField(value = "is_check_b_project")
	private String isCheckBGroup;
	
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

	public String getProjectGroupCode() {
		return projectGroupCode;
	}

	public void setProjectGroupCode(String projectGroupCode) {
		this.projectGroupCode = projectGroupCode;
	}

	public String getIsCheckBGroup() {
		return isCheckBGroup;
	}

	public void setIsCheckBGroup(String isCheckBGroup) {
		this.isCheckBGroup = isCheckBGroup;
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
