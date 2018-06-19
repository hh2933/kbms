package com.shuxin.model.vo;

public class MinDrugTypeVo {
	
	private String id;
	
	private String drugCode;	
	
	/**
	 * 项目名称
	 */
	private String drugName;	
	

	/**
	 * 最小分类编码
	 */
	private String minTypeCode;
	
	/**
	 * 最小分类名称
	 */
	private String minTypeName;	


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

}
