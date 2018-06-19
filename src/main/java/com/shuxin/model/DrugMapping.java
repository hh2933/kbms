package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_drugs_mapping")
public class DrugMapping  implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 标准编码
	 */
	@TableField(value = "standard_code")
	private String standardCode;
	
	/**
	 * 标准产品编码
	 */
	@TableField(value = "standard_product_code")
	private String standardProductCode;
	
	/**
	 * 标准名称
	 */
	@TableField(value = "standard_name")
	private String standardName;	

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

	public String getStandardCode() {
		return standardCode;
	}

	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}

	public String getStandardProductCode() {
		return standardProductCode;
	}

	public void setStandardProductCode(String standardProductCode) {
		this.standardProductCode = standardProductCode;
	}

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
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
