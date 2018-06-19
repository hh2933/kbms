package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleTSRQYPJJZSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 特殊人群药品禁忌症审核
 *
 */
@Service
public class RuleTSRQYPJJZSHServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleTSRQYPJJZSHMapper ruleTSRQYPJJZSHMapper;
	
	@Value(value = "${old.age}")
	private String oldAge;
	
	@Value(value = "${child.age}")
	private String childAge;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> drugList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> drugListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode()))
			{
				drugListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			drugList.add(hospitalClaimDetail);
		}
		
		if(drugList.size()==0)
		{
			return null;
		}
		
		
		List<Map<String, String>> specialPeopleList = ruleTSRQYPJJZSHMapper.selectSpecialPeopleInfo(drugListTemp);
		
		if(specialPeopleList.size()==0)
		{
			return null;
		}
		
		for(Map<String, String> specialPeopleMap:specialPeopleList)
		{
			if("1".equals(specialPeopleMap.get("RQLX")))
			{
				if(hospitalClaim.getPatAge()>=Integer.parseInt(childAge))
//				if(!"5".equals(hospitalClaim.getPatType()))
				{
					continue;
				}
			}
			else if("2".equals(specialPeopleMap.get("RQLX")))
			{
				if("0".equals(hospitalClaim.getPatIsPregnant()))
				{
					continue;
				}
			}
			else if("3".equals(specialPeopleMap.get("RQLX")))
			{
				if("0".equals(hospitalClaim.getPatIsNursing()))
				{
					continue;
				}
			}
			else if("5".equals(specialPeopleMap.get("RQLX")))
			{
				if("0".equals(hospitalClaim.getPatIsNursing())
						&&"0".equals(hospitalClaim.getPatIsPregnant()))
				{
					continue;
				}
			}
			else 
			{
				if(hospitalClaim.getPatAge()<Integer.parseInt(oldAge))
//				if(!"4".equals(hospitalClaim.getPatType()))
				{
					continue;
				}
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:drugList)
			{
				if(hospitalClaimDetail.getProductCode().equals(specialPeopleMap.get("YPBM")))
				{
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, specialPeopleMap.get("TSXX"));
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
