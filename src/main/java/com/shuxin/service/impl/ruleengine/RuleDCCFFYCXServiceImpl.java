package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.DrugInfoMapper;
import com.shuxin.mapper.ruleengine.RuleDCCFFYCXMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *单次处方费用超限
 */
@Service
public class RuleDCCFFYCXServiceImpl implements IAnalysisRuleService{

	@Autowired
	private RuleDCCFFYCXMapper ruleDCCFFYCXMapper;
	
	@Autowired
	private DrugCatalogMapper drugCatalogMapper;
	
	@Autowired
	private DrugInfoMapper drugInfoMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= new ArrayList<ViolationDetail>();
		ViolationDetail violationDetail =null;
		
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
		
		Map<String, Object> paramMap= new HashMap<String, Object>();
		
		paramMap.put("ruleType", "2");
		paramMap.put("list", drugCodeList);
		List<String> exceptDrugCodes=drugInfoMapper.selectExceptDrugInfo(paramMap);
		
		//去掉被排除的药品
		if(exceptDrugCodes.size()>0)
		{
			Iterator<HospitalClaimDetail> iterator = drugCodeList.iterator();
			while (iterator.hasNext())
			{
				HospitalClaimDetail hospitalClaimDetail =  iterator.next();
				if(exceptDrugCodes.contains(hospitalClaimDetail.getProductCode()))
				{
					iterator.remove();
				}
			}
		}
		
		Map<String, BigDecimal> feeOverMap = ruleDCCFFYCXMapper.selectSingleFeeOverInfo();
		
		BigDecimal drugAmount = null;
		String prompt=null;
		
		List<Map<String, String>> drugTypeList=drugCatalogMapper.selectDrugType(drugCodeList);
		
		for(Map<String, String> drugTypeMap:drugTypeList)
		{
			for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
			{
				if(hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("DRUG_CODE"))
				   || hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("NEW_DRUG_CODE")))
				{
					
					if("1".equals(drugTypeMap.get("DRUG_TYPE")))
					{
						drugAmount = feeOverMap.get("XYFY");
						prompt="西药费用超限";
					}
					else if("2".equals(drugTypeMap.get("DRUG_TYPE")))
					{
						drugAmount = feeOverMap.get("ZCYFY");
						prompt="中成药费用超限";
					}
					else 
					{
						drugAmount = feeOverMap.get("ZYYPFY");
						prompt="中药饮片费用超限";
					}
					
					if(hospitalClaimDetail.getMedInsCost().compareTo(drugAmount)==1)
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
					}
//					break;
				}
			}
		}
		
		return list;
	}

}
