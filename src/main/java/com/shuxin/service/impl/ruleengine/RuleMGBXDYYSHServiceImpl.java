package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.Charsets;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleMGBXDYYSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 * 门规病限定项目
 *
 */
@Service
public class RuleMGBXDYYSHServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleMGBXDYYSHMapper ruleMGBXDYYSHMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		//只审核门规病患者
		if(!"13".equals(hospitalClaim.getMedTreatmentMode()))
		{
			return null;
		}
		
		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> specialDiseasesProjectList = ruleMGBXDYYSHMapper.selectSpecialDiseasesProjectInfo(hospitalClaimDetails);
		
		if(specialDiseasesProjectList.size()==0)
		{
			return null;
		}
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(Map<String, String> specialDiseasesProjectMap:specialDiseasesProjectList)
		{
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(hospitalClaimDetail.getProductCode().equals(specialDiseasesProjectMap.get("XMBM")))
				{					
					if(!(specialDiseasesProjectMap.get("DIAGNOSIS_CODE")).equals(hospitalClaimDetail.getMgbz()))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, specialDiseasesProjectMap.get("TSXX"));
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
