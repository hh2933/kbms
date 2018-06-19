package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleMGBZFYZESHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *门规病种费用总额审核
 */
@Service
public class RuleMGBZFYZESHServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleMGBZFYZESHMapper ruleMGBZFYZESHMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		//只审核门规病患者
		if(!"13".equals(hospitalClaim.getMedTreatmentMode()))
		{
			return null;
		}	
	
		List<String>  diagnosisCodeList = new ArrayList<String>();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(!StringUtils.isEmpty(hospitalClaimDetail.getMgbz())
				&&!diagnosisCodeList.contains(hospitalClaimDetail.getMgbz()))
			{
				diagnosisCodeList.add(hospitalClaimDetail.getMgbz());
			}
		}
		
		if(diagnosisCodeList.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> specialDiseasesList= ruleMGBZFYZESHMapper.selectSpecialDiseasesInfo(diagnosisCodeList);
		
		if(specialDiseasesList.size()==0)
		{
			return null;
		}
		
		String years="N";
		
		Map<String, String> paramMap=new HashMap<String,String>();
		paramMap.put("patCode", hospitalClaim.getPatCode());		
		String diagnosisCode="";
//		BigDecimal yearsAmount=new BigDecimal(0);
		BigDecimal monthAmount=new BigDecimal(0);
		boolean flag=false;
		String tip="";
		
		for(Map<String, String>specialDiseasesMap:specialDiseasesList)
		{
			if(!diagnosisCode.equals(specialDiseasesMap.get("DIAGNOSIS_CODE")))
			{
				diagnosisCode=specialDiseasesMap.get("DIAGNOSIS_CODE");
				paramMap.put("diaInHospCode", diagnosisCode);
				paramMap.put("dateType", "month");
				monthAmount=hospitalClaimDetailMapper.selectSpecialPatientDrugAmount(paramMap);
				
//				paramMap.put("dateType", "year");
//				yearsAmount=hospitalClaimDetailMapper.selectSpecialPatientDrugAmount(paramMap);
			}
//			BigDecimal specialYearAmount=new BigDecimal(specialDiseasesMap.get("XDJEY"));
			BigDecimal specialMonthAmount=new BigDecimal(specialDiseasesMap.get("YXDJEY"));
			
			if(Constants.N_FLAG.equalsIgnoreCase(specialDiseasesMap.get("XDSJQJLX")))
			{
//				if(yearsAmount.compareTo(specialYearAmount)>0)
//				{
//					flag=true;
//					tip=specialDiseasesMap.get("TSXX");
//					break;
//				}
				if(monthAmount.compareTo(specialMonthAmount)>0)
				{
					flag=true;
					tip=specialDiseasesMap.get("YTSXX");
					break;
				}
			}
			else
			{
				if(Constants.N_FLAG.equals(years))
				{
//					DynamicDataSourceHolder.setDataSource("dataSource_ybkf");
					years=ruleMGBZFYZESHMapper.selectSpecialPatientYears(hospitalClaim.getPatIdCard());
//					DynamicDataSourceHolder.clearDataSource();
					if(Float.parseFloat(years)>=0&&Float.parseFloat(years)<=1*365){
						years="1";
					}else if(Float.parseFloat(years)>1*365&&Float.parseFloat(years)<=2*365){
						years="2";
					}else if(Float.parseFloat(years)>2*365&&Float.parseFloat(years)<=3*365){
						years="3";
					}else if(Float.parseFloat(years)>3*365&&Float.parseFloat(years)<=5*365){
						years="4";
					}else{
						years="5";
					}
				}
				
				if(specialDiseasesMap.get("XDSJQJLX").equals(years))
				{
//					if(yearsAmount.compareTo(specialYearAmount)>0)
//					{
//						flag=true;
//						tip=specialDiseasesMap.get("TSXX");
//						break;
//					}
					if(monthAmount.compareTo(specialMonthAmount)>0)
					{
						flag=true;
						tip=specialDiseasesMap.get("YTSXX");
						break;
					}
				}
			}
		}
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		if(flag)
		{
//			for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
//			{
//								
//				violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, tip);
			violationDetail=ToolUtils.getViolationDetail3(rule, hospitalClaim, rule.getId(),rule.getMenuName(), tip,monthAmount);	
			if(list==null)
			{
				list= new ArrayList<ViolationDetail>();
			}
			list.add(violationDetail);
//			}
		}
		
		
		return list;
	}

}
