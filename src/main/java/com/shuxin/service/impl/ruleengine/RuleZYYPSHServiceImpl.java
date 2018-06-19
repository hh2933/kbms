package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleZYYPSHMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 中药饮片审核
 */
@Service
public class RuleZYYPSHServiceImpl implements IAnalysisRuleService {

	@Autowired
	private RuleZYYPSHMapper ruleZYYPSHMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		//单味违规标识
		boolean isSingleIllegal = false;
		//单味或复方违规标识
		boolean isCompoundIllegal=false;
		//药品的数量
		int drugCount=0;
		//单方或复方不支付饮片数量
		int singleAndCompoundDrugCount=0;
		List<String> drugCodeList =new ArrayList<String>();
		List<ViolationDetail> list=  new ArrayList<ViolationDetail>();
		List<ViolationDetail> singlelist=  new ArrayList<ViolationDetail>();
		List<ViolationDetail> singlePercentList=  new ArrayList<ViolationDetail>();
		ViolationDetail violationDetail =null;
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<Map<String,String>> piecesExamineInfos=ruleZYYPSHMapper.selectPiecesExamineInfo();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}
			
			if(!drugCodeList.contains(hospitalClaimDetail.getProductCode()))
			{
				drugCodeList.add(hospitalClaimDetail.getProductCode());
			}
			
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			drugCount++;
			isSingleIllegal=false;
//			List<Map<String, String>> piecesExamineList = ruleZYYPSHMapper.selectPiecesExamineInfo(hospitalClaimDetail.getProductCode());
			List<Map<String, String>> piecesExamineList=new ArrayList<Map<String,String>>();
			for(Map<String,String> piecesExamineInfo:piecesExamineInfos)
			{
				if(hospitalClaimDetail.getProductCode().equals(piecesExamineInfo.get("XMBM")))
				{
					piecesExamineList.add(piecesExamineInfo);
				}
			}
			
			if(piecesExamineList ==null || piecesExamineList.size()==0)
			{
				continue;
			}
			
			Map<String, String> piecesExamineMap = piecesExamineList.get(0);
			if("1".equals(piecesExamineMap.get("CFLX")))
			{	
				
				//单味药品数量如果是1就违规
				if(drugCount==1)
				{
					isSingleIllegal=true;
					violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, piecesExamineMap.get("TSXX"));
					
					singlelist.add(violationDetail);
					
				}
			}
			//如果是单味或复方，只要有就违规
			else
			{
				isCompoundIllegal=true;
				violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, piecesExamineMap.get("TSXX"));
				
				list.add(violationDetail);
			}
		}
		
		if(drugCodeList.size()!=0)
		{
			
			singleAndCompoundDrugCount=ruleZYYPSHMapper.selectCountByDrugCode(drugCodeList);
		}
		
		
		if((float)singleAndCompoundDrugCount/(float)drugCount>0.6)
		{
			violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), "贵重饮片和单方不支付饮片的数量占比超过60%");
			singlePercentList.add(violationDetail);
		}
		
		
		if(isSingleIllegal||isCompoundIllegal)
		{
			if(isSingleIllegal)
			{
				singlelist.addAll(singlePercentList);
				return singlelist;
			}
			list.addAll(singlePercentList);
			return list;
		}
		
		return singlePercentList;
	}

}
