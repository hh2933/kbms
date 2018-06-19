package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RulePFQYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *频繁取药
 */
@Service
public class RulePFQYServiceImpl implements IAnalysisRuleService{

	@Value(value = "${frequently.drug.day}")
	private String frequentlyDrugDay;
	
	@Autowired
	private RulePFQYMapper rulePFQYMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
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
		
		List<String> exceptDrugsList= rulePFQYMapper.selectFrequentlyExceptDrugs(drugCodeList);
		
		if(exceptDrugsList.size()>0)
		{
			Iterator<HospitalClaimDetail> iterator = drugCodeList.iterator();
			while (iterator.hasNext())
			{
				HospitalClaimDetail hospitalClaimDetail =  iterator.next();
				if(exceptDrugsList.contains(hospitalClaimDetail.getProductCode()))
				{
					iterator.remove();
				}
			}
		}
		
		if(drugCodeList.size() == 0)
		{
			return null;
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("list", drugCodeList);
		paramMap.put("frequentlyDrugDay", frequentlyDrugDay);
		
		List<Map<String, Object>> frequentlyDrugList = hospitalClaimDetailMapper.selectFrequentlyDrugCount(paramMap);
				
		if(frequentlyDrugList.size()==0)
		{
			return null;
		}
		boolean flag=false;
		for(Map<String,Object> frequentlyDrugMap:frequentlyDrugList)
		{
			int frequentlyDrugCount = ((BigDecimal)frequentlyDrugMap.get("COUNT")).intValue();
			
			if(frequentlyDrugCount>1)
			{
				flag=true;
				break;
			}
			
//			if(frequentlyDrugCount<1)
//			{
//				continue;
//			}
			
//			for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
//			{
//				if(hospitalClaimDetail.getProductCode().equals((String)frequentlyDrugMap.get("PRODUCTCODE")))
//				{
//					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "频繁取药");
//					if(list==null)
//					{
//						list= new ArrayList<ViolationDetail>();
//					}
//					list.add(violationDetail);
////					break;
//				}
//			}
		}
		
		if(flag)
		{
			violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), "频繁取药");
			if(list==null)
			{
				list= new ArrayList<ViolationDetail>();
			}
			list.add(violationDetail);
		}
		
		return list;
	}

}
