package com.shuxin.model.ruleengine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.shuxin.commons.utils.StringUtils;

@TableName("T_PATIENT_INFORMATION")
public class HospitalClaim implements Serializable  {
	/**
	 * 
	 */
	@TableField(exist = false)
	private static final long serialVersionUID = -7831393306962049715L;
	//id
	@NotEmpty(message="id值不能为空")
	@TableField(value = "id")
	private String id;
	//定点机构ID
	@NotEmpty(message="pointOrgId值不能为空")
	@TableField(value = "pointOrgId")
	private String pointOrgId;
	//定点机构等级
	@NotEmpty(message="pointOrgGrade不能为空")
	@Range(min=-2, max=3, message="pointOrgGrade值无效")
	@TableField(value = "pointOrgGrade")
	private String pointOrgGrade;
	//定点机构类型
	@NotEmpty(message="pointOrgType值不能为空")
	@Range(min=0, max=9, message="pointOrgType值无效")
	@TableField(value = "pointOrgType")
	private String pointOrgType;
	//就诊流水号
	@NotEmpty(message="diaSerialCode值不能为空")
	@TableField(value = "diaSerialCode")
	private String diaSerialCode;
	//就医方式
	@NotEmpty(message="medTreatmentMode值不能为空")
	@Digits(integer=2,fraction=0,message="medTreatmentMode值格式有误")
	@TableField(value = "medTreatmentMode")
	private String medTreatmentMode;
	//住院号\门诊号
	@NotEmpty(message="medTreatmentCode值不能为空")
	@TableField(value = "medTreatmentCode")
	private String medTreatmentCode;
	//参保人编码
	@NotEmpty(message="patCode值不能为空")
	@TableField(value = "patCode")
	private String patCode;
	//医保卡号
	@TableField(value = "patMedInsuCard")
	private String patMedInsuCard;
	//参保人姓名
	@NotEmpty(message="patName值不能为空")
	@TableField(value = "patName")
	private String patName;
	//参保类型
	@NotEmpty(message="patInsuredType值不能为空")
	@Digits(integer=3,fraction=0,message="patInsuredType值格式有误")
	@TableField(value = "patInsuredType")
	private String patInsuredType;
	//是否怀孕	
	@Range(min=0, max=1, message="patIsPregnant值无效")
	@TableField(value = "patIsPregnant")
	private String patIsPregnant;
	//是否哺乳期孕妇
	@Range(min=0, max=1, message="patIsNursing值无效")
	@TableField(value = "patIsNursing")
	private String patIsNursing;
	//性别
	@NotEmpty(message="patGender值不能为空")
	@TableField(value = "patGender")
	private String patGender;
	//年龄
	@NotNull(message="patAge值不能为空")
	@Range(min=0, max=200, message="patAge值无效")
	@TableField(value = "patAge")
	private int patAge;
	//身高
	@TableField(value = "patHeight")
	private float patHeight;
	//体重
	@TableField(value = "patWeight")
	private float patWeight;
	//身份证号
	@TableField(value = "patIdCard")
	private String patIdCard;
	//人员类别
	@Range(min=1, max=3, message="patType值无效")
	@TableField(value = "patType")
	private String patType;
	//出生日期
	@NotNull(message="patBirthday值不能为空")
	@Past(message="patBirthday值无效")
	@TableField(value = "patBirthday")
	private Date patBirthday;
	//入院诊断编码
	@TableField(value = "diaInHospCode")
	private String diaInHospCode;
	//入院诊断名称
	@TableField(value = "diaInHospName")
	private String diaInHospName;
	//出院诊断编码
	@TableField(value = "diaOutHospCode")
	private String diaOutHospCode;
	//出院诊断名称
	@TableField(value = "diaOutHospName")
	private String diaOutHospName;
	//副诊断编码
	@TableField(value = "diaViceCode")
	private String diaViceCode;
	//副诊断名称
	@TableField(value="diaViceName")
	private String diaViceName;
	//入院日期
	@NotNull(message="inHospDate值不能为空")
	@TableField(value = "inHospDate")
	private Date inHospDate;
	//出院日期
	@TableField(value = "outHospDate")
	private Date outHospDate;
	//结算日期	
	@TableField(value = "settlementDate")
	private Date settlementDate;
	//入院科室编码
	@NotEmpty(message="inHospDeptCode值不能为空")
	@TableField(value = "inHospDeptCode")
	private String inHospDeptCode;
	//入院科室名称
	@NotEmpty(message="inHospDeptName值不能为空")
	@TableField(value = "inHospDeptName")
	private String inHospDeptName;
	//出院科室编码
	@TableField(value = "outHospDeptCode")
	private String outHospDeptCode;
	//出院科室名称
	@TableField(value = "outHospDeptName")
	private String outHospDeptName;
	//医师工号
	@NotEmpty(message="physicianCode值不能为空")
	@TableField(value = "physicianCode")
	private String physicianCode;
	//医师姓名
	@NotEmpty(message="physicianName值不能为空")
	@TableField(value = "physicianName")
	private String physicianName;
	//医师级别
	@TableField(value = "physicianLevel")
	private String physicianLevel;
	//异地就医标识
	@NotEmpty(message="isRemote值不能为空")
	@Range(min=0, max=1, message="isRemote值无效")
	@TableField(value = "isRemote")
	private String isRemote;
	//医保内金额
	@NotNull(message="medInsTotalCost值不能为空")
	@TableField(value = "medInsTotalCost")
	private BigDecimal medInsTotalCost;
	//总费用
	@NotNull(message="totalCost值不能为空")
	@Min(value=0,message="totalCost值无效")
	@TableField(value = "totalCost")
	private BigDecimal totalCost;
	//待遇类型
	@TableField(value = "treatmentType")
	private String treatmentType;
	//一至六级残疾军人待核标志
	@TableField(value = "disSoldierSign")
	private String disSoldierSign;
	//因战因公标志
	@TableField(value = "becWarPubSign")
	private String becWarPubSign;
	//保健类别
	@TableField(value = "healType")
	private String healType;
	//病区
	@TableField(value = "inpaArea")
	private String inpaArea;
	//病案号
	@TableField(value = "medRecordCode")
	private String medRecordCode;
	//住院状态
	@Range(min=-1, max=1, message="liveHospStatus值无效")
	@TableField(value = "liveHospStatus")
	private String liveHospStatus;
	//是否增量
	@NotEmpty(message="isIncrement值不能为空")
	@Range(min=0, max=1, message="isIncrement值无效")
	@TableField(value = "isIncrement")
	private String isIncrement;
	//保险经办ID
	@NotEmpty(message="insuHandId值不能为空")
	@TableField(value = "insuHandId")
	private String insuHandId;
	
