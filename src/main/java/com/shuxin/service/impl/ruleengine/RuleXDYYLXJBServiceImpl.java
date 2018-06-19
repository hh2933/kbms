package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleXDYYLXJBMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限定医院类型级别
 * @author shuxin
 *
 */
@Service
public class RuleXDYYLXJBServiceImpl implements IAnalysisRuleService {

	@Autowired
	RuleXDYYLXJBMapper ruleXDYYLXJBMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> result = new ArrayList<ViolationDetail>();
		List<HospitalClaimDetail> tempDetails = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> hospitalClaimDetailListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		//"医保限定规则”,"出院带药","费用超限"三个类型的规则，医保为0的不审核
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		for(HospitalClaimDetail detail : hospitalClaimDetails){
			//只有药品才需要审核
			if(!"1".equals(detail.getThrCatType()))
			{
				continue;
			}
			
			if(!productCodeTemp.contains(detail.getProductCode())){
				hospitalClaimDetailListTemp.add(detail);
				productCodeTemp.add(detail.getProductCode());
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
		
		List<Map<String, String>> indicationCode = ruleXDYYLXJBMapper.selectIndicationCode(hospitalClaimDetailListTemp);
		if(indicationCode.size() > 0){
			for(HospitalClaimDetail detail : tempDetails){
				String productCode = detail.getProductCode();
				String pointOrgGrade = hospitalClaim.getPointOrgGrade();
				String pointOrgType = hospitalClaim.getPointOrgType();
				//就医方式
				String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
				for(Map<String, String> map : indicationCode){
					String xmbm = map.get("XMBM");
					String pdyj = map.get("PDYJ");
					String yylx = map.get("YYLX");
					String yyjb = map.get("YYJB");
					String ismz = map.get("SFSHMZ");
					String iszy = map.get("SFSHZY");
					if(productCode.equals(xmbm)){
						//门诊
						if(medTreatmentMode.equals("11") || 
								medTreatmentMode.equals("13") || 
								medTreatmentMode.equals("15") || 
								medTreatmentMode.equals("51") || 
								medTreatmentMode.equals("71")){
							if("N".equals(ismz)){
								continue;
							}
						}
						//住院
						else if(medTreatmentMode.equals("21") || 
								medTreatmentMode.equals("22") || 
								medTreatmentMode.equals("25") || 
								medTreatmentMode.equals("52") || 
								medTreatmentMode.equals("72")){
							if("N".equals(iszy)){
								continue;
							}
						}
						//判断依据是N
						if(pdyj.equals("N")){
							if(!yyjb.equals(pointOrgGrade)){
								ViolationDetail violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, detail, map.get("TSXX"));
								result.add(violationDetail);
							}
						}
						//判断依据是INOR
						
						if(pdyj.equals("INOR")){
							boolean b = false;
							String[] ars = yylx.split("\\|");
							for(int i=0; i<ars.length; i++){
								String[] ar = ars[i].split(",");
								String a1 = ar[0];
								String a2 = ar[1];
								if(!a2.equals("N")){
									if(a1.equals(pointOrgType) && a2.equals(pointOrgGrade)){
										b = true;
									}
								}else{
									if(a1.equals(pointOrgType)){
										b = true;
									}
								}
								if(b){
									continue;
								}
							}
							if(!b){
								ViolationDetail violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, detail, map.get("TSXX"));
								result.add(violationDetail);
							}
						}
					}
				}
			}
		}
		
		if(result.size() > 0){
			return result;
		}else{
			return null;
		}
	}

}
