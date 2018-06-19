package com.shuxin.service.impl.ruleengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleYPPWJJSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 
 *药品配伍禁忌审核
 */
@Service
public class RuleYPPWJJSHServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleYPPWJJSHMapper ruleYPPWJJSHMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		List<HospitalClaimDetail> drugList = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, List<HospitalClaimDetail>> sameDayProjectMap = new HashMap<String, List<HospitalClaimDetail>>();
		
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			//将同一天的项目放在一个List里
			String serviceDate = sf.format(hospitalClaimDetail.getServiceDate());
			if(sameDayProjectMap.containsKey(serviceDate))
			{
				sameDayProjectMap.get(serviceDate).add(hospitalClaimDetail);
			}
			else
			{
				List<HospitalClaimDetail> tempList = new ArrayList<HospitalClaimDetail>();
				tempList.add(hospitalClaimDetail);
				sameDayProjectMap.put(serviceDate, tempList);
			}
		}
		
		if(sameDayProjectMap.isEmpty())
		{
			return null;
		}
		
		List<Map<String,String>> compatibilityTabooInfo=ruleYPPWJJSHMapper.selectCompatibilityTabooInfo();
		
		Iterator<List<HospitalClaimDetail>> it = sameDayProjectMap.values().iterator();
		
		while(it.hasNext())
		{
			drugList =  it.next();
			if(drugList.size()<2)
			{
				continue;
			}
			
//			List<Map<String, String>> compatibilityTabooList = ruleYPPWJJSHMapper.selectCompatibilityTabooInfo(drugList);
			List<Map<String, String>> compatibilityTabooList = new ArrayList<Map<String,String>>();
			for(Map<String,String> compatibilityTaboo :compatibilityTabooInfo)
			{
				for(HospitalClaimDetail hospitalClaimDetail1:drugList)
				{
					for(HospitalClaimDetail hospitalClaimDetail12:drugList)
					{
						if(hospitalClaimDetail1.getProductCode().equals(compatibilityTaboo.get("YPBMA"))&&
								hospitalClaimDetail12.getProductCode().equals(compatibilityTaboo.get("YPBMB")))
						{
							compatibilityTabooList.add(compatibilityTaboo);
						}
					}
				}
			}
				
			
			String prompt="";
			for(Map<String, String> compatibilityTabooMap:compatibilityTabooList)
			{
				for(HospitalClaimDetail hospitalClaimDetail:drugList)
				{
					if(hospitalClaimDetail.getProductCode().equals(compatibilityTabooMap.get("YPBMA")))
					{
						prompt=compatibilityTabooMap.get("YPMCA")+"和"+compatibilityTabooMap.get("YPMCB")+"在一起"+compatibilityTabooMap.get("PWJGTS");
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
//						break;
					}
				}
			}
		
		}
		
		return list;
	}

}
