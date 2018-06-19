package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleXETMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 
 *限儿童
 */
@Service
public class RuleXETServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleXETMapper ruleXETMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
//		List<HospitalClaimDetail> projectList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> projectListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{						
//			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
//			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			
//			projectList.add(hospitalClaimDetail);
		}
		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		List<Map<String, Object>> limitAgeList = ruleXETMapper.selectLimitAgeInfo(projectListTemp);
		
		
		if(limitAgeList.size()==0)
		{
			return null;
		}
		
		int patAge = hospitalClaim.getPatAge();
		String patInsuredType=hospitalClaim.getPatInsuredType();
		
		for(Map<String, Object> limitAgeMap:limitAgeList)
		{
			if(!Constants.N_FLAG.equalsIgnoreCase((String)limitAgeMap.get("CBLXBM")))
			{
				List<String> insuredTypeList=Arrays.asList(((String)limitAgeMap.get("CBLXBM")).split(","));
				if(insuredTypeList.contains(patInsuredType))
				{
					continue;
				}
			}
			
			int limitAge = ((BigDecimal)limitAgeMap.get("XDNLNT")).intValue();
			if(patAge<limitAge)
			{
				continue;
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(hospitalClaimDetail.getProductCode().equals((String)limitAgeMap.get("XMBM")))
				{
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, (String)limitAgeMap.get("TSXX"));
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
//					break;
				}
			}
		}
		
		return list;
	}

}
