package com.shuxin.model.ruleengine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.shuxin.commons.utils.Charsets;
import com.shuxin.commons.utils.StringUtils;

@TableName("T_CHARGE_DETAILS")
public class HospitalClaimDetail implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6628666325011669856L;
	//id
	@NotEmpty(message="id值不能为空")
	@TableField(value = "id")
	private String id;
//	//单据项编码
//	@TableField(value = "itemCode")
//	private String itemCode;
	//就诊流水号
	@NotEmpty(message="diaSerialCode值不能为空")
	@TableField(value = "diaSerialCode")
	private String diaSerialCode;
	//医嘱流水号
	@NotEmpty(message="docSerialCode值不能为空")
	@TableField(value = "docSerialCode")
	private String docSerialCode;
	//医嘱类型
	@TableField(value = "docSerialType")
	@Range(min=-1, max=1, message="docSerialType值无效")
	private String docSerialType;
	//项目编码
	@NotEmpty(message="productCode值不能为空")
	@TableField(value = "productCode")
	private String productCode;
	//项目名称
	@NotEmpty(message="productName值不能为空")
	@TableField(value = "productName")
	private String productName;
//	//类别编码
//	@TableField(value = "typeCode")
//	private String typeCode;
//	//类别名称
//	@TableField(value = "typeName")
//	private String typeName;
	//三大目录类别
	@NotEmpty(message="thrCatType值不能为空")
	@Range(min=1, max=4, message="thrCatType值无效")
	@TableField(value = "thrCatType")
	private String thrCatType;
	//报销等级
	@NotEmpty(message="reimLevel值不能为空")
	@Range(min=1, max=3, message="reimLevel值无效")
	@TableField(value = "reimLevel")
	private String reimLevel;
	//出院带药标识
	@NotEmpty(message="outHospTakMedicine值不能为空")
	@Range(min=0, max=1, message="outHospTakMedicine值无效")
	@TableField(value = "outHospTakMedicine")
	private String outHospTakMedicine;
	//单价
	@NotNull(message="unitPrice值不能为空")
	@Min(value=0,message="unitPrice值无效")
	@TableField(value = "unitPrice")
	private BigDecimal unitPrice;
	//数量
	@NotNull(message="pnumber值不能为空")
	@TableField(value = "pnumber")
	private float pnumber;
	//金额
	@NotNull(message="amount值不能为空")
	@Min(value=0,message="amount值无效")
	@TableField(value = "amount")
	private BigDecimal amount;
	//包装单位
	@NotEmpty(message="unit值不能为空")
	@TableField(value = "unit")
	private String unit;
	//规格	
	@TableField(value = "spec")
	private String spec;
	//用药天数	
	@Min(value=0,message="medDays值无效")
	@TableField(value = "medDays")
	private float medDays;
	//每次用量
	
	@Min(value=0,message="dosage值无效")
	@TableField(value = "dosage")
	private float dosage;
	//使用频次	
	@Min(value=0,message="frequency值无效")
	@TableField(value = "frequency")
	private float frequency;
	//帖数	
	@TableField(value = "cardNum")
	private float cardNum;
	//外院费用标识
	@NotNull(message="outYardSign值不能为空")
	@Range(min=0, max=1, message="outYardSign值无效")
	@TableField(value = "outYardSign")
	private String outYardSign;
	//计费标识
	@NotEmpty(message="chargSign值不能为空")
	@Range(min=-1, max=1, message="chargSign值无效")
	@TableField(value = "chargSign")
	private String chargSign;
	//服务日期
	@NotNull(message="serviceDate值不能为空")
	@TableField(value = "serviceDate")
	private Date serviceDate;
	
	
	@TableField(value="stopDate")
	private String stopDate;
	//执行科室编码
	@NotEmpty(message="hospDeptCode值不能为空")
	@TableField(value = "hospDeptCode")
	private String hospDeptCode;
	//执行科室名称
	@NotEmpty(message="hospDeptName值不能为空")
	@TableField(value = "hospDeptName")
	private String hospDeptName;
	//执行医生工号	
	@TableField(value = "physicianCode")
	private String physicianCode;
	//执行医生姓名
	@TableField(value = "physicianName")
	private String physicianName;
	//医保内金额
	@NotNull(message="medInsCost值不能为空")
	@Min(value=0,message="medInsCost值无效")
	@TableField(value = "medInsCost")
	private BigDecimal medInsCost;
	//先行自付金额
	@NotNull(message="payCost值不能为空")
	@Min(value=0,message="payCost值无效")
	@TableField(value = "payCost")
	private BigDecimal payCost;
	//自费金额
	@NotNull(message="selfCost值不能为空")
	@Min(value=0,message="selfCost值无效")
	@TableField(value = "selfCost")
	private BigDecimal selfCost;
	
	//明细操作类型
	@NotEmpty(message="detailOperationType值不能为空")
	@Range(min=1, max=3, message="detailOperationType值无效")
	private String detailOperationType;
	
	//门规病种
	private String mgbz;
	
	//费用归并编码
	private String fygbbm;
	
	//费用归并名称
	private String fygbmc;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
