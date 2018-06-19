package com.shuxin.model.ruleengine;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;

@TableName("T_PATIENT_INFORMATION_OPT")
public class HospitalClaimOpt extends HospitalClaim {

	private static final long serialVersionUID = 2322486421569047584L;
	
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
