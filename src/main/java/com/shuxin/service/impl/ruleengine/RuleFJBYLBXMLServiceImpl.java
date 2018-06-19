package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleFJBYLBXMLMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 非基本医疗保险目录
 * 
 */
@Service
public class RuleFJBYLBXMLServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleFJBYLBXMLMapper ruleFJBYLBXMLMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<HospitalClaimDetail> projectCodeListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
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
//			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectCodeListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
		}
		
		if(hospitalClaimDetails.size() == 0)
		{
			return null;
		}
		
		List<Map<String, String>> insuranceFolderList = ruleFJBYLBXMLMapper.selectInsuranceFolder(projectCodeListTemp);
		
		for(Map<String, String> insuranceFolderMap:insuranceFolderList)
		{
			if("1".equals(insuranceFolderMap.get("YLBXLX")))
			{
				if("410".equals(hospitalClaim.getPatInsuredType()))
				{
					continue;
				}
			}
			else
			{
				if("510".equals(hospitalClaim.getPatInsuredType()))
				{
					continue;
				}
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(hospitalClaimDetail.getProductCode().equals(insuranceFolderMap.get("XMBM")))
				{
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, insuranceFolderMap.get("TSXX"));
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
