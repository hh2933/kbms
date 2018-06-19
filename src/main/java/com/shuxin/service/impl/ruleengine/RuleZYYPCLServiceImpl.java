package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.TumourDiagnosisMapper;
import com.shuxin.mapper.ruleengine.RuleZYYPCLMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 中药饮片超量
 */
@Service
public class RuleZYYPCLServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleZYYPCLMapper ruleZYYPCLMapper;
	
	@Autowired
	private TumourDiagnosisMapper tumourDiagnosisMapper;
	
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		List<String> diagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		
		if(diagnosisCodeList.size()==0)
		{
			return null;
		}
		
		//查询恶性肿瘤患者
		int diagnosisCount = tumourDiagnosisMapper.selectTumourDiagnosisCount(diagnosisCodeList);
		
		List<Map<String, String>> piecesExcessInfos =ruleZYYPCLMapper.selectPiecesExcessInfo();
		
		
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
			
//			List<Map<String, String>> piecesExcessList = ruleZYYPCLMapper.selectPiecesExcessInfo(hospitalClaimDetail.getProductCode());
			List<Map<String,String>> piecesExcessList=new ArrayList<Map<String,String>>();
			for(Map<String,String> piecesExcessInfo:piecesExcessInfos)
			{
				if(hospitalClaimDetail.getProductCode().equals(piecesExcessInfo.get("XMBM")))
				{
					piecesExcessList.add(piecesExcessInfo);
				}
			}
			
			if(piecesExcessList ==null || piecesExcessList.size()==0)
			{
				continue;
			}
			
			Map<String, String> piecesExcessMap = piecesExcessList.get(0);
			//如果离休干部标识不为N，且该患者类别为离休就不用审核
			String patType = piecesExcessMap.get("SFJCLXGB");
			if(!Constants.N_FLAG.equalsIgnoreCase(patType))
			{
				if("3".equals(hospitalClaim.getPatType()))
				{
					continue;
				}
			}
			
			
			float dosage=0;
			float cardNum=0;
			//如果大于0将患者定义为恶性肿瘤患者
			if(diagnosisCount>0)
			{
				 dosage=(new BigDecimal(piecesExcessMap.get("TSBRXLK"))).floatValue();
				 cardNum=(new BigDecimal(piecesExcessMap.get("TSBRTS"))).floatValue();				
			}
			else
			{
				 dosage=(new BigDecimal(piecesExcessMap.get("PTBRXLK"))).floatValue();
				 cardNum=(new BigDecimal(piecesExcessMap.get("PTBRTS"))).floatValue();				
			}
			//如果没有超量就跳过
			if(hospitalClaimDetail.getDosage()<=dosage&&
					hospitalClaimDetail.getCardNum()<=cardNum	)
			{
				continue;
			}
			violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, (String)piecesExcessMap.get("TSXX"));
			if(list==null)
			{
				list= new ArrayList<ViolationDetail>();
			}
			list.add(violationDetail);
		}
		return list;
	}

}
