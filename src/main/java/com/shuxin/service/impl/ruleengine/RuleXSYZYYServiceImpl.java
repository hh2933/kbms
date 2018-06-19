package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.MIDiagnosisMapper;
import com.shuxin.mapper.TreatsubjectTransMapper;
import com.shuxin.mapper.ruleengine.RuleXSYZYYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限适应症用药
 *
 */
@Service
public class RuleXSYZYYServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleXSYZYYMapper ruleXSYZYYMapper;
	
	@Autowired
	private MIDiagnosisMapper miDiagnosisMapper;
	
	@Autowired
	private TreatsubjectTransMapper treatsubjectTransMapper;
	
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule,HospitalClaim hospitalClaim, List<HospitalClaimDetail> hospitalClaimDetailsList) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
				
		List<Map<String, String>> limitIndicationList = null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<String> aiagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		List<String> codeList = new ArrayList<String>();
		codeList.addAll(aiagnosisCodeList);
		
		List<HospitalClaimDetail> drugList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> drugListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetailsList)
		{
			if(!codeList.contains(hospitalClaimDetail.getProductCode())){
				
				codeList.add(hospitalClaimDetail.getProductCode());
			}
			
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				drugListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			drugList.add(hospitalClaimDetail);
		}
		
		if(drugList.size()==0)
		{
			return null;
		}
		
		
		//判断是违规还是可疑
		if("1".equals(rule.getResultType()))
		{
			limitIndicationList = ruleXSYZYYMapper.selectIndicationCode_illegal(drugListTemp);
		}
		else
		{
			limitIndicationList = ruleXSYZYYMapper.selectIndicationCode_suspicion(drugListTemp);
		}
		
		if(limitIndicationList.size()==0)
		{
			return null;
		}
		
		
		
		List<String> adaptionPackageCodeList= new ArrayList<String>();
		if(aiagnosisCodeList.size()>0)
		{
			miDiagnosisMapper.selectAdaptionPackageCode(aiagnosisCodeList);
		}
		List<String> treatsubjectTransList = treatsubjectTransMapper.selectDefineSubjectCode(productCodeTemp);
		
		
		for(HospitalClaimDetail hospitalClaimDetail:drugList)
		{
			
			//当审核通过或不需要审核，就直接跳出这个循环
			secondLoop:
			for(Map<String, String> map:limitIndicationList)
			{
				if(!hospitalClaimDetail.getProductCode().equals(map.get("XMBM")))
				{
					continue;
				}
				if(!Constants.N_FLAG.equalsIgnoreCase(map.get("CBLXBM")))
				{
					String[] insuredTypeCodes = map.get("CBLXBM").split(",");
					for(String insuredTypeCode:insuredTypeCodes)
					{
						if(insuredTypeCode.equals(hospitalClaim.getPatInsuredType()))
						{
							break secondLoop;
						}
					}
				}
				if(!Constants.N_FLAG.equalsIgnoreCase(map.get("ZLXM")))
				{
					String[] treatmentCodes = map.get("ZLXM").split(",");
					for(String treatmentCode:treatmentCodes)
					{
						if(treatsubjectTransList.contains(treatmentCode))
						{
							break secondLoop;
						}
					}
				}
				
				if(!Constants.N_FLAG.equalsIgnoreCase(map.get("XJYFS")))
				{
					if("0".equals(map.get("XJYFS")))
					{
						if(hospitalClaim.getMedTreatmentMode().equals("11") || 
								hospitalClaim.getMedTreatmentMode().equals("13") || 
								hospitalClaim.getMedTreatmentMode().equals("15") || 
								hospitalClaim.getMedTreatmentMode().equals("51") || 
								hospitalClaim.getMedTreatmentMode().equals("71"))
						{
							break secondLoop;
						}
					}
					else
					{
						if(hospitalClaim.getMedTreatmentMode().equals("21") || 
							hospitalClaim.getMedTreatmentMode().equals("22") || 
							hospitalClaim.getMedTreatmentMode().equals("25") || 
							hospitalClaim.getMedTreatmentMode().equals("52") || 
							hospitalClaim.getMedTreatmentMode().equals("72"))
						{
							break secondLoop;
						}
					}
				}
				
				String[] limitDiagnosisCodes= map.get("XDSYZBM").split(",");
				
				for(String limitDiagnosisCode:limitDiagnosisCodes)
				{
					String[] codes=limitDiagnosisCode.split("\\|");
					boolean isViolation = false;
					for(String code:codes)
					{
						//判断诊疗和项目是否匹配
						if(code.startsWith("A"))
						{
							if(!adaptionPackageCodeList.contains(code))
							{
								isViolation=true;
								break;
							}
						}
						else
						{
							if(!treatsubjectTransList.contains(code))
							{
								isViolation=true;
								break;
							}
						}
					  }
					//只要有一套编码匹配，就算通过审核
					if(!isViolation)
					{
						break secondLoop;
					}
				}
				violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, map.get("TSXX"));
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
