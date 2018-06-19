package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *1011_分组对应表
 *
 */
@TableName("t_step_drug_mapping")
public class StepDrug implements Serializable {

	private static final long serialVersionUID = -6921308631275344502L;
	
	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 药品分组
	 */
	@TableField(value = "drug_group")
	private String drugGroup;
	
	/**
	 * 药品分类（1为抗细菌抗生素，2为抗真菌抗生素）
	 */
	@TableField(value = "drug_type")
	private String drugType;
	
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
	 * 最小分类
	 */
	@TableField(value = "min_type",validate=FieldStrategy.IGNORED)
	private String minType;
	
	/**
	 * 备注
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

	public String getDrugGroup() {
		return drugGroup;
	}

	public void setDrugGroup(String drugGroup) {
		this.drugGroup = drugGroup;
	}

	public String getdrugType() {
		return drugType;
	}

	public void setdrugType(String drugType) {
		this.drugType = drugType;
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

	public String getMinType() {
		return minType;
	}

	public void setMinType(String minType) {
		this.minType = minType;
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
