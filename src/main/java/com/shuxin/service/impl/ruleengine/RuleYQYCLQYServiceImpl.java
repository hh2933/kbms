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
import com.shuxin.mapper.ruleengine.HospitalClaimMapper;
import com.shuxin.mapper.ruleengine.RuleYQYCLQYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 
 *月取药超量
 */
@Service
public class RuleYQYCLQYServiceImpl implements IAnalysisRuleService{

	@Autowired
	private RuleYQYCLQYMapper ruleYQYCLQYMapper;
	
	@Autowired
	private HospitalClaimMapper hospitalClaimMapper;
	
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		//如果超量取药（1008/1009）或者提前取药（1006/1007）有违规，就不用审核此规则
		if(Constants.ruleExamineResult.containsKey(hospitalClaim.getDiaSerialCode()))
		{
			Constants.ruleExamineResult.remove(hospitalClaim.getDiaSerialCode());
			return null;
		}
		
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
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
			
			drugCodeList.add(hospitalClaimDetail);			
			
		}
		
		if(drugCodeList.size() == 0)
		{
			return null;
		}		
		
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("list", drugCodeList);
//		pMap.put("medTreatmentMode", hospitalClaim.getMedTreatmentMode());
		
		
		List<Map<String, Object>> monthOverDrugList = ruleYQYCLQYMapper.selectMonthOverPrescription(pMap);
		
		
		if(monthOverDrugList == null || monthOverDrugList.size()==0)
		{
			return null;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("list", drugCodeList);
		
		List<Map<String, Object>> mothTakeDrugList = hospitalClaimMapper.selectMonthTakeDrugsInfo(paramMap);
		
		String prompt ="月取药总量超最大用药天数?天";
		
		for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
		{
			//该药品在一个月内被取的次数
			int takeDrugCount = 0;
			//该药品在一个月内被取的数量
			float takeDrugNum=0;
			
			for(Map<String, Object> mothTakeDrugMap:mothTakeDrugList)
			{
				if(hospitalClaimDetail.getProductCode().
						equals((String)mothTakeDrugMap.get("PRODUCTCODE")))
				{
					takeDrugCount = ((BigDecimal)mothTakeDrugMap.get("COUNT")).intValue();
					takeDrugNum = ((BigDecimal)mothTakeDrugMap.get("PNUMBER")).floatValue();
					break;
				}
			}
			
			if(takeDrugCount<4)
			{
				continue;
			}
			
			for(Map<String, Object> monthOverDrugMap:monthOverDrugList)
			{
				if(!hospitalClaimDetail.getProductCode().
						equals((String)monthOverDrugMap.get("YPBM")))
				{
					continue;
				}
				
				//限定参保类型不为"N",且与患者的参保类型一样就跳过
				if(!Constants.N_FLAG.equalsIgnoreCase((String)monthOverDrugMap.get("XDCBLX")))
				{
					List<String> insuredTypeList=Arrays.asList(((String)monthOverDrugMap.get("XDCBLX")).split(","));
					if(insuredTypeList.contains(hospitalClaim.getPatInsuredType()))
					{
						continue;
					}
				}
				
				float minPackageNum = ((BigDecimal)monthOverDrugMap.get("ZXBZSL")).floatValue();
				float maxPrescriptionNum = ((BigDecimal)monthOverDrugMap.get("RCFZDL")).floatValue();
				int limitDays = ((BigDecimal)monthOverDrugMap.get("MRXDTS")).intValue();
				if(!Constants.N_FLAG.equalsIgnoreCase((String)monthOverDrugMap.get("BZXDTSSD"))
						&&hospitalClaim.getMedTreatmentMode().equals("13"))
				{
					limitDays = Integer.parseInt(((String)monthOverDrugMap.get("BZXDTSSD")));
				}
				
				float patPrescriptionNum=takeDrugNum*minPackageNum/maxPrescriptionNum;
				if(patPrescriptionNum>limitDays)
				{
					
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt.replace("?", String.valueOf(limitDays)));
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
				}
			}
			
		}
		
		
		
		return list;
	}	

}