	//上传来源
	@NotEmpty(message="dataSource值不能为空")
	@Range(min=1, max=3, message="dataSource值无效")
	private String dataSource;
	
	//主单操作类型
	@NotEmpty(message="operationType值不能为空")
	@Range(min=1, max=3, message="operationType值无效")
	private String operationType;
	
	//治疗方式
	private String dbzzlfs;
	
	//治疗名称
	private String dbzzlmc;
	
	private String recoverState;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPointOrgId() {
		return pointOrgId;
	}
	public void setPointOrgId(String pointOrgId) {
		this.pointOrgId = pointOrgId;
	}
	public String getPointOrgGrade() {
		return pointOrgGrade;
	}
	public void setPointOrgGrade(String pointOrgGrade) {
		this.pointOrgGrade = pointOrgGrade;
	}
	public String getPointOrgType() {
		return pointOrgType;
	}
	public void setPointOrgType(String pointOrgType) {
		this.pointOrgType = pointOrgType;
	}
	public String getDiaSerialCode() {
		return diaSerialCode;
	}
	public void setDiaSerialCode(String diaSerialCode) {
		this.diaSerialCode = diaSerialCode;
	}
	public String getMedTreatmentMode() {
		return medTreatmentMode;
	}
	public void setMedTreatmentMode(String medTreatmentMode) {
		this.medTreatmentMode = medTreatmentMode;
	}
	public String getMedTreatmentCode() {
		return medTreatmentCode;
	}
	public void setMedTreatmentCode(String medTreatmentCode) {
		this.medTreatmentCode = medTreatmentCode;
	}
	public String getPatCode() {
		return patCode;
	}
	public void setPatCode(String patCode) {
		this.patCode = patCode;
	}
	public String getPatMedInsuCard() {
		return patMedInsuCard;
	}
	public void setPatMedInsuCard(String patMedInsuCard) {
		this.patMedInsuCard = patMedInsuCard;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getPatInsuredType() {
		return patInsuredType;
	}
	public void setPatInsuredType(String patInsuredType) {
		this.patInsuredType = patInsuredType;
	}
	public String getPatIsPregnant() {
		return patIsPregnant;
	}
	public void setPatIsPregnant(String patIsPregnant) {
		this.patIsPregnant = patIsPregnant;
	}
	public String getPatIsNursing() {
		return patIsNursing;
	}
	public void setPatIsNursing(String patIsNursing) {
		this.patIsNursing = patIsNursing;
	}
	public String getPatGender() {
		return patGender;
	}
	public void setPatGender(String patGender) {
		this.patGender = patGender;
	}
	public int getPatAge() {
		return patAge;
	}
	public void setPatAge(int patAge) {
		this.patAge = patAge;
	}
	public float getPatHeight() {
		return patHeight;
	}
	public void setPatHeight(float patHeight) {
		this.patHeight = patHeight;
	}
	public float getPatWeight() {
		return patWeight;
	}
	public void setPatWeight(float patWeight) {
		this.patWeight = patWeight;
	}
	public String getPatIdCard() {
		return patIdCard;
	}
	public void setPatIdCard(String patIdCard) {
		this.patIdCard = patIdCard;
	}
	public String getPatType() {
		return patType;
	}
	public void setPatType(String patType) {
		this.patType = patType;
	}
	public Date getPatBirthday() {
		return patBirthday;
	}
	public void setPatBirthday(Date patBirthday) {
		this.patBirthday = patBirthday;
	}
	public String getDiaInHospCode() {
		return diaInHospCode;
	}
	public void setDiaInHospCode(String diaInHospCode) {
		this.diaInHospCode = diaInHospCode;
	}
	public String getDiaInHospName() {
		return diaInHospName;
	}
	public void setDiaInHospName(String diaInHospName) {
		this.diaInHospName = diaInHospName;
	}
	public String getDiaOutHospCode() {
		return diaOutHospCode;
	}
	public void setDiaOutHospCode(String diaOutHospCode) {
		this.diaOutHospCode = diaOutHospCode;
	}
	public String getDiaOutHospName() {
		return diaOutHospName;
	}
	public void setDiaOutHospName(String diaOutHospName) {
		this.diaOutHospName = diaOutHospName;
	}
	public String getDiaViceCode() {
		return diaViceCode;
	}
	public void setDiaViceCode(String diaViceCode) {
		this.diaViceCode = diaViceCode;
	}
	public String getDiaViceName() {
		return diaViceName;
	}
	public void setDiaViceName(String diaViceName) {
		this.diaViceName = diaViceName;
	}
	public Date getInHospDate() {
		return inHospDate;
	}
	public void setInHospDate(Date inHospDate) {
		this.inHospDate = inHospDate;
	}
	public Date getOutHospDate() {
		return outHospDate;
	}
	public void setOutHospDate(Date outHospDate) {
		this.outHospDate = outHospDate;
	}
	public Date getSettlementDate() {
		return settlementDate;
	}
	public void setSettlementDate(Date settlementDate) {
		this.settlementDate = settlementDate;
	}
	public String getInHospDeptCode() {
		return inHospDeptCode;
	}
	public void setInHospDeptCode(String inHospDeptCode) {
		this.inHospDeptCode = inHospDeptCode;
	}
	public String getInHospDeptName() {
		return inHospDeptName;
	}
	public void setInHospDeptName(String inHospDeptName) {
		this.inHospDeptName = inHospDeptName;
	}
	public String getOutHospDeptCode() {
		return outHospDeptCode;
	}
	public void setOutHospDeptCode(String outHospDeptCode) {
		this.outHospDeptCode = outHospDeptCode;
	}
	public String getOutHospDeptName() {
		return outHospDeptName;
	}
	public void setOutHospDeptName(String outHospDeptName) {
		this.outHospDeptName = outHospDeptName;
	}
	public String getPhysicianCode() {
		return physicianCode;
	}
	public void setPhysicianCode(String physicianCode) {
		this.physicianCode = physicianCode;
	}
	public String getPhysicianName() {
		return physicianName;
	}
	public void setPhysicianName(String physicianName) {
		this.physicianName = physicianName;
	}
	public String getPhysicianLevel() {
		return physicianLevel;
	}
	public void setPhysicianLevel(String physicianLevel) {
		this.physicianLevel = physicianLevel;
	}
	public String getIsRemote() {
		return isRemote;
	}
	public void setIsRemote(String isRemote) {
		this.isRemote = isRemote;
	}
	public BigDecimal getMedInsTotalCost() {
		return medInsTotalCost;
	}
	public void setMedInsTotalCost(BigDecimal medInsTotalCost) {
		this.medInsTotalCost = medInsTotalCost;
	}
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
	public String getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}
	public String getDisSoldierSign() {
		return disSoldierSign;
	}
	public void setDisSoldierSign(String disSoldierSign) {
		this.disSoldierSign = disSoldierSign;
	}
	public String getBecWarPubSign() {
		return becWarPubSign;
	}
	public void setBecWarPubSign(String becWarPubSign) {
		this.becWarPubSign = becWarPubSign;
	}
	public String getHealType() {
		return healType;
	}
	public void setHealType(String healType) {
		this.healType = healType;
	}
	public String getInpaArea() {
		return inpaArea;
	}
	public void setInpaArea(String inpaArea) {
		this.inpaArea = inpaArea;
	}
	public String getMedRecordCode() {
		return medRecordCode;
	}
	public void setMedRecordCode(String medRecordCode) {
		this.medRecordCode = medRecordCode;
	}
	public String getLiveHospStatus() {
		return liveHospStatus;
	}
	public void setLiveHospStatus(String liveHospStatus) {
		if(StringUtils.isEmpty(liveHospStatus))
		{
			liveHospStatus="-1";
		}
		this.liveHospStatus = liveHospStatus;
	}
	public String getIsIncrement() {
		return isIncrement;
	}
	public void setIsIncrement(String isIncrement) {
		this.isIncrement = isIncrement;
	}
	public String getInsuHandId() {
		return insuHandId;
	}
	public void setInsuHandId(String insuHandId) {
		this.insuHandId = insuHandId;
	}
//	
//	public ReturnResult check(){
//		
//	}
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	public String getDbzzlfs() {
		return dbzzlfs;
	}
	public void setDbzzlfs(String dbzzlfs) {
		this.dbzzlfs = dbzzlfs;
	}
	public String getDbzzlmc() {
		return dbzzlmc;
	}
	public void setDbzzlmc(String dbzzlmc) {
		this.dbzzlmc = dbzzlmc;
	}
	
