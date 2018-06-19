package com.shuxin.commons.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;

public class ToolUtils {
	
	/**
	 * 得到所有诊断编码
	 * @param hospitalClaim
	 * @return
	 */
	public static List<String> getAllAiagnosisCode(HospitalClaim hospitalClaim)
	{
		List<String> diagnosisCodeList = new ArrayList<String>();
		
//		diagnosisCodeList.add(hospitalClaim.getDiaInHospCode());
//		
//		if(StringUtils.isNotBlank(hospitalClaim.getDiaOutHospCode()))
//		{
//			diagnosisCodeList.add(hospitalClaim.getDiaOutHospCode());
//		}
		
		if(StringUtils.isNotBlank(hospitalClaim.getDiaViceCode()))
		{
			String[] diaViceCodes = hospitalClaim.getDiaViceCode().split("\\|");
			for(String diaViceCode:diaViceCodes)
			{
				diagnosisCodeList.add(diaViceCode);
			}
		}
		return diagnosisCodeList;
	}
	
	public static ViolationDetail getViolationDetail(RuleTableInfo rule,HospitalClaim hospitalClaim,
			HospitalClaimDetail hospitalClaimDetail,String prompt )
	{
		SimpleDateFormat simpleDateFormat =   new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		ViolationDetail violationDetail = new ViolationDetail();
		violationDetail.setDetailCode(UUID.randomUUID().toString().replace("-", ""));
		violationDetail.setRuleName(rule.getMenuName());
		violationDetail.setRuleType(rule.getRuleType());
		violationDetail.setBillStatus(rule.getResultType());
		violationDetail.setPatId(hospitalClaim.getPatCode());
		violationDetail.setProductCode(hospitalClaimDetail.getProductCode());
		violationDetail.setProductName(hospitalClaimDetail.getProductName());
		violationDetail.setAgainstAmount(hospitalClaimDetail.getAmount().toString());
		violationDetail.setAgainstNum(String.valueOf(hospitalClaimDetail.getPnumber()));
		violationDetail.setAgainstCode(hospitalClaim.getDiaSerialCode());
		violationDetail.setMessageCode(String.valueOf(rule.getRuleCode()));
		violationDetail.setServiceDate(simpleDateFormat.format(hospitalClaimDetail.getServiceDate()));
		violationDetail.setDocSerialType(hospitalClaimDetail.getDocSerialType());
		violationDetail.setDocSerialCode(hospitalClaimDetail.getDocSerialCode());
		violationDetail.setDiaViceName(hospitalClaim.getDiaViceName());
		violationDetail.setMessage(prompt);
		return violationDetail;
	}
		
	public static ViolationDetail getViolationDetail2(RuleTableInfo rule,HospitalClaim hospitalClaim,
			String diagnosisCode,String diagnosisName,String prompt )
	{
		SimpleDateFormat simpleDateFormat =   new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		ViolationDetail violationDetail = new ViolationDetail();
		violationDetail.setDetailCode(UUID.randomUUID().toString().replace("-", ""));
		violationDetail.setRuleName(rule.getMenuName());
		violationDetail.setRuleType(rule.getRuleType());
		violationDetail.setBillStatus(rule.getResultType());
		violationDetail.setPatId(hospitalClaim.getPatCode());
		violationDetail.setProductCode(diagnosisCode);
		violationDetail.setAgainstAmount("");
		violationDetail.setAgainstNum("");
		violationDetail.setProductName(diagnosisName);
		violationDetail.setAgainstCode(hospitalClaim.getDiaSerialCode());
		violationDetail.setMessageCode(String.valueOf(rule.getRuleCode()));
		violationDetail.setServiceDate(simpleDateFormat.format(date));
		violationDetail.setDiaViceName(hospitalClaim.getDiaViceName());
		violationDetail.setMessage(prompt);
		return violationDetail;
	}
	
	/**
	 * 针对门规病
	 * @param rule
	 * @param hospitalClaim
	 * @param diagnosisCode
	 * @param diagnosisName
	 * @param prompt
	 * @return
	 */
	public static ViolationDetail getViolationDetail3(RuleTableInfo rule,HospitalClaim hospitalClaim,
			String diagnosisCode,String diagnosisName,String prompt ,BigDecimal monthAmount)
	{
		SimpleDateFormat simpleDateFormat =   new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		ViolationDetail violationDetail = new ViolationDetail();
		violationDetail.setDetailCode(UUID.randomUUID().toString().replace("-", ""));
		violationDetail.setRuleName(rule.getMenuName());
		violationDetail.setRuleType(rule.getRuleType());
		violationDetail.setBillStatus(rule.getResultType());
		violationDetail.setPatId(hospitalClaim.getPatCode());
		violationDetail.setProductCode(diagnosisCode);
		violationDetail.setAgainstAmount(monthAmount.toString());
		violationDetail.setAgainstNum("");
		violationDetail.setProductName(diagnosisName);
		violationDetail.setAgainstCode(hospitalClaim.getDiaSerialCode());
		violationDetail.setMessageCode(String.valueOf(rule.getRuleCode()));
		violationDetail.setServiceDate(simpleDateFormat.format(date));
		violationDetail.setDiaViceName(hospitalClaim.getDiaViceName());
		violationDetail.setMessage(prompt);
		return violationDetail;
	}


}
