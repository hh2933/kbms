package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleXECXMBXDJMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限二次项目报销单价
 * @author shuxin
 *
 */
@Service
public class RuleXECXMBXDJServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	RuleXECXMBXDJMapper ruleXECXMBXDJMapper;
	@Autowired
	HospitalClaimDetailMapper HospitalClaimDetailMapper;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> result = null;
		ViolationDetail violationDetail =null;
		List<HospitalClaimDetail> tempDetails = new ArrayList<HospitalClaimDetail>();
		//"医保限定规则”,"出院带药","费用超限"三个类型的规则，医保为0的不审核
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		for(HospitalClaimDetail detail : hospitalClaimDetails){
			//只有项目才需要审核
			if("1".equals(detail.getThrCatType()))
			{
				continue;
			}
			
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					detail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			tempDetails.add(detail);
		}
		
		
		if(tempDetails.size()==0)
		{
			return null;
		}
		
		List<Map<String, String>> indicationCodeList = ruleXECXMBXDJMapper.selectIndicationCode(tempDetails);
		
		if(indicationCodeList.size()==0)
		{
			return null;
		}
		
		List<BigDecimal> priceList= new ArrayList<BigDecimal>();
		for(Map<String, String> indicationCodeMap:indicationCodeList)
		{
			priceList.clear();
			BigDecimal price = new BigDecimal(indicationCodeMap.get("D2XXMYBJEFZY"));
			for(HospitalClaimDetail detail :tempDetails)
			{
				boolean violationFlag = false;
				if(detail.getProductCode().equals(indicationCodeMap.get("XMBM")))
				{
					priceList.add(detail.getUnitPrice());
					if(priceList.size()>1)
					{
						int count=0;
						for(BigDecimal projectPrice:priceList)
						{
							if(projectPrice.compareTo(price)==1)
							{
								count++;
							}
							if(count>1)
							{
								violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, detail, indicationCodeMap.get("TSXX"));
								if(result==null)
								{
									result= new ArrayList<ViolationDetail>();
								}
								result.add(violationDetail);
								violationFlag=true;
								break;
							}
						}
						
						if(violationFlag)
						{
							break;
						}
						
					}
				}
				
			}
		}	

		
		return result;
		
	}

}
