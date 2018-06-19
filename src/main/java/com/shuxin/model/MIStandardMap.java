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
* 项目标准映射表信息
*
*/
@TableName("t_mistandardmap")
public class MIStandardMap implements Serializable{

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	/**医保项目编码 */
	@TableField(value = "PROJECT_CODE")
	private String projectCode;
	/**医保项目名称*/
	@TableField(value = "PROJECT_NAME")
	private String projectName;
	/**标准编码 */
	@TableField(value = "STANDARD_CODE")
	private String standardCode;
	/**标准名称 */
	@TableField(value = "STANDARD_NAME")
	private String standardName;
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

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStandardCode() {
		return standardCode;
	}

	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}

	public String getStandardName() {
		return standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
