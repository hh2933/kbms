package com.shuxin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_limit_number_mapping_opt")
public class LimitNumberOpt extends LimitNumber{
	
	private static final long serialVersionUID = 3150893851188335370L;
	
	@TableField(value = "opt_type")
	private String optType;

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	

}
