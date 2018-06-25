package com.shuxin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.shuxin.model.catalog.YPML;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@TableName("t_drugs_catalog")
public class DrugCatalog  implements Serializable {
	
	private static final long serialVersionUID = -4374333415598990721L;

	@TableId(type = IdType.UUID)
	private String id;
	
	/**
	 * 药品编码
	 */
	@TableField(value = "drug_code")
	private String drugCode;
	
	/**
	 * 药品新增编码
	 */
	@TableField(value = "new_drug_code",validate=FieldStrategy.IGNORED)
	private String newdrugCode;

	/**
	 * 药品名称
	 */
	@TableField(value = "drug_name")
	private String drugName;
	
	/**
	 * 剂型
	 */
	@TableField(value = "dosage")
	private String dosage;
	
	/**
	 * 最小包装
	 */
	@TableField(value = "min_package")
	private String minPack;
	
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
	@TableField(value = "self_pay")
	private double selfPay;
	
	/**
	 * 给药途径
	 */
	@TableField(value = "Route_medication")
	private String routeMedication;
	
	/**
	 * 药品分类
	 */
	@TableField(value = "drug_Type")
	private String drugType;
	
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

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getNewdrugCode() {
		return newdrugCode;
	}

	public void setNewdrugCode(String newdrugCode) {
		this.newdrugCode = newdrugCode;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

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

	public String getRouteMedication() {
		return routeMedication;
	}

	public void setRouteMedication(String routeMedication) {
		this.routeMedication = routeMedication;
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



	/*
	 * @author zengj
	 * @Description YPML转化为DrugCatalog
	 * @Date 2018-6-22 10:18
	 * @Param [ypml]
	 * @return com.shuxin.model.DrugCatalog
	 */
	public static DrugCatalog ypmlToDrugCatalog(YPML ypml){
		DrugCatalog drugCatalog = new DrugCatalog();
		drugCatalog.setDrugCode(ypml.getYbypbm());			//医保药品编码
		drugCatalog.setNewdrugCode("");						//药品新增编码
		drugCatalog.setDrugName(ypml.getYyypmc());			//医院药品名称
		drugCatalog.setDrugType(ypml.getYplx().toString());	//药品类别(1:西药,2:中成药,3:中药饮片)
		drugCatalog.setDosage(ypml.getJx());				//剂型,比如咀嚼片/注射用粉针/普通片剂等
		drugCatalog.setFormats(ypml.getGg());				//规格
		drugCatalog.setUnit(ypml.getDw());					//单位
		drugCatalog.setPrice(ypml.getJg());					//价格
		drugCatalog.setMinPack(ypml.getZxbz().toString()); 	//最小包装
		drugCatalog.setManufacturer(ypml.getSccs());		//生产厂商
		drugCatalog.setSelfPay(ypml.getZfbl()); 			//自付比例
		drugCatalog.setRouteMedication(ypml.getGytj());		//给药途径
		drugCatalog.setRemark(ypml.getSm());				//说明
		drugCatalog.setLimitTreatment("N");					//限定就医方式
		return drugCatalog;
	}

	public void preInsert(){
		this.setId(UUID.randomUUID().toString().replace("-",""));
		this.setCreateTime(new Date());
		this.setCreateUser("自动识别");
		this.setUpdateTime(new Date());
		this.setUpdateUser("自动识别");
	}
}
