package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *费用明细数据异常
 */
@Service
public class RuleFYMXSJYCServiceImpl implements IAnalysisRuleService {

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			BigDecimal amount =  hospitalClaimDetail.getUnitPrice().multiply(new BigDecimal(String.valueOf(hospitalClaimDetail.getPnumber())));
			if(hospitalClaimDetail.getAmount().compareTo(amount)!=0)
			{
				violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "费用明细数据异常");
				if(list==null)
				{
					list= new ArrayList<ViolationDetail>();
				}
				list.add(violationDetail);
			}
		}
		
		return list;
	}

}
