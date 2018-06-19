package com.shuxin.service.impl.ruleengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.DateUtils;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.HospitalClaimMapper;
import com.shuxin.mapper.ruleengine.RuleFJZYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *分解住院
 */
@Service
public class RuleFJZYServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleFJZYMapper ruleFJZYMapper;
	
	@Autowired
	private HospitalClaimMapper hospitalClaimMapper;
	
	private Logger logger = LogManager.getLogger(getClass());

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("patCode", hospitalClaim.getPatCode());
		paramMap.put("diaserialCode", hospitalClaim.getDiaSerialCode());
		
		String lastOutHospitalDate = hospitalClaimMapper.selectLastOutHospitalDate(paramMap);
		
		if(StringUtils.isEmpty(lastOutHospitalDate))
		{
			return null;
		}
		
		Map<String,String> manyHospitalizationMap=ruleFJZYMapper.selectManyHospitalizationInfo();
		
		SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
		int days = 0;
		try {
			 days = DateUtils.differentDays(sFormat.parse(lastOutHospitalDate), hospitalClaim.getInHospDate());
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		List<ViolationDetail> list= null;
//		ViolationDetail violationDetail =null;
		if(days<=Integer.parseInt(manyHospitalizationMap.get("SJJGXDT")))
		{			
//			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
//			{
//				violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, manyHospitalizationMap.get("TSXX1"));
			    ViolationDetail violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), manyHospitalizationMap.get("TSXX1"));
//				if(list==null)
//				{
				list= new ArrayList<ViolationDetail>();
//				}
				list.add(violationDetail);
//			}
		}
		
		return list;
	}

}
