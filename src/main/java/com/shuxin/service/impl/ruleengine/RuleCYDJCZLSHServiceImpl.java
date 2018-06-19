package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleCYDJCZLSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 出院带检查治疗审核
 *
 */
@Service
public class RuleCYDJCZLSHServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleCYDJCZLSHMapper ruleCYDJCZLSHMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> projectList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> projectListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有出院检查的项目才需要审核
			if("1".equals(hospitalClaimDetail.getThrCatType())||
					!"1".equals(hospitalClaimDetail.getOutHospTakMedicine()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			projectList.add(hospitalClaimDetail);
		}

		if(projectList.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> checkInfoList = ruleCYDJCZLSHMapper.selectOutHospitalCheckInfo(projectListTemp);
		
		if(checkInfoList.size()==0)
		{
			return null;
		}
		
		for(Map<String, String> checkInfoMap:checkInfoList)
		{
			for(HospitalClaimDetail hospitalClaimDetail:projectList)
			{
				if(hospitalClaimDetail.getProductCode().equals(checkInfoMap.get("XMBM")))
				{
					if(hospitalClaimDetail.getPnumber()>Integer.parseInt(checkInfoMap.get("XDCS")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, checkInfoMap.get("TSXX"));
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
