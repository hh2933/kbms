package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_drug_info_opt")
public class DrugInfoOpt extends DrugInfo implements Serializable{

	private static final long serialVersionUID = 7270102668994386188L;

	@TableField(value = "opt_type")
	private String optType;

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}
	
}
