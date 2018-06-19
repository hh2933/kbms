package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleZDHLXSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *疾病诊断合理性审核
 */
@Service
public class RuleZDHLXSHServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleZDHLXSHMapper ruleZDHLXSHMapper;
	
	@Value(value = "${child.age}")
	private String childAge;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<String> diagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		
		if(diagnosisCodeList.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> diagnosisIllegalList = ruleZDHLXSHMapper.selectDiagnosisIllegalInfo(diagnosisCodeList);
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(Map<String, String> diagnosisIllegalMap:diagnosisIllegalList)
		{
			if("1".equals(diagnosisIllegalMap.get("XDLB")))
			{
				if(hospitalClaim.getPatAge()<Integer.parseInt(childAge))
//				if("5".equals(hospitalClaim.getPatType()))
				{
					continue;
				}
			}
			else if("2".equals(diagnosisIllegalMap.get("XDLB")))
			{
				if("1".equals(hospitalClaim.getPatGender()))
				{
					continue;
				}
			}
			else 
			{
				if("2".equals(hospitalClaim.getPatGender()))
				{
					continue;
				}
			}
			
			violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, diagnosisIllegalMap.get("JBBM"),diagnosisIllegalMap.get("DIAGNOSIS_NAME"), "疾病诊断不合理");
			if(list==null)
			{
				list= new ArrayList<ViolationDetail>();
			}
			list.add(violationDetail);
		}
		
		return list;
	}

}
