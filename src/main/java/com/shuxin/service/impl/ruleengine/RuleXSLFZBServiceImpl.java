package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.DateUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.LimitNumberMapper;
import com.shuxin.mapper.ruleengine.RuleXSLFZBMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 限定数量
 *
 */
@Service
public class RuleXSLFZBServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private LimitNumberMapper limitNumberMapper;
	
	@Autowired
	private RuleXSLFZBMapper ruleXSLFZBMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<HospitalClaimDetail> projectList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> projectListTemp =new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp=new ArrayList<String>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
//			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
//			
//			projectList.add(hospitalClaimDetail);
		}
		
		if(hospitalClaimDetails.size()<2)
		{
			return null;
		}
		
		projectList.addAll(hospitalClaimDetails);
		
		List<Map<String, String>> groupProjectList = limitNumberMapper.selectProjectMappingInfo(projectListTemp);
		
		if(groupProjectList.size()<2)
		{
			return null;
		}
		
		Map<String, List<HospitalClaimDetail>> groupProjectMap = new HashMap<String, List<HospitalClaimDetail>>();
		Iterator<HospitalClaimDetail> it = null;
		for(Map<String, String> projectMap:groupProjectList)
		{
			it = projectList.iterator();
			while(it.hasNext())
			{
				HospitalClaimDetail hospitalClaimDetail = it.next();
				if(hospitalClaimDetail.getProductCode().equals(projectMap.get("PROJECT_CODE")))
				{
					if(groupProjectMap.containsKey(projectMap.get("GROUP_CODE")))
					{
						groupProjectMap.get(projectMap.get("GROUP_CODE")).add(hospitalClaimDetail);
					}
					else
					{
						List<HospitalClaimDetail> tempList = new ArrayList<HospitalClaimDetail>();
						tempList.add(hospitalClaimDetail);
						groupProjectMap.put(projectMap.get("GROUP_CODE"), tempList);
					}
					it.remove();
				}
			}
		}
		
		List<Map<String, String>>  limitProjectSortList = ruleXSLFZBMapper.selectLimitProjectSortInfo();
		
		Date now = new Date();
		
		for(Map<String, String> limitProjectSortMap:limitProjectSortList)
		{
			int days = Integer.parseInt(limitProjectSortMap.get("SJQJT"))-1;
			int limitNum = Integer.parseInt(limitProjectSortMap.get("XDSLZLS"));
			Calendar calendar = Calendar.getInstance();//日历对象
			calendar.setTime(now);//设置当前日期
			calendar.add(Calendar.DAY_OF_MONTH, days*-1);
			
			int count=0;
			List<HospitalClaimDetail> detailList = groupProjectMap.get(limitProjectSortMap.get("FZ"));
			if(detailList==null)
			{
				continue;
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:detailList)
			{
				if(DateUtils.differentDays(calendar.getTime(),hospitalClaimDetail.getServiceDate())>=0)
				{
					count++;
				}
				
				if(count>limitNum)
				{
					violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, limitProjectSortMap.get("TSXX"));
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
