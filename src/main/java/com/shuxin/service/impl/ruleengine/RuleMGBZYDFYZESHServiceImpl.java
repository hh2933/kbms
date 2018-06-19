package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.datasource.DynamicDataSourceHolder;
import com.shuxin.commons.utils.Charsets;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.MIDiagnosisMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleMGBZYDFYZESHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *门规综合病种费用总额审核
 */
@Service
public class RuleMGBZYDFYZESHServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleMGBZYDFYZESHMapper ruleMGBZYDFYZESHMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	@Autowired
	private MIDiagnosisMapper mIDiagnosisMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		
		//只审核门规病患者
		if(!"13".equals(hospitalClaim.getMedTreatmentMode()))
		{
			return null;
		}
		
		
		
		List<String> diagnosisCodeList1 = new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(!StringUtils.isEmpty(hospitalClaimDetail.getMgbz())
				&&!diagnosisCodeList1.contains(hospitalClaimDetail.getMgbz()))
			{
				diagnosisCodeList1.add(hospitalClaimDetail.getMgbz());
			}
		}
		if(diagnosisCodeList1.size()==0)
		{
			return null;
		}
		List<String> aiagnosisCodeInfoList =mIDiagnosisMapper.selectBZ31DiagnosisCodeInfo(diagnosisCodeList1);
		
		if(aiagnosisCodeInfoList.size()==0)
		{
			return null;
		}
		
		List<String> diagnosisCodeList = new ArrayList<String>();
		//身份证号码
		String patIdCard=hospitalClaim.getPatIdCard();
		List<String> diagnosisCode=new ArrayList<String>();
//		DynamicDataSourceHolder.setDataSource("dataSource_ybkf");
		diagnosisCode=ruleMGBZYDFYZESHMapper.selectDiagnosisCodeByPatIdCard(patIdCard);
//		DynamicDataSourceHolder.clearDataSource();
		if(diagnosisCode.size()==0)
		{
			return null;
		}
		String tempDiagnosisCode=null;
		for(int i=0;i<diagnosisCode.size();i++){
			tempDiagnosisCode=diagnosisCode.get(i).split(",")[0].trim();
			if(!diagnosisCodeList.contains(tempDiagnosisCode)){
				diagnosisCodeList.add(tempDiagnosisCode);
			}
		}

		
		if(diagnosisCodeList.size()==0)
		{
			return null;
		}
		
		int aiagnosisCodeCount=mIDiagnosisMapper.selectBZ31DiagnosisCodeCount(diagnosisCodeList);
		if(aiagnosisCodeCount==0)
		{
			return null;
		}
		else if(aiagnosisCodeCount>2)
		{
			aiagnosisCodeCount=2;
		}
		
		Map<String, String> multipleSpecialDiseasesMap = ruleMGBZYDFYZESHMapper.selectSpecialMultipleDiseasesInfo(aiagnosisCodeCount);
		
		if(multipleSpecialDiseasesMap.size()==0)
		{
			return null;
		}
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
		paramMap.put("patCode", hospitalClaim.getPatCode());	
		BigDecimal monthAmount=new BigDecimal(0);
		String tip="";
		BigDecimal specialMonthAmount=new BigDecimal(multipleSpecialDiseasesMap.get("YXDJE"));
		paramMap.put("dateType", "month");
		paramMap.put("list", aiagnosisCodeInfoList);
		
		monthAmount=hospitalClaimDetailMapper.selectSpecialPatientDrugAmountOne(paramMap);
		if(monthAmount.compareTo(specialMonthAmount)>0)
		{
			tip=multipleSpecialDiseasesMap.get("YTSXX");
		}
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		
		if(!StringUtils.isEmpty(tip))
		{
			violationDetail=ToolUtils.getViolationDetail3(rule, hospitalClaim, rule.getId(),rule.getMenuName(), tip,monthAmount);	
			if(list==null)
			{
				list= new ArrayList<ViolationDetail>();
			}
			list.add(violationDetail);
		}			
		
		return list;
	}

}
