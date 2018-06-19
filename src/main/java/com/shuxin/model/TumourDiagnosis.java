package com.shuxin.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
*
*治疗项目转
*
*/
@TableName("t_tumour_diagnosis")
public class TumourDiagnosis {
	

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
	
	
	
	
	
	
	
}
