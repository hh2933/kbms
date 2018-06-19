package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.DrugCatalogMapper;
import com.shuxin.mapper.ruleengine.RuleCYDYZLSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 * 出院带药种类审核
 *
 */
@Service
public class RuleCYDYZLSHServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleCYDYZLSHMapper ruleCYDYZLSHMapper;
	
	@Autowired
	private DrugCatalogMapper drugCatalogMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {

		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> drugCodeListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp=new ArrayList<String>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= new ArrayList<ViolationDetail>();
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
		
		if(drugCodeList.size()<2)
		{
			return null;
		}
		
		List<Map<String, String>> drugTypeList=drugCatalogMapper.selectDrugType(drugCodeListTemp);
		
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
		
		//中成药和中药饮片不能同时存在
		if(chineseDrugList.size()>0&&chinesePiecesList.size()>0)
		{
			for(Map<String, String> drugTypeMap:chinesePiecesList)
			{
				for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
				{
					if(hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("DRUG_CODE"))
						|| hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("NEW_DRUG_CODE")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "出院带药种类异常");
						list.add(violationDetail);
						break;
					}
				}
			}
			return list;
		}
		
		Map<String, String> outHospitalDrugTypeMap = ruleCYDYZLSHMapper.selectOutHospitalDrugType();
		
		int westernDrugCount = Integer.parseInt(outHospitalDrugTypeMap.get("XYZLSL"));
		int chineseDrugCount = Integer.parseInt(outHospitalDrugTypeMap.get("ZCYZLSL"));
		
		if(westernDrugList.size()>westernDrugCount)
		{
			//将超出的药品作为违规药品
			for(int i=westernDrugCount;i<westernDrugList.size();i++)
			{	
				Map<String, String> drugTypeMap=westernDrugList.get(i);
				for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
				{
					if(hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("DRUG_CODE"))
						|| hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("NEW_DRUG_CODE")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "出院带药种类异常");
						list.add(violationDetail);
						break;
					}
				}
			}
		}
		
		if(chineseDrugList.size()>chineseDrugCount)
		{
			//将超出的药品作为违规药品
			for(int i=chineseDrugCount;i<chineseDrugList.size();i++)
			{
				Map<String, String> drugTypeMap=chineseDrugList.get(i);
				for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
				{
					if(hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("DRUG_CODE"))
						|| hospitalClaimDetail.getProductCode().equals(drugTypeMap.get("NEW_DRUG_CODE")))
					{
						violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "出院带药种类异常");
						list.add(violationDetail);
						break;
					}
				}
			}
		}
		
		return list;
	}

}
