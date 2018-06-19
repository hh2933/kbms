package com.shuxin.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_Drugs_mapping_opt")
public class DrugMappingOpt extends DrugMapping implements Serializable{

	private static final long serialVersionUID = -5132839610888080149L;
	@TableField(value = "opt_type")
	private String optType;



	public String getOptType() {
		return optType;
	}



	public void setOptType(String optType) {
		this.optType = optType;
	}
	
	
	
}
