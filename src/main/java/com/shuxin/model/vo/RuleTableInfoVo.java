package com.shuxin.model.vo;

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
@TableName("T_RULE_TABLE_INFO")
public class RuleTableInfoVo implements Serializable {
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;
	
	
	/** 主键id */
	@TableId(type = IdType.UUID)
	private String id;
	
	/**表名 */
	@TableField(value = "table_name")
	private String tableName;
	
	/**表名 */
	@TableField(value = "menu_id")
	private String menuId;
	
	/**表名 */
	@TableField(value = "menu_name")
	private String menuName;
	
	/**表名 */
	@TableField(value = "rule_type")
	private String ruleType;
	
	
	/**表名 */
	@TableField(value = "remark")
	private String remark;
	
	/**表名 */
	@TableField(value = "is_used")
	private String isUsed;
	
	
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
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
