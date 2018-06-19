package com.shuxin.model;
import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.shuxin.commons.utils.JsonUtils;

/**
 *
 * 角色
 *
 */
@TableName("T_ROLE")
public class Role implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;

	/** 角色名 */
	private String name;



	/** 简介 */
	private String description;

	/** 状态 */
	private Integer status;
	
	//创建时间
	@TableField(value = "create_time")
	private Date createTime;
	
	//创建人
	@TableField(value = "create_user")
    private String createUser;
    
    //更新时间
	@TableField(value = "update_time")
    private Date updateTime;
    
    //更新人
	@TableField(value = "update_user")
    private String updateUser;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status
				+ ", createTime=" + createTime + ", createUser=" + createUser + ", updateTime=" + updateTime
				+ ", updateUser=" + updateUser + "]";
	}

//	@Override
//	public String toString() {
//		return JsonUtils.toJson(this);
//	}
	
}