	public String getRecoverState() {
		return recoverState;
	}
	public void setRecoverState(String recoverState) {
		this.recoverState = recoverState;
	}
	@Override
	public String toString() {
		return "HospitalClaim [id=" + id + ", pointOrgId=" + pointOrgId + ", pointOrgGrade=" + pointOrgGrade
				+ ", pointOrgType=" + pointOrgType + ", diaSerialCode=" + diaSerialCode + ", medTreatmentMode="
				+ medTreatmentMode + ", medTreatmentCode=" + medTreatmentCode + ", patCode=" + patCode
				+ ", patMedInsuCard=" + patMedInsuCard + ", patName=" + patName + ", patInsuredType=" + patInsuredType
				+ ", patIsPregnant=" + patIsPregnant + ", patIsNursing=" + patIsNursing + ", patGender=" + patGender
				+ ", patAge=" + patAge + ", patHeight=" + patHeight + ", patWeight=" + patWeight + ", patIdCard="
				+ patIdCard + ", patType=" + patType + ", patBirthday=" + patBirthday + ", diaInHospCode="
				+ diaInHospCode + ", diaInHospName=" + diaInHospName + ", diaOutHospCode=" + diaOutHospCode
				+ ", diaOutHospName=" + diaOutHospName + ", diaViceCode=" + diaViceCode + ", diaViceName=" + diaViceName
				+ ", inHospDate=" + inHospDate + ", outHospDate=" + outHospDate + ", settlementDate=" + settlementDate
				+ ", inHospDeptCode=" + inHospDeptCode + ", inHospDeptName=" + inHospDeptName + ", outHospDeptCode="
				+ outHospDeptCode + ", outHospDeptName=" + outHospDeptName + ", physicianCode=" + physicianCode
				+ ", physicianName=" + physicianName + ", physicianLevel=" + physicianLevel + ", isRemote=" + isRemote
				+ ", medInsTotalCost=" + medInsTotalCost + ", totalCost=" + totalCost + ", treatmentType="
				+ treatmentType + ", disSoldierSign=" + disSoldierSign + ", becWarPubSign=" + becWarPubSign
				+ ", healType=" + healType + ", inpaArea=" + inpaArea + ", medRecordCode=" + medRecordCode
				+ ", liveHospStatus=" + liveHospStatus + ", isIncrement=" + isIncrement + ", insuHandId=" + insuHandId
				+ ", dataSource=" + dataSource + ", operationType=" + operationType + ", dbzzlfs=" + dbzzlfs
				+ ", dbzzlmc=" + dbzzlmc + "]";
	}
	
	
	
	
	
	
}
