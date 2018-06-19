package com.shuxin.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("t_projects_mapping_opt")
public class ProjectsMappingOpt extends ProjectsMapping {

	private static final long serialVersionUID = 360702027354173671L;
	
	@TableField(value = "opt_type")
	private String optType;

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}	

}
