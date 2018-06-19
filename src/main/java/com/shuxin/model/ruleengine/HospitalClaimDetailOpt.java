package com.shuxin.model.ruleengine;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("T_CHARGE_DETAILS_OPT")
public class HospitalClaimDetailOpt extends HospitalClaimDetail {

	private static final long serialVersionUID = -8511610337200677949L;

	private String optId;
	
	private Date optDate;

	public String getOptId() {
		return optId;
	}

	public void setOptId(String optId) {
		this.optId = optId;
	}

	public Date getOptDate() {
		return optDate;
	}

	public void setOptDate(Date optDate) {
		this.optDate = optDate;
	}
}
