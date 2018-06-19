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
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.DrugInfoMapper;
import com.shuxin.mapper.ruleengine.RuleDCCFYPZLYCMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *单次处方药品种类异常
 */
@Service
public class RuleDCCFYPZLYCServiceImpl implements IAnalysisRuleService {

	@Autowired
	private DrugCatalogMapper drugCatalogMapper;
	
	@Autowired
	private RuleDCCFYPZLYCMapper ruleDCCFYPZLYCMapper;
	
	@Autowired
	private DrugInfoMapper drugInfoMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= new ArrayList<ViolationDetail>();
		
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
			
			drugCodeList.add(hospitalClaimDetail);			
			
		}
		
		if(drugCodeList.size() == 0)
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
		
		Map<String, BigDecimal> typeException=ruleDCCFYPZLYCMapper.selectRuleDCCFYPZLYC();
		
		int westernMedicine = typeException.get("XYZLSL").intValue();
		int chineseMedicine = typeException.get("ZCYZLSL").intValue();
		int chinesePieces = typeException.get("ZYYPZLSL").intValue();
		
		//如果药品总数都没有超过这三个指标，审核通过
		if(drugCodeList.size()<westernMedicine&&
				drugCodeList.size()<chineseMedicine&&
				drugCodeList.size()<chinesePieces)
		{
			return null;
		}
		
		List<Map<String, String>> drugTypeList=drugCatalogMapper.selectDrugType(drugCodeList);
		
		List<Map<String, String>> westernMedicineList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> chineseMedicineList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> chinesePiecesList = new ArrayList<Map<String, String>>();
		
		for(Map<String, String> drugTypeMap:drugTypeList)
		{
			
			if("1".equals(drugTypeMap.get("DRUG_TYPE")))
			{
				westernMedicineList.add(drugTypeMap);
			}
			else if("2".equals(drugTypeMap.get("DRUG_TYPE")))
			{
				chineseMedicineList.add(drugTypeMap);
			}
			else 
			{
				chinesePiecesList.add(drugTypeMap);
			}
		}
		String prompt="";
		if(westernMedicineList.size()>westernMedicine)
		{
			prompt="西药种类超过"+westernMedicine+"种";
			//setResultList(rule, hospitalClaim,list,drugCodeList,westernMedicineList.subList(westernMedicine, westernMedicineList.size()),prompt);
		}		
		else if(chineseMedicineList.size()>chineseMedicine)
		{
			prompt="中成药超过"+chineseMedicine+"种";
			//setResultList(rule, hospitalClaim,list,drugCodeList,chineseMedicineList.subList(chineseMedicine, chineseMedicineList.size()),prompt);
		}		
		else if(chinesePiecesList.size()>chinesePieces)
		{
			prompt="中药饮片超过"+chinesePieces+"味";
			//setResultList(rule, hospitalClaim,list,drugCodeList,chinesePiecesList.subList(chinesePieces, chinesePiecesList.size()),prompt);
		}
		
		if(!StringUtils.isEmpty(prompt))
		{
			ViolationDetail violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), prompt);
			list.add(violationDetail);
		}
		
		return list;
	}	
	
	/**
	 * 组装结果
	 * @param rule
	 * @param hospitalClaim
	 * @param list
	 * @param drugCodeList
	 * @param drugTypeList
	 * @param prompt
	 */
	private void setResultList(RuleTableInfo rule, HospitalClaim hospitalClaim,List<ViolationDetail> list,
			List<HospitalClaimDetail> drugCodeList,List<Map<String, String>> drugTypeList,
			String prompt)
	{
		ViolationDetail violationDetail =null;
		for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
		{
			for(Map<String, String> drugTypeMap:drugTypeList)
			{
				if(hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("DRUG_CODE"))
						||hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("NEW_DRUG_CODE")))
				{
					
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
					
					list.add(violationDetail);
//					break;
				}
			}
			
		}
	}

}
