package com.shuxin.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;

/**
*
*治疗项目转
*
*/
@TableName("t_treatsubject_trans")
public class TreatSubjectTrans {

	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	//医保诊断编码
	@TableField(value = "subject_code")
    private String subjectCode;
	//医保诊断名称
	@TableField(value = "definesubject_code")
    private String definesubjectCode;
	//标准诊断编码
	@TableField(value = "subject_name")
    private String subjectName;
	//标准诊断名称
	@TableField(value = "message",validate=FieldStrategy.IGNORED)
	private String message;

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

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getDefinesubjectCode() {
		return definesubjectCode;
	}

	public void setDefinesubjectCode(String definesubjectCode) {
		this.definesubjectCode = definesubjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
