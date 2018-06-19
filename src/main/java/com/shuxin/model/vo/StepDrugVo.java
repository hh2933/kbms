package com.shuxin.model.vo;

public class StepDrugVo {
	
	private String id;
	/**
	 * 药品分组
	 */
	private String drugGroup;
	
	/**
	 * 药品分类（1为抗细菌抗生素，2为抗真菌抗生素）
	 */
	private String drugType;
	
	/**
	 * 药品编码
	 */
	private String drugCode;
	
	/**
	 * 药品名称
	 */
	private String drugName;
	
	/**
	 * 最小分类
	 */
	private String minType;
	
	/**
	 * 备注
	 */
	private String remark;

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

	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
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

}
