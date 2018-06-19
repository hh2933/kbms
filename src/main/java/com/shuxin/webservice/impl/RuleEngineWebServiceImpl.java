package com.shuxin.webservice.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.shuxin.commons.utils.PropertiesLoader;
import com.shuxin.commons.utils.SpringContextHelper;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ThreadPoolUtil;
import com.shuxin.commons.utils.WebServiceParamValidation;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.model.ruleengine.WSReturnResult;
import com.shuxin.service.ruleengine.IExamineService;
import com.shuxin.service.ruleengine.IHospitalClaimService;
import com.shuxin.service.ruleengine.IViolationDetailService;
import com.shuxin.webservice.RuleEngineWebService;
@WebService
public class RuleEngineWebServiceImpl implements RuleEngineWebService {
	
	private String certID;
	
	@Override
	public WSReturnResult examine(String certId, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		WSReturnResult returnResult = new WSReturnResult();
		
		if(StringUtils.isEmpty(certID))
		{
			PropertiesLoader propertiesLoader = new PropertiesLoader("certID.properties");
			certID = propertiesLoader.getProperty("certID");
		}
		
		if(!certId.equals(certID)){
			returnResult.setResultCode("0002");
			returnResult.setResultMsg("认证ID号验证失败！");
			returnResult.setResultStatus("F");
			return returnResult;
		}
		
		
		if(!"3".equals(hospitalClaim.getOperationType()))
		{
			WebServiceParamValidation.baseCheckInputVO(hospitalClaim, returnResult);
			
			if("F".equals(returnResult.getResultStatus()))
			{
				return returnResult;
			}
			
//			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
//			{
//				WebServiceParamValidation.baseCheckInputVO(hospitalClaimDetail, returnResult);
//				if("F".equals(returnResult.getResultStatus()))
//				{
//					return returnResult;
//				}
//			}
		}
		
		
		ThreadPoolUtil.handleHospitalClaimOpt(hospitalClaim, hospitalClaimDetails);
		
		boolean opreationResult = false;
		
		IHospitalClaimService hospitalClaimService =(IHospitalClaimService)SpringContextHelper.getBean("hospitalClaimServiceImpl");
		
		List<ViolationDetail> list = new ArrayList<ViolationDetail>();
		
		//如果删除主单信息，不需要审核直接返回结果
		if("3".equals(hospitalClaim.getOperationType()))
		{
			if(StringUtils.isEmpty(hospitalClaim.getId()))
			{
				returnResult.setResultCode("0002");
				returnResult.setResultMsg("id为空！");
				returnResult.setResultStatus("F");
				return returnResult;
			}
			
			IViolationDetailService violationDetailService =(IViolationDetailService)SpringContextHelper.getBean("violationDetailServiceImpl");
			violationDetailService.deleteViolationDetail(hospitalClaim.getId());
			opreationResult=hospitalClaimService.delHospitalClaimInfo(hospitalClaim.getDiaSerialCode());
			if(opreationResult)
			{
				returnResult.setResultCode("0000");
				returnResult.setResultMsg("正常");
				returnResult.setResultStatus("S");
			}
			else
			{
				returnResult.setResultCode("0002");
				returnResult.setResultMsg("业务数据逻辑处理异常");
				returnResult.setResultStatus("F");
			}
			return returnResult;
		}
		else
		{
			opreationResult=hospitalClaimService.handleHospitalClaimInfo(hospitalClaim, hospitalClaimDetails);
			if(!opreationResult)
			{
				returnResult.setResultCode("0002");
				returnResult.setResultMsg("业务数据逻辑处理异常");
				returnResult.setResultStatus("F");
				return returnResult;
			}
		}
		
		//就医方式
		String medTreatmentMode = hospitalClaim.getMedTreatmentMode();
		
		IExamineService examineService=(IExamineService)SpringContextHelper.getBean("examineServiceImpl");
		//判断就医方式是门诊
		if(medTreatmentMode.equals("11") || 
			medTreatmentMode.equals("13") || 
			medTreatmentMode.equals("15") || 
			medTreatmentMode.equals("51") || 
			medTreatmentMode.equals("71"))
		{
			list = examineService.examineOutpatient(hospitalClaim,hospitalClaimDetails);
		}
		else if(medTreatmentMode.equals("21") || 
				medTreatmentMode.equals("22") || 
				medTreatmentMode.equals("25") || 
				medTreatmentMode.equals("52") || 
				medTreatmentMode.equals("72"))
		{
			list = examineService.examineHospitalization(hospitalClaim,hospitalClaimDetails);
		}
		else
		{
			returnResult.setResultCode("0002");
			returnResult.setResultMsg("业务数据逻辑处理异常");
			returnResult.setResultStatus("F");
		}
		returnResult.setList(list);
		ThreadPoolUtil.handleViolationDetail(hospitalClaim, hospitalClaimDetails, list);
		return returnResult;
	}

}
