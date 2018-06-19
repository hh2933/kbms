package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
/**
*
* 规则表信息
*
*/
@TableName("t_dictionary")
public class Dictionary implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	
	/**字典类型*/
	@TableField(value = "dict_type")
	private String dictType;
	
	/**字典编码 */
	@TableField(value = "dict_code")
	private String dictCode;
	
	/**字典名称 */
	@TableField(value = "dict_name")
	private String dictName;
	

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
	//字典类型code
	
	@TableField(value = "DICT_TYPE_CODE")
	private String dictTypeCode;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
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

	public String getDictTypeCode() {
		return dictTypeCode;
	}

	public void setDictTypeCode(String dictTypeCode) {
		this.dictTypeCode = dictTypeCode;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
