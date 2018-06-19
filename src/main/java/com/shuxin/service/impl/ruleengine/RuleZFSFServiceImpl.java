package com.shuxin.service.impl.ruleengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleZFSFMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 
 *重复收费
 */
@Service
public class RuleZFSFServiceImpl implements IAnalysisRuleService{
	
	@Autowired
	private RuleZFSFMapper ruleZFSFMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<HospitalClaimDetail> projectList = null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, List<HospitalClaimDetail>> sameDayProjectMap = new HashMap<String, List<HospitalClaimDetail>>();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只审核项目，不审核药品
			if("1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}			
			
			
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
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
		
//		List<Map<String,String>> repeatDrugInfo =ruleZFSFMapper.selectRepeatDrugInfo();
		
		Iterator<List<HospitalClaimDetail>> it = sameDayProjectMap.values().iterator();
		
		while(it.hasNext())
		{
			projectList =  it.next();
			//如果项目少于2个就不可能出现重复收费的情况
			if(projectList.size()<2)
			{
				continue;
			}
			
			List<Map<String, String>> repeatProjectList = ruleZFSFMapper.selectRepeatDrugInfo(projectList);
//			List<Map<String, String>> repeatProjectList = new ArrayList<Map<String,String>>();
//			for(Map<String,String> repeatDrug :repeatDrugInfo)
//			{
//				for(HospitalClaimDetail hospitalClaimDetail1 :projectList)
//				{
//					for(HospitalClaimDetail hospitalClaimDetail2 :projectList)
//					{
//						if(hospitalClaimDetail1.getProductCode().equals(repeatDrug.get("PROJECT_CODE"))&&
//								hospitalClaimDetail2.getProductCode().equals(repeatDrug.get("PROJECT_CODE_A")))
//						{
//							repeatProjectList.add(repeatDrug);
//						}
//					}
//				}
//			}
			
			if(repeatProjectList.size()==0)
			{
				continue;
			}
			
			for(Map<String, String> repeatProjectMap:repeatProjectList)
			{
				if(!Constants.N_FLAG.equalsIgnoreCase(repeatProjectMap.get("SFCKS")))
				{
					List<String> departments = Arrays.asList(repeatProjectMap.get("SFCKS").split(","));
					if(departments.contains(hospitalClaim.getInHospDeptCode()))
					{
						continue;
					}
				}
				
				for(HospitalClaimDetail hospitalClaimDetail:projectList)
				{
					if(hospitalClaimDetail.getProductCode().equals(repeatProjectMap.get("PROJECT_CODE")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, repeatProjectMap.get("TSXX"));
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
