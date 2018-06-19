package com.shuxin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_minimumdrugtype")
public class MinDrugType  implements Serializable {

	private static final long serialVersionUID = 4938002265827985484L;

	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 药品编码
	 */
	@TableField(value = "drug_code")
	private String drugCode;
	

	/**
	 * 药品名称
	 */
	@TableField(value = "drug_name")
	private String drugName;
	
	/**
	 * 最小分类编码
	 */
	@TableField(value = "minitype_code")
	private String minTypeCode;
	
	/**
	 * 最小分类名称
	 */
	@TableField(value = "miniType_name")
	private String minTypeName;	
	
	
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

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}	

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getMinTypeCode() {
		return minTypeCode;
	}

	public void setMinTypeCode(String minTypeCode) {
		this.minTypeCode = minTypeCode;
	}

	public String getMinTypeName() {
		return minTypeName;
	}

	public void setMinTypeName(String minTypeName) {
		this.minTypeName = minTypeName;
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
