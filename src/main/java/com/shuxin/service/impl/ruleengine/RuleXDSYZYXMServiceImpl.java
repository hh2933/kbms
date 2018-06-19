package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.MIDiagnosisMapper;
import com.shuxin.mapper.TreatsubjectTransMapper;
import com.shuxin.mapper.ruleengine.RuleXDSYZYXMMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 限定适应症用项目
 *
 */
@Service
public class RuleXDSYZYXMServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleXDSYZYXMMapper ruleXDSYZYXMMapper;
	
	@Autowired
	private MIDiagnosisMapper mIDiagnosisMapper;
	
	@Autowired
	private TreatsubjectTransMapper treatsubjectTransMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<HospitalClaimDetail> projectCodeList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> projectCodeListTemp = new ArrayList<HospitalClaimDetail>();
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		List<String> aiagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		List<String> productCodeTemp = new ArrayList<String>();
		List<String> codeList = new ArrayList<String>();
		codeList.addAll(aiagnosisCodeList);
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(!codeList.contains(hospitalClaimDetail.getProductCode())){
				codeList.add(hospitalClaimDetail.getProductCode());
			}
			//只审核项目
			if("1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectCodeListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			projectCodeList.add(hospitalClaimDetail);		
			
		}
		
		if(projectCodeList.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> indicationProjectList = ruleXDSYZYXMMapper.selectIndicationProjectInfo(projectCodeListTemp);
		
		if(indicationProjectList.size()==0)
		{
			return null;
		}
		
		List<String> diagnosticList = new ArrayList<String>();
		if(aiagnosisCodeList.size()>0)
		{
			mIDiagnosisMapper.selectAdaptionPackageCode(aiagnosisCodeList);
		}
		List<String> treatsubjectTransList = treatsubjectTransMapper.selectDefineSubjectCode(productCodeTemp);
		Iterator<HospitalClaimDetail> it = null;
		for(Map<String, String> indicationProjectMap:indicationProjectList)
		{
			if(treatsubjectTransList.contains(indicationProjectMap.get("ZLXMBM")))
			{
				continue;
			}
			boolean violationFlag=true;//违规标识
			String[] indicationDiagnosisGroup = indicationProjectMap.get("XDSYZBM").split(",");
			for(String indicationDiagnosises:indicationDiagnosisGroup)
			{
				boolean containFlag = true;//是否包含治疗项目编码
				String[] indicationDiagnosisCodes=indicationDiagnosises.split("\\|");
				//必须包含所有项目编码
				for(String indicationDiagnosisCode:indicationDiagnosisCodes)
				{
					//只要有一个不包含就跳出循环
					if(!diagnosticList.contains(indicationDiagnosisCode))
					{
						containFlag=false;
						break;
					}
				}
				//如果有一组编码都包含，说明没有违规
				if(containFlag)
				{
					violationFlag=false;
					break;
				}
			}
			if(violationFlag)
			{
				it = projectCodeList.iterator();
				while(it.hasNext())
				{
					HospitalClaimDetail hospitalClaimDetail = it.next();
					if(hospitalClaimDetail.getProductCode().equals
							(indicationProjectMap.get("XMBM")))
					{
						violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, indicationProjectMap.get("TSXX"));
						if(list==null)
						{
							list= new ArrayList<ViolationDetail>();
						}
						list.add(violationDetail);
						it.remove();
					}
				}
			}
		}
		
		return list;
	}
	

}
