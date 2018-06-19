package com.shuxin.service.impl.ruleengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ProjectsMappingMapper;
import com.shuxin.mapper.ruleengine.RuleXMYXMPPMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 项目与项目匹配
 *
 */
@Service
public class RuleXMYXMPPServiceImpl implements IAnalysisRuleService{

	@Autowired
	private RuleXMYXMPPMapper ruleXMYXMPPMapper;
	
	@Autowired
	private ProjectsMappingMapper projectsMappingMapper;
	
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {

		List<HospitalClaimDetail> projectList =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		Map<String, List<HospitalClaimDetail>> projectMap=new HashMap<String, List<HospitalClaimDetail>>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
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
			
			if(projectMap.containsKey(dateFormat.format(hospitalClaimDetail.getServiceDate())))
			{
				projectMap.get(dateFormat.format(hospitalClaimDetail.getServiceDate())).add(hospitalClaimDetail);
			}
			else
			{
				projectList = new ArrayList<HospitalClaimDetail>();
				projectList.add(hospitalClaimDetail);
				projectMap.put(dateFormat.format(hospitalClaimDetail.getServiceDate()), projectList);
			}
		}
		
		if(projectMap.isEmpty())
		{
			return null;
		}
		
		Collection<List<HospitalClaimDetail>> projectCollection= projectMap.values();
		
		List<Map<String,String>> projectMappingInfo=ruleXMYXMPPMapper.selectProjectMappingInfo();
		
		List<Map<String,String>> bProjectMappingInfo =projectsMappingMapper.selectBProjectMappingInfoInHos();
		
		Iterator<List<HospitalClaimDetail>> iterator=projectCollection.iterator();
		List<String> bProjectCodes ;
		List<String> bProjectCodesHistory;
		List<Map<String, String>> projectMappingList;
		while(iterator.hasNext())
		{
			projectList=iterator.next();		
			
			
//			projectMappingList = ruleXMYXMPPMapper.selectProjectMappingInfo(projectList);
			projectMappingList=new ArrayList<Map<String,String>>();
			for(Map<String,String> projectMapping :projectMappingInfo)
			{
				for(HospitalClaimDetail hospitalClaimDetail : projectList)
				{
					if(hospitalClaimDetail.getProductCode().equals(projectMapping.get("PROJECT_CODE")))
					{
						projectMappingList.add(projectMapping);
					}
				}
			}
			
			if(projectMappingList.size()==0)
			{
				continue;
			}			
			
			 bProjectCodes = null;
			 bProjectCodesHistory = null;
			
			//判断就医方式是门诊
			if(hospitalClaim.getMedTreatmentMode().equals("11") || 
					hospitalClaim.getMedTreatmentMode().equals("13") || 
					hospitalClaim.getMedTreatmentMode().equals("15") || 
					hospitalClaim.getMedTreatmentMode().equals("51") || 
					hospitalClaim.getMedTreatmentMode().equals("71"))
			{				
				bProjectCodes = projectsMappingMapper.selectBProjectMappingInfo(projectList);
			}
			
			for(Map<String, String> projectMappingMap:projectMappingList)
			{
				if(Constants.Y_FLAG.equalsIgnoreCase(projectMappingMap.get("SFSHLXGB")))
				{
					if("3".equals(hospitalClaim.getPatType()))
					{
						continue;
					}
				}
				
				if(!Constants.N_FLAG.equalsIgnoreCase(projectMappingMap.get("XDKS")))
				{
					List<String> departments = Arrays.asList(projectMappingMap.get("XDKS").split(","));
					if(departments.contains(hospitalClaim.getInHospDeptCode()))
					{
						continue;
					}
				}
				
				//判断就医方式是门诊
				if(hospitalClaim.getMedTreatmentMode().equals("11") || 
					hospitalClaim.getMedTreatmentMode().equals("13") || 
					hospitalClaim.getMedTreatmentMode().equals("15") || 
					hospitalClaim.getMedTreatmentMode().equals("51") || 
					hospitalClaim.getMedTreatmentMode().equals("71"))
				{
					
					if(bProjectCodes.contains(projectMappingMap.get("DYXMBMZB")))
					{
						continue;
					}				
				}
				else 
				{
					if(Constants.N_FLAG.equals(projectMappingMap.get("CXBZDSJ")))
					{
						if(bProjectCodes==null)
						{
//							bProjectCodes = projectsMappingMapper.selectBProjectMappingInfo(projectList);
							bProjectCodes = new ArrayList<String>();
							for(Map<String,String> bProjectMapping:bProjectMappingInfo)
							{
								for(HospitalClaimDetail hospitalClaimDetail :projectList)
								{
									if(hospitalClaimDetail.getProductCode().equals(bProjectMapping.get("PROJECT_CODE")))
									{
										bProjectCodes.add(bProjectMapping.get("PROJECT_GROUP_CODE"));
									}
								}
							}
							
						}
						
						if(bProjectCodes.contains(projectMappingMap.get("DYXMBMZB")))
						{
							continue;
						}
					}
					else
					{
						if(bProjectCodesHistory == null)
						{
							Map<String,String> paramMap = new HashMap<String,String>();
							paramMap.put("patCode", hospitalClaim.getPatCode());
							paramMap.put("fromDate", dateFormat.format(hospitalClaim.getInHospDate()));
							if("1".equals(hospitalClaim.getLiveHospStatus()))
							{
								paramMap.put("toDate", dateFormat.format(hospitalClaim.getOutHospDate()));
							}
							else
							{
								paramMap.put("toDate", dateFormat.format(new Date()));
							}
							
							bProjectCodesHistory = projectsMappingMapper.selectBProjectMappingHistoryInfo(paramMap);						
						}
						if(bProjectCodesHistory.contains(projectMappingMap.get("DYXMBMZB")))
						{
							continue;
						}
					}
				}
				
				for(HospitalClaimDetail hospitalClaimDetail:projectList)
				{
					if(hospitalClaimDetail.getProductCode().equals(projectMappingMap.get("PROJECT_CODE")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, projectMappingMap.get("TSXX"));
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
						break;
					}
				}
				
			}
		}
		
		return list;
	}
	

}
