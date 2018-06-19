package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleTQQYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 提前取药（违规/可疑）
 *
 */
@Service
public class RuleTQQYServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleTQQYMapper ruleTQQYMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	
	protected Logger logger = LogManager.getLogger(getClass());
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<HospitalClaimDetail> drugList = new ArrayList<HospitalClaimDetail>();
		List<String> idList = new ArrayList<String>();
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
			
			drugList.add(hospitalClaimDetail);
			idList.add(hospitalClaimDetail.getId());
		}
		
		if(drugList.size()==0)
		{
			return null;
		}
		
		List<Map<String, Object>> advanceMedicineList = null;
		
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("list", drugList);
//		pMap.put("medTreatmentMode", hospitalClaim.getMedTreatmentMode());
		
		//判断是违规还是可疑
		if("1".equals(rule.getResultType()))
		{
			advanceMedicineList = ruleTQQYMapper.selectAdvanceMedicineIllegal(pMap);
		}
		else
		{
			advanceMedicineList = ruleTQQYMapper.selectAdvanceMedicineSuspicious(pMap);
		}
		
		if(advanceMedicineList.size()==0)
		{
			return null;
		}
		
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("list", idList);
		
		String prompt="";
		Map<String, Object> lastDrugMap = null;
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd"); 
		for(HospitalClaimDetail hospitalClaimDetail:drugList)
		{
			for(Map<String, Object> advanceMedicine:advanceMedicineList)
			{
				if(!hospitalClaimDetail.getProductCode().
						equals((String)advanceMedicine.get("YPBM")))
				{
					continue;
				}
				
				paramMap.put("productCode", hospitalClaimDetail.getProductCode());
				lastDrugMap=hospitalClaimDetailMapper.selectLastClaimDetail(paramMap);
				if(lastDrugMap==null || lastDrugMap.isEmpty())
				{
					break;
				}
				
				BigDecimal minPackage = (BigDecimal)advanceMedicine.get("ZXBZ");
				BigDecimal dayMaxDose = (BigDecimal)advanceMedicine.get("RZDJL");
				int limitDays = ((BigDecimal)advanceMedicine.get("TQTS")).intValue();
				String specialLimitDays =(String)advanceMedicine.get("BZXDTQTS");
				BigDecimal lastDrugCount = (BigDecimal)lastDrugMap.get("PNUMBER");
				
				int totalDrugDays = minPackage.divide(dayMaxDose,0, BigDecimal.ROUND_HALF_UP).multiply(lastDrugCount).intValue();
				int advanceDay = limitDays;
				//判断是否是门特病，和病种限定提前天数字段不等于'N'
				if(!Constants.N_FLAG.equalsIgnoreCase(specialLimitDays)
						&&	hospitalClaim.getMedTreatmentMode().equals("13"))
				{
					advanceDay=Integer.parseInt(specialLimitDays);
				}
				Date lastServiceDate =(Date)lastDrugMap.get("SERVICEDATE");
				long properDrugDays = lastServiceDate.getTime()+(totalDrugDays-advanceDay)* 24*60*60*1000L;
				long today = (new Date()).getTime();
					if(properDrugDays>today)
					{
						long betweenDays = (properDrugDays-today)/(1000*3600*24)+advanceDay;
						prompt = "该患者在"+dateFormat.format(lastServiceDate)+"开"+hospitalClaimDetail.getProductName()+totalDrugDays+"天量，于今日开药提前了"+betweenDays+"天(可提前"+advanceDay+"天取药)";
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
						Constants.ruleExamineResult.put(hospitalClaim.getDiaSerialCode(), "true");
					}
			}
		}
		
		return list;
	}	

}
