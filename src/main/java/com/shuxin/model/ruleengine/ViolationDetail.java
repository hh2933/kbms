package com.shuxin.model.ruleengine;

import java.io.Serializable;

public class ViolationDetail  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -701838521736955024L;
	//违规明细序号
	private String DetailCode;
	//违反规则
	private String ruleName;
	//规则分类
	private String ruleType;
	//相关记录ID
	private String recordId;
	//结果分组编码
	private String groupCode;
	//参保人ID
	private String patId;
	//费用项目编码
	private String productCode;
	//费用项目名称
	private String productName;
	//违规金额
	private String againstAmount;
	//违规数量
	private String againstNum;
	//违规单据号
	private String againstCode;
	//单据状态
	private String billStatus;
	//违规规则设置提示信息编码
	private String messageCode;
	//提示信息
	private String message;	
	//服务时间
	private String serviceDate;
	//本次审核结果编码
	private String violationCode;
	
	//医嘱类型
	private String docSerialType;
	//医嘱流水号
	private String docSerialCode;
	//副诊断名称
	private String diaViceName;
	
	public String getDetailCode() {
		return DetailCode;
	}
	public void setDetailCode(String detailCode) {
		DetailCode = detailCode;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		if(productName == null)
		{
			productName="";
		}
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAgainstAmount() {
		return againstAmount;
	}
	public void setAgainstAmount(String againstAmount) {
		this.againstAmount = againstAmount;
	}
	public String getAgainstNum() {
		return againstNum;
	}
	public void setAgainstNum(String againstNum) {
		this.againstNum = againstNum;
	}
	public String getAgainstCode() {
		return againstCode;
	}
	public void setAgainstCode(String againstCode) {
		this.againstCode = againstCode;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getMessageCode() {
		return messageCode;
	}
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	public String getViolationCode() {
		return violationCode;
	}
	public void setViolationCode(String violationCode) {
		this.violationCode = violationCode;
	}
	public String getDocSerialType() {
		if(docSerialType==null)
		{
			docSerialType="";
		}
		return docSerialType;
	}
	public void setDocSerialType(String docSerialType) {
		this.docSerialType = docSerialType;
	}
	public String getDocSerialCode() {
		if(docSerialCode==null)
		{
			docSerialCode="";
		}
		return docSerialCode;
	}
	public void setDocSerialCode(String docSerialCode) {
		this.docSerialCode = docSerialCode;
	}
	public String getDiaViceName() {
		if(diaViceName==null)
		{
			diaViceName="";
		}
		return diaViceName;
	}
	public void setDiaViceName(String diaViceName) {
		this.diaViceName = diaViceName;
	}
	
	
}
