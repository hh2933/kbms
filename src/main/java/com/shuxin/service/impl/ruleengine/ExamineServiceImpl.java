package com.shuxin.service.impl.ruleengine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.enums.RuleInterfaceImplEnums;
import com.shuxin.commons.utils.SpringContextHelper;
import com.shuxin.mapper.RuleTableInfoMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
import com.shuxin.service.ruleengine.IExamineService;

/**
 * 审核服务
 * @author shuxin
 *
 */
@Service
public class ExamineServiceImpl implements IExamineService {
	
	@Autowired
    private  RuleTableInfoMapper ruleTableInfoMapper;
	
	protected Logger logger = LogManager.getLogger(getClass());
	
	/**
	 * 审核门诊
	 */
	@Override
	public List<ViolationDetail> examineOutpatient(HospitalClaim hospitalClaim, List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> violationDetails = new ArrayList<ViolationDetail>();
		//获取所有规则
		List<RuleTableInfo> rules = ruleTableInfoMapper.selectRuleTableInfoAll("1");
		System.out.println("规则个数："+rules.size());
		logger.debug("规则个数："+rules.size());
		//循环每个规则进行验证
		for(RuleTableInfo rule : rules){
			String tableName = rule.getTableName();
			System.out.println("规则表名："+tableName);
			logger.debug("规则表名："+tableName);
			List<ViolationDetail> violationDetail = analysis(rule,hospitalClaim,hospitalClaimDetails);
			if(null != violationDetail){
				violationDetails.addAll(violationDetail);
			}
		}
		return violationDetails;
	}

	/**
	 * 审核住院
	 */
	@Override
	public List<ViolationDetail> examineHospitalization(HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> violationDetails = new ArrayList<ViolationDetail>();
		//获取所有规则
		List<RuleTableInfo> rules = ruleTableInfoMapper.selectRuleTableInfoAll("2");
		System.out.println("规则个数："+rules.size());
		logger.debug("规则个数："+rules.size());
		//循环每个规则进行验证
		for(RuleTableInfo rule : rules){
			String tableName = rule.getTableName();
			System.out.println("规则表名："+tableName);
			logger.debug("规则表名："+tableName);
			List<ViolationDetail> violationDetail = analysis(rule,hospitalClaim,hospitalClaimDetails);
			if(null != violationDetail){
				violationDetails.addAll(violationDetail);
			}
		}
		return violationDetails;
	}

	/**
	 * 按不同的规则解析
	 */
	private List<ViolationDetail> analysis(RuleTableInfo rule,HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails){
		List<ViolationDetail> violationDetail = null;
		IAnalysisRuleService analysisRuleService = null;
		try 
		{
			analysisRuleService = (IAnalysisRuleService)SpringContextHelper.getBean(RuleInterfaceImplEnums.getEnumValue(rule.getTableName()));
			violationDetail = analysisRuleService.executeRule(rule,hospitalClaim, hospitalClaimDetails);
		} 
		catch (Exception e) 
		{			
			logger.error(e.getMessage());
		}
		return violationDetail;
	}
}
