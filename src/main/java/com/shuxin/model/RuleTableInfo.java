package com.shuxin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
/**
*
* 规则表信息
*
*/
@TableName("T_RULE_TABLE_INFO")
public class RuleTableInfo implements Serializable {
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
	@TableField(value = "remark",validate=FieldStrategy.IGNORED)
	private String remark;
	
	/**表名 */
	@TableField(value = "is_used")
	private String isUsed;
	
	/**规则编号 */
	@TableField(value = "rule_code")
	private int ruleCode;
	
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
	//结果类型(1：违规；2：可疑)
	@TableField(value = "RESULT_TYPE")
	private String resultType;
	//查询明细(0：不查询；1：查明细)
	@TableField(value = "IS_DETAIL")
	private String isDetail;
	//'明细类型(1：取药记录；2：疗程记录；3：相关记录；4：重复记录)'
	@TableField(value = "DETAIL_TYPE",validate=FieldStrategy.IGNORED)
	private String datailType;
	//'是否需要反馈(0：否；1：是)'
	@TableField(value = "IS_FEEDBACK")
	private String isFeedback;
    //规则适用类型(1：门诊；2：住院;3：门诊/住院)
	@TableField(value = "APPLY_TYPE")
	private String applyType;
	
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getIsDetail() {
		return isDetail;
	}

	public void setIsDetail(String isDetail) {
		this.isDetail = isDetail;
	}

	public String getDatailType() {
		return datailType;
	}

	public void setDatailType(String datailType) {
		this.datailType = datailType;
	}

	public String getIsFeedback() {
		return isFeedback;
	}

	public void setIsFeedback(String isFeedback) {
		this.isFeedback = isFeedback;
	}

	@TableField(exist = false)
	private List<RuleColumnInfo> rows;
	

	
	public List<RuleColumnInfo> getRows() {
		return rows;
	}

	public void setRows(List<RuleColumnInfo> rows) {
		this.rows = rows;
	}

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

	public int getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(int ruleCode) {
		this.ruleCode = ruleCode;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}	

}
