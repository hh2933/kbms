package com.shuxin.service.ruleengine;

import java.util.List;

import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;

public interface IAnalysisRuleService {
	
	public List<ViolationDetail> executeRule(RuleTableInfo rule,HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails);

}
