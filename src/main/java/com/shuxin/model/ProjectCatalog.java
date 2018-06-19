package com.shuxin.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_project_catalog")
public class ProjectCatalog  implements Serializable {
	
	private static final long serialVersionUID = 6172684024529962870L;

	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 项目编码
	 */
	@TableField(value = "project_code")
	private String projectCode;
	
	/**
	 * 项目新增编码
	 */
	@TableField(value = "newproject_code",validate=FieldStrategy.IGNORED)
	private String newprojectCode;

	/**
	 * 项目名称
	 */
	@TableField(value = "project_Name")
	private String projectName;
	
	/**
	 * 项目内涵
	 */
	@TableField(value = "project_connotation",validate=FieldStrategy.IGNORED)
	private String projectConnotation;
	
	/**
	 * 除外内容
	 */
	@TableField(value = "exception_content",validate=FieldStrategy.IGNORED)
	private String exceptionContent;
	
	/**
	 * 规格
	 */
	@TableField(validate=FieldStrategy.IGNORED)
	private String formats;
	
	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 价格
	 */
	private BigDecimal price;
	
	/**
	 * 生产厂商
	 */
	private String manufacturer;
	
	/**
	 * 自付比例
	 */
	@TableField(value = "self_pay")
	private double selfPay;
	
	/**
	 * 备注
	 */
	@TableField(validate=FieldStrategy.IGNORED)
	private String remark;
	
	/**
	 * 限定就医方式（1门规；2职工普通门诊；3省医保；4工伤；5职工急诊）
	 */
	@TableField(value = "limit_treatment")
	private String limitTreatment;
	
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

	public String getNewprojectCode() {
		return newprojectCode;
	}

	public void setNewprojectCode(String newprojectCode) {
		this.newprojectCode = newprojectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectConnotation() {
		return projectConnotation;
	}

	public void setProjectConnotation(String projectConnotation) {
		this.projectConnotation = projectConnotation;
	}

	public String getExceptionContent() {
		return exceptionContent;
	}

	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}

	public String getFormats() {
		return formats;
	}

	public void setFormats(String formats) {
		this.formats = formats;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}	

	public double getSelfPay() {
		return selfPay;
	}

	public void setSelfPay(double selfPay) {
		this.selfPay = selfPay;
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

	public String getLimitTreatment() {
		return limitTreatment;
	}

	public void setLimitTreatment(String limitTreatment) {
		this.limitTreatment = limitTreatment;
	}
	
}
