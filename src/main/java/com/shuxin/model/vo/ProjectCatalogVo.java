package com.shuxin.model.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;

public class ProjectCatalogVo {
	
	private String id;
	
	private String projectCode;	
	
	/**
	 * 项目名称
	 */
	private String projectName;
	
	private String newprojectCode;
	
	/**
	 * 项目内涵
	 */
	private String projectConnotation;
	
	/**
	 * 除外内容
	 */
	private String exceptionContent;
	
	/**
	 * 规格
	 */
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
	private double selfPay;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 限定就医方式（1门规；2职工普通门诊；3省医保；4工伤；5职工急诊）
	 */
	private String limitTreatment;

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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}	

	

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewprojectCode() {
		return newprojectCode;
	}

	public void setNewprojectCode(String newprojectCode) {
		this.newprojectCode = newprojectCode;
	}

	public String getLimitTreatment() {
		return limitTreatment;
	}

	public void setLimitTreatment(String limitTreatment) {
		this.limitTreatment = limitTreatment;
	}

}
