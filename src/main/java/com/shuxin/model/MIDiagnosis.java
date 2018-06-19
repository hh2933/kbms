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
* 适应症诊断表信息
*
*/
@TableName("T_MIdiagnosis")
public class MIDiagnosis implements Serializable{
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	/**医保诊断编码 */
	@TableField(value = "DIAGNOSIS_CODE")
	private String diagnosisCode;
	/**自适应症包编码 */
	@TableField(value = "ADAPTION_PACKAGE_CODE")
	private String adaptionPackageCode;
	/**医保诊断名称 */
	@TableField(value = "DIAGNOSIS_NAME")
	private String diagnosisName;
	/**提示信息*/
	@TableField(value = "MESSAGE",validate=FieldStrategy.IGNORED)
	private String message;
	/**创建时间 */
	@TableField(value = "CREATE_TIME")
	private Date createTime;
	/**创建人 */
	@TableField(value = "CREATE_USER")
	private String createUser;
	/**修改时间 */
	@TableField(value = "UPDATE_TIME")
	private Date updateTime;
	/**修改人 */
	@TableField(value = "UPDATE_USER")
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
	public String getAdaptionPackageCode() {
		return adaptionPackageCode;
	}
	public void setAdaptionPackageCode(String adaptionPackageCode) {
		this.adaptionPackageCode = adaptionPackageCode;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
