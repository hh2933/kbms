package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleCYDYCLSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 * 出院带药超量审核
 *
 */
@Service
public class RuleCYDYCLSHServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleCYDYCLSHMapper ruleCYDYCLSHMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= new ArrayList<ViolationDetail>();
		List<HospitalClaimDetail> drugCodeListTemp =new ArrayList<HospitalClaimDetail>();
		List <String> productCodeTemp =new ArrayList<String>();
		
		
		ViolationDetail violationDetail =null;
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有出院带的药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType())||
					!"1".equals(hospitalClaimDetail.getOutHospTakMedicine()))
			{
				continue;
			}			
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				drugCodeListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			drugCodeList.add(hospitalClaimDetail);			
		}
		
		if(drugCodeList.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> outHospitalDrugList = ruleCYDYCLSHMapper.selectOutHospitalDrugInfo(drugCodeListTemp);
		
		if(outHospitalDrugList.size()==0)
		{
			return null;
		}
		
		for(Map<String, String> outHospitalDrugMap:outHospitalDrugList)
		{
			if(!Constants.N_FLAG.equalsIgnoreCase(outHospitalDrugMap.get("XDCBLX")))
			{
				List<String> insuredTypeList=Arrays.asList(outHospitalDrugMap.get("XDCBLX").split(","));
				if(insuredTypeList.contains(hospitalClaim.getPatInsuredType()))	
				{
					continue;
				}				
			}
			
			for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
			{
				if(hospitalClaimDetail.getProductCode().equals(outHospitalDrugMap.get("YPBM")))
				{
					BigDecimal limitMaxNum = new BigDecimal(outHospitalDrugMap.get("MAXNUM"));
					BigDecimal maxNum=new BigDecimal(String.valueOf(hospitalClaimDetail.getPnumber()*Float.valueOf(outHospitalDrugMap.get("ZXBZSL"))));
					if(maxNum.compareTo(limitMaxNum)==1)
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "该药品出院带药量不得超过"+outHospitalDrugMap.get("MRXDTS")+"天");
						list.add(violationDetail);
						break;
					}
				}
			}
		}
		
		
		return list;
	}

}
