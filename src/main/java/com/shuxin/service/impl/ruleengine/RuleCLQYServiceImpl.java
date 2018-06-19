package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleCLQYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 超量取药（违规/可疑）
 *
 */
@Service
public class RuleCLQYServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleCLQYMapper ruleCLQYMapper;
	
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetailsList) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> drugList = new ArrayList<HospitalClaimDetail>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetailsList)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
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
		
		List<Map<String, Object>> overDrugsList=null;
		
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("list", drugList);
		//pMap.put("medTreatmentMode", hospitalClaim.getMedTreatmentMode());
		
		
		if("1".equals(rule.getResultType()))
		{
			overDrugsList = ruleCLQYMapper.selectOverPrescriptionIllegal(pMap);
		}
		else
		{
			overDrugsList = ruleCLQYMapper.selectOverPrescriptionSuspicious(pMap);
		}
		
		if(overDrugsList.size()==0)
		{
			return null;
		}
		
		for(HospitalClaimDetail hospitalClaimDetail:drugList)
		{
			for(Map<String, Object> overDrugsMap:overDrugsList)
			{
				if(!hospitalClaimDetail.getProductCode().equals
						((String)overDrugsMap.get("YPBM")))
				{
					continue;
				}
				
				//限定参保类型不为"N",且与患者的参保类型一样就跳过
				if(!Constants.N_FLAG.equalsIgnoreCase((String)overDrugsMap.get("XDCBLX")))
				{
					List<String> insuredTypeList=Arrays.asList(((String)overDrugsMap.get("XDCBLX")).split(","));
					if(insuredTypeList.contains(hospitalClaim.getPatInsuredType()))	
					{
						continue;
					}
				}
				
				float minPackageNum = ((BigDecimal)overDrugsMap.get("ZXBZSL")).floatValue();
				float maxPrescriptionNum = ((BigDecimal)overDrugsMap.get("RCFZDL")).floatValue();
				int limitDays = ((BigDecimal)overDrugsMap.get("MRXDTS")).intValue();
				if(!Constants.N_FLAG.equalsIgnoreCase((String)overDrugsMap.get("BZXDTSSD"))
						&&hospitalClaim.getMedTreatmentMode().equals("13"))
				{
					limitDays = Integer.parseInt(((String)overDrugsMap.get("BZXDTSSD")));
				}
				
				float patPrescriptionNum=hospitalClaimDetail.getPnumber()*minPackageNum/maxPrescriptionNum;
				if(patPrescriptionNum>limitDays)
				{
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "该药品药量不得超过"+limitDays+"天");
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
					Constants.ruleExamineResult.put(hospitalClaim.getDiaSerialCode(), "true");
				}
				
			}
		}
		
		return list;
	}

}
