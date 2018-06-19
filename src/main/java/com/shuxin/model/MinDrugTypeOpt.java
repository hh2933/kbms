package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_minimumdrugtype_opt")
public class MinDrugTypeOpt extends MinDrugType implements Serializable{

	private static final long serialVersionUID = 4018624256344353745L;
	@TableField(value = "opt_type")
	private String optType;



	public String getOptType() {
		return optType;
	}



	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	
	
}
