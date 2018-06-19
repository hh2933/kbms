package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.DateUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleXDPCMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限定频次（违规/可疑）
 *
 */
@Service
public class RuleXDPCServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleXDPCMapper ruleXDPCMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<HospitalClaimDetail> projectList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> projectListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只审核项目，不审核药品
			if("1".equals(hospitalClaimDetail.getThrCatType()))
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
		
		List<Map<String, Object>> limitFrequencyList=null;
		
		//判断是违规还是可疑
		if("1".equals(rule.getResultType()))
		{
			limitFrequencyList = ruleXDPCMapper.selectLimitFrequencyIllegal(projectListTemp);
		}
		else
		{
			limitFrequencyList = ruleXDPCMapper.selectLimitFrequencySuspicion(projectListTemp);
		}
		
		if(limitFrequencyList.size()==0)
		{
			return null;
		}
		
		for(Map<String, Object> limitFrequencyMap:limitFrequencyList)
		{
			//判断不需要审核的科室
			if(!Constants.N_FLAG.equalsIgnoreCase
					((String)limitFrequencyMap.get("SFSHMZJXDKS")))
			{
				boolean departmentFlag=false;
				String[] departmentCodes =((String)limitFrequencyMap.get("SFSHMZJXDKS")).split(",");
				for(String departmentCode:departmentCodes)
				{
					if(departmentCode.equals(hospitalClaim.getInHospDeptCode()))
					{
						departmentFlag=true;
						break;
					}
				}
				if(departmentFlag)
				{
					continue;
				}
			}
			
			if("1".equals((String)limitFrequencyMap.get("SJJGLX")))
			{
				//只审核住院
				if(hospitalClaim.getMedTreatmentMode().equals("21") || 
						hospitalClaim.getMedTreatmentMode().equals("22") || 
						hospitalClaim.getMedTreatmentMode().equals("25") || 
						hospitalClaim.getMedTreatmentMode().equals("52") || 
						hospitalClaim.getMedTreatmentMode().equals("72"))
				{						
					violationDetail = onceCalculation(rule, hospitalClaim, projectList,limitFrequencyMap);
					if(violationDetail!=null)
					{
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
					}
				}
				continue;
			}
			
			
			for(HospitalClaimDetail hospitalClaimDetail:projectList)
			{
				if(!hospitalClaimDetail.getProductCode().
						equals((String)limitFrequencyMap.get("XMBM")))
				{
					continue;
				}			
				
				
				if("3".equals((String)limitFrequencyMap.get("SJJGLX")))
				{
					BigDecimal limitCount=(BigDecimal)limitFrequencyMap.get("MZXDCS");
					//判断是否住院
					if(hospitalClaim.getMedTreatmentMode().equals("21") || 
							hospitalClaim.getMedTreatmentMode().equals("22") || 
							hospitalClaim.getMedTreatmentMode().equals("25") || 
							hospitalClaim.getMedTreatmentMode().equals("52") || 
							hospitalClaim.getMedTreatmentMode().equals("72")){						
							limitCount = (BigDecimal)limitFrequencyMap.get("ZYXDCS");						
					}
					
					if(hospitalClaimDetail.getPnumber()>limitCount.floatValue())
					{
						violationDetail =ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, (String)limitFrequencyMap.get("TSXX"));
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
					}
				}
				else if("4".equals((String)limitFrequencyMap.get("SJJGLX")))
				{
					violationDetail = fixedDateInterval(rule, hospitalClaim, hospitalClaimDetail,limitFrequencyMap);
					if(violationDetail!=null)
					{
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
					}
				}
				
//				break;
			}
			
		}
		
		return list;
	}
	
	/**
	 * 一次计算（出入院，本单据）
	 * @param rule
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 * @param limitFrequencyMap
	 * @return
	 */
	private ViolationDetail onceCalculation(RuleTableInfo rule,HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails,Map<String, Object> limitFrequencyMap)
	{
		float frequentlyNumber=0;
		HospitalClaimDetail durgDetail=null;
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(hospitalClaimDetail.getProductCode().
					equals((String)limitFrequencyMap.get("XMBM")))
			{
				frequentlyNumber+=hospitalClaimDetail.getPnumber();
				if(durgDetail!=null)
				{
					if(durgDetail.getServiceDate().getTime()<hospitalClaimDetail.getServiceDate().getTime())
					{
						durgDetail=hospitalClaimDetail;
					}
				}
				else
				{
					durgDetail=hospitalClaimDetail;
				}				
			}
		}
		
		Date date = new Date();
		
		if("1".equals(hospitalClaim.getLiveHospStatus()))
		{
			date = hospitalClaim.getOutHospDate();
		}
		
		int differentDays = DateUtils.differentDays(hospitalClaim.getInHospDate(),date);
		
		if(Constants.Y_FLAG.equalsIgnoreCase
				((String)limitFrequencyMap.get("ZYTSSTSW")))
		{
			differentDays++;
		}
		ViolationDetail violationDetail = null;
		BigDecimal  frequentlyNumberPerDay=new BigDecimal(Math.ceil(frequentlyNumber/differentDays));
		BigDecimal limitFrequency =new BigDecimal(limitFrequencyMap.get("ZYXDCS").toString());
		if(frequentlyNumberPerDay.compareTo(limitFrequency)>0)
		{
			violationDetail =ToolUtils.getViolationDetail(rule, hospitalClaim, durgDetail, (String)limitFrequencyMap.get("TSXX"));
		}
		return violationDetail;
	}
	
	
	/**
	 * 一次计算（出入院，本单据）
	 * @param rule
	 * @param hospitalClaim
	 * @param hospitalClaimDetail
	 * @param limitFrequencyMap
	 * @return
	 */
	private ViolationDetail onceCalculation(RuleTableInfo rule,HospitalClaim hospitalClaim,
			HospitalClaimDetail hospitalClaimDetail,Map<String, Object> limitFrequencyMap)
	{
		ViolationDetail violationDetail = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("productCode", hospitalClaimDetail.getProductCode());
		paramMap.put("fromDate", format.format(hospitalClaim.getInHospDate()));
		Date date = new Date();
		
		if("1".equals(hospitalClaim.getLiveHospStatus()))
		{
			date = hospitalClaim.getOutHospDate();
		}
		
		paramMap.put("toDate", format.format(date));
		
		BigDecimal frequentlyNumber = hospitalClaimDetailMapper.selectFrequentlyDrugNumber(paramMap);
		
		int differentDays = DateUtils.differentDays(hospitalClaim.getInHospDate(),date);
		
		if(Constants.Y_FLAG.equalsIgnoreCase
				((String)limitFrequencyMap.get("ZYTSSTSW")))
		{
			differentDays++;
		}
		
		if(frequentlyNumber.compareTo(new BigDecimal(differentDays))>-1)
		{
			violationDetail =ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, (String)limitFrequencyMap.get("TSXX"));
		}
		return violationDetail;
	}
	
	/**
	 * 固定时间间隔
	 * @param rule
	 * @param hospitalClaim
	 * @param hospitalClaimDetail
	 * @param limitFrequencyMap
	 * @return
	 */
	private ViolationDetail fixedDateInterval(RuleTableInfo rule,HospitalClaim hospitalClaim,
			HospitalClaimDetail hospitalClaimDetail,Map<String, Object> limitFrequencyMap)
	{
		ViolationDetail violationDetail = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("productCode", hospitalClaimDetail.getProductCode());
		paramMap.put("toDate", format.format(hospitalClaimDetail.getServiceDate()));
		int intervalDays = Integer.parseInt(((String)limitFrequencyMap.get("SJJGT")));
		if(Constants.N_FLAG.equalsIgnoreCase
				((String)limitFrequencyMap.get("ZYTSSTSW")))
		{
			intervalDays--;
		}
		paramMap.put("intervalDays", String.valueOf(intervalDays));
		BigDecimal frequentlyNumber = hospitalClaimDetailMapper.selectFrequentlyDrugNumber(paramMap);
		
		
		BigDecimal limitCount=(BigDecimal)limitFrequencyMap.get("MZXDCS");
		//判断是否住院
		if(hospitalClaim.getMedTreatmentMode().equals("21") || 
				hospitalClaim.getMedTreatmentMode().equals("22") || 
				hospitalClaim.getMedTreatmentMode().equals("25") || 
				hospitalClaim.getMedTreatmentMode().equals("52") || 
				hospitalClaim.getMedTreatmentMode().equals("72")){
			limitCount = (BigDecimal)limitFrequencyMap.get("ZYXDCS");
		}
		
		if(frequentlyNumber.compareTo(limitCount)==1)
		{
			violationDetail =ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, (String)limitFrequencyMap.get("TSXX"));
		}
		return violationDetail;
	}
	
}
