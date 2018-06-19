package com.shuxin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_decompose_mapping_opt")
public class DecomposeHospitalOpt extends DecomposeHospital {
	private static final long serialVersionUID = -4410435945802635416L;

	@TableField(value = "opt_type")
	private String optType;

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	
}
