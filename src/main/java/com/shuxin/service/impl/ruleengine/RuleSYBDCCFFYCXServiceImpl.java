package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.DrugInfoMapper;
import com.shuxin.mapper.ruleengine.RuleSYBDCCFFYCXMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *省医保单次处方费用超限
 */
@Service
public class RuleSYBDCCFFYCXServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleSYBDCCFFYCXMapper ruleSYBDCCFFYCXMapper;
	
	@Autowired
	private DrugInfoMapper drugInfoMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		//只审核参保类型为省医保和就医方式为普通门诊的患者
		if(!"392".equals(hospitalClaim.getPatInsuredType())
				|| !"11".equals(hospitalClaim.getMedTreatmentMode()))
		{
			return null;
		}
		
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
		//中药饮品的药品编码
		List<String> productCodeList =new ArrayList<String>();
		productCodeList=ruleSYBDCCFFYCXMapper.selectByDrugcode();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			//只审核药品类型为西药和中成药 不审核中药饮品
			if(productCodeList.contains(hospitalClaimDetail.getProductCode()))
			{
				continue;
			}
			drugCodeList.add(hospitalClaimDetail);	
		}
		
		if(drugCodeList.size()==0)
		{
			return null;
		}
		
		Map<String, Object> paramMap= new HashMap<String, Object>();
		
		paramMap.put("ruleType", "2");
		paramMap.put("list", drugCodeList);
		List<String> exceptDrugCodes=drugInfoMapper.selectExceptDrugInfo(paramMap);
		
		//去掉被排除的药品
		if(exceptDrugCodes.size()>0)
		{
			Iterator<HospitalClaimDetail> iterator = drugCodeList.iterator();
			while (iterator.hasNext())
			{
				HospitalClaimDetail hospitalClaimDetail =  iterator.next();
				if(exceptDrugCodes.contains(hospitalClaimDetail.getProductCode()))
				{
					iterator.remove();
				}
			}
		}		
		
		if(drugCodeList.size()==0)
		{
			return null;
		}
		
		Map<String, String> pMedicalInsuranceMap = ruleSYBDCCFFYCXMapper.selectProvincialMedicalInsuranceInfo();
		
		BigDecimal drugAmount = new BigDecimal(0);
		
		for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
		{
			drugAmount = drugAmount.add(hospitalClaimDetail.getAmount());
		}
		
		if(drugAmount.compareTo(new BigDecimal(pMedicalInsuranceMap.get("YPZFY")))==1)
		{
			ViolationDetail violationDetail=ToolUtils.getViolationDetail3(rule, hospitalClaim, rule.getId(),rule.getMenuName(), "本次处方总费用超出规定费用",drugAmount);	
			
			List<ViolationDetail> list= new ArrayList<ViolationDetail>();
			
			list.add(violationDetail);
			return list;
		}
		
		return null;
	}

}
