package com.shuxin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_limit_treatment_mapping_opt")
public class LimitTreatmentOpt extends LimitTreatment {

	private static final long serialVersionUID = -7440930528336234208L;

	@TableField(value = "opt_type")
	private String optType;

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	
}
