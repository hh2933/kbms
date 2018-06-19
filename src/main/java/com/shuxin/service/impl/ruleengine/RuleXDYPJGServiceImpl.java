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
import com.shuxin.mapper.ruleengine.RuleXDYPJGMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 限定价格
 *
 */
@Service
public class RuleXDYPJGServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleXDYPJGMapper ruleXDYPJGMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
//		List<HospitalClaimDetail> projectCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
//		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
//		{
//			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
//			
//			projectCodeList.add(hospitalClaimDetail);		
//			
//		}
//		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> projectPriceList = ruleXDYPJGMapper.selectLimitProjectPrice(hospitalClaimDetails);
		
		if(projectPriceList.size()==0)
		{
			return null;
		}
		
		for(Map<String, String> projectPriceMap:projectPriceList)
		{
			if(Constants.Y_FLAG.equalsIgnoreCase(projectPriceMap.get("LXGBSH")))
			{
				if("3".equals(hospitalClaim.getPatType()))
				{
					continue;
				}
			}
			
			if(!Constants.N_FLAG.equalsIgnoreCase(projectPriceMap.get("BCYSHDCBLX")))
			{
				List<String> insuredTypeList=Arrays.asList(projectPriceMap.get("BCYSHDCBLX").split(","));
				if(insuredTypeList.contains(hospitalClaim.getPatInsuredType()))	
				{
					continue;
				}				
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(hospitalClaimDetail.getProductCode().equals(projectPriceMap.get("YPBM")))
				{
					BigDecimal limitPrice= new BigDecimal(projectPriceMap.get("YPJG"));
					if(hospitalClaimDetail.getAmount().compareTo(limitPrice)==1)
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, projectPriceMap.get("TSXX"));
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
					}
				}
			}
			
		}
		
		return list;
	}

}
