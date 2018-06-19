package com.shuxin.model.vo;

public class DrugMappingVo {
	
	private String id;
	
	private String drugCode;	
	
	/**
	 * 项目名称
	 */
	private String drugName;	
	

	/**
	 * 标准编码
	 */
	private String standardCode;
	
	/**
	 * 标准产品编码
	 */
	private String standardProductCode;
	
	/**
	 * 标准名称
	 */
	private String standardName;
	
	
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

}
