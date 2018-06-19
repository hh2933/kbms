package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.MinDrugTypeMapper;
import com.shuxin.mapper.ruleengine.RuleCLQYMapper;
import com.shuxin.mapper.ruleengine.RuleZFCLYYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 重复超量用药
 *
 */
@Service
public class RuleZFCLYYServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleZFCLYYMapper ruleZFCLYYMapper;
	
	@Autowired
	private MinDrugTypeMapper minDrugTypeMapper;
	
	@Autowired
	private RuleCLQYMapper ruleCLQYMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<String> drugCodeList = new ArrayList<String>();
		List<HospitalClaimDetail> drugPnumberList = new ArrayList<HospitalClaimDetail>();
		
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
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
			//相同药品不添加
			if(!drugCodeList.contains(hospitalClaimDetail.getProductCode()))
			{
				drugCodeList.add(hospitalClaimDetail.getProductCode());
			}
			
			drugPnumberList.add(hospitalClaimDetail);				
			
		}
		//如果明细里没有超过2种不同药器，不用审核该规则
		if(drugCodeList.size()<2)
		{
			return null;
		}
		
		//查询排外表信息
		List<String> exceptDrugCode= ruleZFCLYYMapper.selectExistsDrugCode(drugCodeList);
		
		//剔除排外表中的药品
		drugCodeList.removeAll(exceptDrugCode);
		
		//剔除后剩下的药品没有超过2种的，不用审核该规则
		if(drugCodeList.size()<2)
		{
			return null;
		}
		
		List<Map<String, String>> minDrugTypeList = minDrugTypeMapper.selectMinDrugTypeForDrugCode(drugCodeList);
		
		if(drugCodeList.size() != minDrugTypeList.size())
		{
			return null;
		}
		
		//将最小分类相同的药品整合在一起
		boolean isSameType=false;
		Map<String, List<String>> repeatMinDrugType = new HashMap<String,List<String>>();
		List<String> drugCodes=null;
		for(Map<String, String> minDrugTypeMap:minDrugTypeList)
		{
			if(repeatMinDrugType.containsKey(minDrugTypeMap.get("MINITYPE_CODE")))
			{			
				isSameType=true;
				repeatMinDrugType.get(minDrugTypeMap.get("MINITYPE_CODE")).add(minDrugTypeMap.get("DRUG_CODE"));
			}
			else
			{
				drugCodes= new ArrayList<String>();
				drugCodes.add(minDrugTypeMap.get("DRUG_CODE"));
				repeatMinDrugType.put(minDrugTypeMap.get("MINITYPE_CODE"), drugCodes);
			}
		}
		
		if(isSameType)
		{
			List<String> repeatDrugCodeList= new ArrayList<String>();
			Collection<List<String>> drugCodeCollection=repeatMinDrugType.values();
			Iterator<List<String>> iterator = drugCodeCollection.iterator();
			while (iterator.hasNext()) 
			{
				List<String> tempDdrugCodeList = iterator.next();
				if(tempDdrugCodeList.size()>1)
				{
					repeatDrugCodeList.addAll(tempDdrugCodeList);
				}
				
			}
			
			//判断就医方式是门诊
			if(hospitalClaim.getMedTreatmentMode().equals("11") || 
				hospitalClaim.getMedTreatmentMode().equals("13") || 
				hospitalClaim.getMedTreatmentMode().equals("15") || 
				hospitalClaim.getMedTreatmentMode().equals("51") || 
				hospitalClaim.getMedTreatmentMode().equals("71"))
			  {						
					return returnResult(rule, hospitalClaim,drugPnumberList,repeatDrugCodeList);
			  }
			else
			{
				return checkHospitalization(repeatDrugCodeList,rule, hospitalClaim,drugPnumberList);
			}
		}
		
		
		return null;
	}	
	
	/**
	 * 检查住院是否违规
	 * @param drugPnumber
	 * @return
	 */
	private List<ViolationDetail> checkHospitalization(List<String> drugCodeList,RuleTableInfo rule,
			HospitalClaim hospitalClaim,List<HospitalClaimDetail> hospitalClaimDetails)
	{
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		Map<String,Object> pMap = new HashMap<String,Object>();
		pMap.put("list", drugCodeList);
		//pMap.put("medTreatmentMode", hospitalClaim.getMedTreatmentMode());
		List<Map<String, String>> maxPrescriptionNumList=ruleCLQYMapper.selectMaxPrescriptionNum(pMap);
		if(maxPrescriptionNumList.size()==0)
		{
			return null;
		}
		
		for(Map<String,String> maxPrescriptionNumMap:maxPrescriptionNumList)
		{
			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
			{
				if(!maxPrescriptionNumMap.get("YPBM").equals(hospitalClaimDetail.getProductCode()))
				{
					continue;
				}
				float maxPrescriptionNum=Float.parseFloat(maxPrescriptionNumMap.get("RCFZDL"));
				if(hospitalClaimDetail.getPnumber()>maxPrescriptionNum)
				{
					violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "重复超量用药");
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
				}
			}
		}
		
		return list;
		
	}
	
	/**
	 * 组装返回参数
	 * @param rule
	 * @param hospitalClaim
	 * @param hospitalClaimDetails
	 * @param drugCodeList
	 * @return
	 */
	private List<ViolationDetail> returnResult(RuleTableInfo rule,HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails,List<String> drugCodeList)
	{
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(drugCodeList.contains(hospitalClaimDetail.getProductCode()))
			{
				violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "重复用药");
				if(list==null)
				{
					list= new ArrayList<ViolationDetail>();
				}
				list.add(violationDetail);
			}
		}
		return list;
	}	

}
