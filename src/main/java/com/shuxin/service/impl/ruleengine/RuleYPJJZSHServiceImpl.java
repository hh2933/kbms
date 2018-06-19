package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.DiagnosisCatalogMapper;
import com.shuxin.mapper.ruleengine.RuleYPJJZSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 
 *药品禁忌审核
 */
@Service
public class RuleYPJJZSHServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleYPJJZSHMapper ruleYPJJZSHMapper;
	
	@Autowired
	private DiagnosisCatalogMapper diagnosisCatalogMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> drugList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> drugListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		List<String> diagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		
		if(diagnosisCodeList.size()==0)
		{
			return null;
		}
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
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
		
		List<Map<String, String>> drugTabooList = ruleYPJJZSHMapper.selectDrugTabooInfo(drugListTemp);
		
		if(drugTabooList.size()==0)
		{
			return null;
		}
		
		List<Map<String,String>> diagnosticList = diagnosisCatalogMapper.selectDiagnosticInfo(diagnosisCodeList);
		
		for(Map<String, String> drugTabooMap:drugTabooList)
		{
			List<String> diagnosticTabooList = Arrays.asList(drugTabooMap.get("JBJJ").split(","));
			
			for(HospitalClaimDetail hospitalClaimDetail:drugList)
			{
				if(hospitalClaimDetail.getProductCode().equals(drugTabooMap.get("YPBM")))
				{
					StringBuffer diagnosisName = new StringBuffer();
					for(Map<String, String> diagnosticMap:diagnosticList)
					{
						if(diagnosticTabooList.contains(diagnosticMap.get("DIAGNOSIS_CODE")))
						{
							if(!StringUtils.isEmpty(diagnosisName.toString()))
							{
								diagnosisName.append(",");
							}
							diagnosisName.append(diagnosticMap.get("DIAGNOSIS_NAME")+"疾病");
						}
					}
					
					if(StringUtils.isEmpty(diagnosisName.toString()))
					{
						break;
					}
					
					String prompt=hospitalClaimDetail.getProductName()+"禁用于"+diagnosisName;
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
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
