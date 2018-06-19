package com.shuxin.service.impl.ruleengine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *就诊信息数据异常
 */
@Service
public class RuleJZXXSJYCServiceImpl implements IAnalysisRuleService {

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		boolean flag=false;
		String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
		if(StringUtils.isEmpty(hospitalClaim.getDiaInHospCode())&&
				StringUtils.isEmpty(hospitalClaim.getDiaOutHospCode())
			&&StringUtils.isEmpty(hospitalClaim.getDiaViceCode()))
		{
			flag=true;
		}
		else if(medTreatmentMode.equals("21") || 
				medTreatmentMode.equals("22") || 
				medTreatmentMode.equals("25") || 
				medTreatmentMode.equals("52") || 
				medTreatmentMode.equals("72"))
		{
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
			if(!fmt.format(hospitalClaim.getInHospDate()).equals(fmt.format(hospitalClaim.getOutHospDate())))
			{
				flag=true;
			}
		}
		
		if(flag)
		{
			List<ViolationDetail> list= new ArrayList<ViolationDetail>();
//			ViolationDetail violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, hospitalClaim.getDiaSerialCode(),hospitalClaim.getDiaSerialCode(), "就诊信息数据异常");
			ViolationDetail violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), "就诊信息数据异常");
			
			list.add(violationDetail);
			return list;
		}
		
		return null;
	}

}
