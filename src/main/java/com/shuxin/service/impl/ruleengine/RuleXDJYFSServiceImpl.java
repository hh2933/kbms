package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.DateUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleXDJYFSMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限定就医方式
 *
 */
@Service
public class RuleXDJYFSServiceImpl implements IAnalysisRuleService {
	
	//限定就医方式格规则Mapper
	@Autowired
	RuleXDJYFSMapper xDJYFSMapper;
		
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> result = new ArrayList<ViolationDetail>();
		List<HospitalClaimDetail> projectListTemp=new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp=new ArrayList<String>();
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails){
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				projectListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
		}
		
//		List<HospitalClaimDetail> tempDetails = new ArrayList<HospitalClaimDetail>();
		
		//"医保限定规则”,"出院带药","费用超限"三个类型的规则，医保为0的不审核
//		String ruleType = rule.getRuleType();
//		if(ruleType.equals("1") || ruleType.equals("2") || ruleType.equals("3")){
//			for(HospitalClaimDetail detail : hospitalClaimDetails){
//				BigDecimal medInsCost = detail.getMedInsCost();
//				if(medInsCost.compareTo(BigDecimal.ZERO) == 1){
//					tempDetails.add(detail);
//				}
//			}
//		}
//		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		//就医方式
		String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
		List<Map<String, String>> indicationCode = null;
		//就医方式是门诊
		if(medTreatmentMode.equals("11") || 
			medTreatmentMode.equals("13") || 
			medTreatmentMode.equals("15") || 
			medTreatmentMode.equals("51") || 
			medTreatmentMode.equals("71")){
			//查询项目是否在规则表中
			indicationCode = xDJYFSMapper.selectIndicationCode("1", projectListTemp);
			if(indicationCode.size() > 0){
				//是否异地就医
				String isRemote = hospitalClaim.getIsRemote();
				for(HospitalClaimDetail detail : hospitalClaimDetails){
					String productCode = detail.getProductCode();
					for(Map<String, String> map : indicationCode){
						String xmbm = map.get("XMBM");
						String isYD = map.get("SFSHYDSJ");
						if(productCode.equals(xmbm)){
							//是否审核异地数据
							if("1".equals(isRemote) && "N".equals(isYD)){
								continue;
							}
							ViolationDetail violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, detail, map.get("TSXX"));
							result.add(violationDetail);
						}
					}
				}
			}
		//就医方式是住院
		}else if(medTreatmentMode.equals("21") || 
				medTreatmentMode.equals("22") || 
				medTreatmentMode.equals("25") || 
				medTreatmentMode.equals("52") || 
				medTreatmentMode.equals("72")){ 
			//查询项目是否在规则表中
			indicationCode = xDJYFSMapper.selectIndicationCode("0",projectListTemp);
			if(indicationCode.size() > 0){
				//是否异地就医
				String isRemote = hospitalClaim.getIsRemote();
				for(HospitalClaimDetail detail : hospitalClaimDetails){
					String productCode = detail.getProductCode();
					for(Map<String, String> map : indicationCode){
						String xmbm = map.get("XMBM");
						String dayNum = map.get("RYDSTQBSH");
						String isYD = map.get("SFSHYDSJ");
						if(productCode.equals(xmbm)){
							//入院多少天前不审核
							if(!"N".equals(dayNum)){
								int dayNumInt = Integer.parseInt(dayNum);
								Date curDate = new Date(System.currentTimeMillis());
								Date inHospDate = hospitalClaim.getInHospDate();
								int day = DateUtils.differentDays(inHospDate,curDate);
								if(day <= dayNumInt){
									continue;
								}
							}
							//是否审核异地数据
							if("1".equals(isRemote) && "N".equals(isYD)){
								continue;
							}
							ViolationDetail violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, detail, map.get("TSXX"));
							result.add(violationDetail);
						}
					}
				}
			}
		}else{
			return null;
		}
		
		if(result.size() > 0){
			return result;
		}else{
			return null;
		}
	}

}
