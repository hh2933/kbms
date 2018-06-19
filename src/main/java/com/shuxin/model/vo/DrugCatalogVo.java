package com.shuxin.model.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;

public class DrugCatalogVo {
	
	private String id;
	
	private String drugCode;	
	
	/**
	 * 项目名称
	 */
	private String drugName;
	
	private String newdrugCode;
	

	/**
	 * 剂型
	 */
	private String dosage;
	
	/**
	 * 最小包装
	 */
	private String minPack;
	
	/**
	 * 给药途径
	 */
	private String routeMedication;
	
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
	 * 药品分类
	 */
	private String drugType;
	
	/**
	 * 备注
	 */
	private String remark;	
	
	/**
	 * 限定就医方式（1门规；2职工普通门诊；3省医保；4工伤；5职工急诊）
	 */
	private String limitTreatment;

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getMinPack() {
		return minPack;
	}

	public void setMinPack(String minPack) {
		this.minPack = minPack;
	}

	public String getRouteMedication() {
		return routeMedication;
	}

	public void setRouteMedication(String routeMedication) {
		this.routeMedication = routeMedication;
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

	public String getNewdrugCode() {
		return newdrugCode;
	}

	public void setNewdrugCode(String newdrugCode) {
		this.newdrugCode = newdrugCode;
	}

	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getLimitTreatment() {
		return limitTreatment;
	}

	public void setLimitTreatment(String limitTreatment) {
		this.limitTreatment = limitTreatment;
	}
	

}
