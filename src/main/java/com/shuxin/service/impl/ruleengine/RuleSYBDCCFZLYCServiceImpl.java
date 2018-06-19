package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.DrugInfoMapper;
import com.shuxin.mapper.ruleengine.RuleSYBDCCFZLYCMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 * 省医保单次处方种类异常
 *
 */
@Service
public class RuleSYBDCCFZLYCServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleSYBDCCFZLYCMapper ruleSYBDCCFZLYCMapper;
	
	@Autowired
	private DrugInfoMapper drugInfoMapper;

	@Autowired
	private DrugCatalogMapper drugCatalogMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		//只审核参保类型为省医保患者
		if(!"392".equals(hospitalClaim.getPatInsuredType()))
		{
			return null;
		}
		
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
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
		
		Map<String, String> pMedicalInsuranceMap = ruleSYBDCCFZLYCMapper.selectProvincialMedicalInsuranceInfo();
		
		List<Map<String, String>> drugTypeList=drugCatalogMapper.selectDrugType(drugCodeList);
		
		List<Map<String, String>> westernDrugList=new ArrayList<Map<String, String>> ();
		List<Map<String, String>> chineseDrugList=new ArrayList<Map<String, String>> ();
		List<Map<String, String>> chinesePiecesList=new ArrayList<Map<String, String>> ();
		
		for(Map<String, String> drugTypeMap:drugTypeList)
		{
			if("1".equals(drugTypeMap.get("DRUG_TYPE")))
			{
				westernDrugList.add(drugTypeMap);
			}
			else if("2".equals(drugTypeMap.get("DRUG_TYPE")))
			{
				chineseDrugList.add(drugTypeMap);
			}
			else 
			{
				chinesePiecesList.add(drugTypeMap);
			}
		}
		
		String tip="";
		
		if(westernDrugList.size()>Integer.parseInt(pMedicalInsuranceMap.get("XYZLSL")))
		{
			tip="西药种类超过"+pMedicalInsuranceMap.get("XYZLSL")+"种";
		}		
		else if(chineseDrugList.size()>Integer.parseInt(pMedicalInsuranceMap.get("ZCYZLSL")))
		{
			tip="中成药种类超过"+pMedicalInsuranceMap.get("ZCYZLSL")+"种";
		}		
		else if(chinesePiecesList.size()>Integer.parseInt(pMedicalInsuranceMap.get("ZYYPZLSL")))
		{
			tip="中药饮片种类数量超过"+pMedicalInsuranceMap.get("ZYYPZLSL")+"味";
		}		
		
		if(StringUtils.isEmpty(tip))
		{
			return null;
		}
		
		ViolationDetail violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), tip);	
		
		List<ViolationDetail> list= new ArrayList<ViolationDetail>();
		
		list.add(violationDetail);
		
		return list;
		
	}

}