//	public String getItemCode() {
//		return itemCode;
//	}
//	public void setItemCode(String itemCode) {
//		this.itemCode = itemCode;
//	}
	public String getDiaSerialCode() {
		return diaSerialCode;
	}
	public void setDiaSerialCode(String diaSerialCode) {
		this.diaSerialCode = diaSerialCode;
	}
	public String getDocSerialCode() {
		if(docSerialCode==null)
		{
			return "";
		}
		return docSerialCode;
	}
	public void setDocSerialCode(String docSerialCode) {
		this.docSerialCode = docSerialCode;
	}
	public String getDocSerialType() {
		if(docSerialType==null)
		{
			return "";
		}
		return docSerialType;
	}
	public void setDocSerialType(String docSerialType) {
		this.docSerialType = docSerialType;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
//	public String getTypeCode() {
//		return typeCode;
//	}
//	public void setTypeCode(String typeCode) {
//		this.typeCode = typeCode;
//	}
//	public String getTypeName() {
//		return typeName;
//	}
//	public void setTypeName(String typeName) {
//		this.typeName = typeName;
//	}
	public String getThrCatType() {
		return thrCatType;
	}
	public void setThrCatType(String thrCatType) {
		this.thrCatType = thrCatType;
	}
	public String getReimLevel() {
		if(reimLevel==null)
		{
			return "";
		}
		return reimLevel;
	}
	public void setReimLevel(String reimLevel) {
		this.reimLevel = reimLevel;
	}
	public String getOutHospTakMedicine() {
		if(outHospTakMedicine==null)
		{
			return "";
		}
		return outHospTakMedicine;
	}
	public void setOutHospTakMedicine(String outHospTakMedicine) {
		this.outHospTakMedicine = outHospTakMedicine;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getPnumber() {
		return pnumber;
	}
	public void setPnumber(float pnumber) {
		this.pnumber = pnumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getUnit() {
		if(unit==null)
		{
			return "";
		}
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getSpec() {
		if(spec==null)
		{
			return "";
		}
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public float getMedDays() {
		return medDays;
	}
	public void setMedDays(float medDays) {
		this.medDays = medDays;
	}
	public float getDosage() {
		return dosage;
	}
	public void setDosage(float dosage) {
		this.dosage = dosage;
	}
	public float getFrequency() {
		return frequency;
	}
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	public float getCardNum() {
		return cardNum;
	}
	public void setCardNum(float cardNum) {
		this.cardNum = cardNum;
	}
	public String getOutYardSign() {
		if(outYardSign==null)
		{
			return "";
		}
		return outYardSign;
	}
	public void setOutYardSign(String outYardSign) {
		this.outYardSign = outYardSign;
	}
	public String getChargSign() {
		if(chargSign==null)
		{
			return "";
		}
		return chargSign;
	}
	public void setChargSign(String chargSign) {
		this.chargSign = chargSign;
	}
	public Date getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	
	public String getStopDate() {
		if(stopDate==null)
		{
			return "";
		}
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		
		this.stopDate = stopDate;
	}
	public String getHospDeptCode() {
		if(hospDeptCode==null)
		{
			return "";
		}
		return hospDeptCode;
	}
	public void setHospDeptCode(String hospDeptCode) {
		this.hospDeptCode = hospDeptCode;
	}
	public String getHospDeptName() {
		if(hospDeptName==null)
		{
			return "";
		}
		return hospDeptName;
	}
	public void setHospDeptName(String hospDeptName) {
		this.hospDeptName = hospDeptName;
	}
	public String getPhysicianCode() {
		if(physicianCode==null)
		{
			return "";
		}
		return physicianCode;
	}
	public void setPhysicianCode(String physicianCode) {
		this.physicianCode = physicianCode;
	}
	public String getPhysicianName() {
		if(physicianName==null)
		{
			return "";
		}
		return physicianName;
	}
	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}
	public BigDecimal getMedInsCost() {
		return medInsCost;
	}
	public void setMedInsCost(BigDecimal medInsCost) {
		this.medInsCost = medInsCost;
	}
	public BigDecimal getPayCost() {
		return payCost;
	}
	public void setPayCost(BigDecimal payCost) {
		this.payCost = payCost;
	}
	public BigDecimal getSelfCost() {
		return selfCost;
	}
	public void setSelfCost(BigDecimal selfCost) {
		this.selfCost = selfCost;
	}	
	
	public String getDetailOperationType() {
		if(detailOperationType==null)
		{
			return "";
		}
		return detailOperationType;
	}
	public void setDetailOperationType(String detailOperationType) {
		this.detailOperationType = detailOperationType;
	}	
	
	public String getMgbz() {
		if(StringUtils.isEmpty(mgbz))
		{
			mgbz="";
		}
		return Charsets.dropSurplusChar(mgbz);
	}
	public void setMgbz(String mgbz) {
		this.mgbz = mgbz;
	}
	public String getFygbbm() {
		if(StringUtils.isEmpty(fygbbm))
		{
			fygbbm="0";
		}
		return fygbbm;
	}
	public void setFygbbm(String fygbbm) {
		this.fygbbm = fygbbm;
	}
	public String getFygbmc() {
		if(StringUtils.isEmpty(fygbmc))
		{
			fygbmc="";
		}
		return fygbmc;
	}
	public void setFygbmc(String fygbmc) {
		this.fygbmc = fygbmc;
	}
	@Override
	public String toString() {
		return "HospitalClaimDetail [id=" + id + ", diaSerialCode=" + diaSerialCode + ", docSerialCode=" + docSerialCode
				+ ", docSerialType=" + docSerialType + ", productCode=" + productCode + ", productName=" + productName
				+ ", thrCatType=" + thrCatType + ", reimLevel=" + reimLevel + ", outHospTakMedicine="
				+ outHospTakMedicine + ", unitPrice=" + unitPrice + ", pnumber=" + pnumber + ", amount=" + amount
				+ ", unit=" + unit + ", spec=" + spec + ", medDays=" + medDays + ", dosage=" + dosage + ", frequency="
				+ frequency + ", cardNum=" + cardNum + ", outYardSign=" + outYardSign + ", chargSign=" + chargSign
				+ ", serviceDate=" + serviceDate + ", hospDeptCode=" + hospDeptCode + ", hospDeptName=" + hospDeptName
				+ ", physicianCode=" + physicianCode + ", physicianName=" + physicianName + ", medInsCost=" + medInsCost
				+ ", payCost=" + payCost + ", selfCost=" + selfCost + ", detailOperationType=" + detailOperationType
				+ ", mgbz=" + mgbz + ", fygbbm=" + fygbbm + ", fygbmc=" + fygbmc + "]";
	}
	
	
	
}
