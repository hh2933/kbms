package com.shuxin.webservice;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.WSReturnResult;

@WebService
public interface RuleEngineWebService {
	
	@WebMethod
	public WSReturnResult examine(String certId,HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails);

}
