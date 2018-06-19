package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleXDXBMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限定性别(违规/可疑)
 *
 */
@Service
public class RuleXDXBServiceImpl  implements IAnalysisRuleService{

	@Autowired
	private RuleXDXBMapper ruleXDXBMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		
//		List<HospitalClaimDetail> projectCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> HospitalClaimDetailListTemp =new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp=new ArrayList<String>();
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
//			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
//			
//			projectCodeList.add(hospitalClaimDetail);	
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				HospitalClaimDetailListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
		}
		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> limitSexList = null;
		
		//判断是违规还是可疑
		if("1".equals(rule.getResultType()))
		{
			limitSexList = ruleXDXBMapper.selectLimitSexInfoIllegal(HospitalClaimDetailListTemp);
		}
		else
		{
			limitSexList = ruleXDXBMapper.selectLimitSexInfoSuspicious(HospitalClaimDetailListTemp);
		}
		
		if(limitSexList.size()==0)
		{
			return null;
		}
		//性别
		String patSex="-1";
		
		if("1".equals(hospitalClaim.getPatGender()))
		{
			patSex="1";
		}
		else if("2".equals(hospitalClaim.getPatGender()))
		{
			patSex="0";
		}
		
		for(Map<String, String> limitSexMap:limitSexList)
		{
			if(patSex.equals(limitSexMap.get("XDXB")))
			{
				continue;
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(hospitalClaimDetail.getProductCode().equals(limitSexMap.get("XMBM")))
				{
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, limitSexMap.get("TSXX"));
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
