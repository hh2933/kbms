package com.shuxin.model.vo;

public class DrugInfoVo {

	
	private String id;
	
	/**
	 * 药品编码
	 */
	private String drugCode;
	
	/**
	 * 药品名称
	 */
	private String drugName;
	
	/**
	 * 规则类型（1：出院带药规则；2：单次处方药品种类异常/费用超限）
	 */
	private String ruleType;

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

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

}
