package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *
 * 诊断标准映射
 *
 */
@TableName("t_diagnosticbz_mapping")
public class DiagnosticmaBZMpping implements Serializable {


	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	//医保诊断编码
	@TableField(value = "diagnosis_code")
    private String diagnosisCode;
	//医保诊断名称
	@TableField(value = "diagnosis_name")
    private String diagnosisName;
	//标准诊断编码
	@TableField(value = "bzdiagnosis_code")
    private String bzdiagnosisCode;
	//标准诊断名称
	@TableField(value = "bzdiagnosis_name")
	private String bzdiagnosisName;
	/**
	 * 备注
	 */
	@TableField(validate=FieldStrategy.IGNORED)
	private String remark;
	//创建时间
	@TableField(value = "create_time")
	private Date createTime;
	
	//创建人
	@TableField(value = "create_user")
    private String createUser;
    
    //更新时间
	@TableField(value = "update_time")
    private Date updateTime;
    
    //更新人
	@TableField(value = "update_user")
    private String updateUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDiagnosisCode() {
		return diagnosisCode;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		this.diagnosisCode = diagnosisCode;
	}

	public String getDiagnosisName() {
		return diagnosisName;
	}

	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}

	public String getBzdiagnosisCode() {
		return bzdiagnosisCode;
	}

	public void setBzdiagnosisCode(String bzdiagnosisCode) {
		this.bzdiagnosisCode = bzdiagnosisCode;
	}

	public String getBzdiagnosisName() {
		return bzdiagnosisName;
	}

	public void setBzdiagnosisName(String bzdiagnosisName) {
		this.bzdiagnosisName = bzdiagnosisName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
	
	
	
	
	
}
