package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.DateUtils;
import com.shuxin.commons.utils.MySortList;
import com.shuxin.commons.utils.StringUtils;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleBHLRYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;
/**
 * 
 *不合理入院
 */
@Service
public class RuleBHLRYServiceImpl implements IAnalysisRuleService {
	
	@Autowired
	private RuleBHLRYMapper ruleBHLRYMapper;
	
	@Value(value = "${unreasonable.hospital.day}")
	private String hospitalDay;

	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		
		if(!"1".equals(hospitalClaim.getLiveHospStatus()))
		{
			return null;
		}
		
		Date date = new Date();
		//从入院的第二天开始审核该规则
		if(DateUtils.differentDays(hospitalClaim.getInHospDate(), date)<1)
		{
			return null;
		}
		
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		List<HospitalClaimDetail> hospitalClaimDetailListTemp=new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp=new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				hospitalClaimDetailListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
		}
		
		if(hospitalClaimDetails.size()==0)
		{
			return null;
		}
		
		if("1".equals(hospitalClaim.getLiveHospStatus()))
		{
			if(DateUtils.differentDays(hospitalClaim.getInHospDate(),hospitalClaim.getOutHospDate())<Integer.parseInt(hospitalDay))
			{
//				for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
//				{
//					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, "本次住院天数低于3天");
					violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), "本次住院天数低于"+hospitalDay+"天");
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
//				}
				return list;
			}
		}		
		
		
		List<Map<String, String>> unreasonableProjectList=ruleBHLRYMapper.selectUnreasonableProjectInfo(hospitalClaimDetailListTemp);
		
		MySortList<HospitalClaimDetail> mysortList = new MySortList<HospitalClaimDetail>();
		//将明细按服务日期排序
		mysortList.sortByMethod(hospitalClaimDetails, "getServiceDate", false);
		
		boolean examinFlag=false;
		boolean drugFlag=false;
		boolean treatmentFlag=false;
		boolean operationFlag=false;
		BigDecimal examinFee = new BigDecimal(0);
		BigDecimal drugFee = new BigDecimal(0);
		BigDecimal treatmentFee = new BigDecimal(0);
		BigDecimal totalFee = new BigDecimal(0);
		
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			totalFee=totalFee.add(hospitalClaimDetail.getAmount());
			int days=DateUtils.differentDays(hospitalClaim.getInHospDate(),hospitalClaimDetail.getServiceDate());
			for(Map<String, String> unreasonableProjectMap:unreasonableProjectList)
			{				
				if("7".equals(unreasonableProjectMap.get("BLX")))
				{
					operationFlag=true;
				}
				
				if(hospitalClaimDetail.getProductCode().equals(unreasonableProjectMap.get("YPXMBM")))
				{
					
					if(days<2)
					{
						if("1".equals(unreasonableProjectMap.get("BLX")))
						{
							drugFlag=true;
						}
						else if("4".equals(unreasonableProjectMap.get("BLX")))
						{
							examinFlag=true;
						}
						else if("3".equals(unreasonableProjectMap.get("BLX")))
						{
							treatmentFlag=true;
						}
					 }
					else if(days<3)
					{
						if("3".equals(unreasonableProjectMap.get("BLX")))
						{
							treatmentFlag=true;
						}
					}					
					
					if("1".equals(unreasonableProjectMap.get("BLX")))
					{
						drugFee=drugFee.add(hospitalClaimDetail.getAmount());
					}
					else if("3".equals(unreasonableProjectMap.get("BLX")))
					{
						treatmentFee=treatmentFee.add(hospitalClaimDetail.getAmount());
					}
					else if("4".equals(unreasonableProjectMap.get("BLX")))
					{
						examinFee=examinFee.add(hospitalClaimDetail.getAmount());
					}
					
					break;
				}
			}
		}
		String tip="";
		//有手术不需要审核下面这些条件
		if(!operationFlag&&(!drugFlag||!examinFlag||!treatmentFlag))
		{
			
			if(!drugFlag)
			{
				tip="自入院开始连续2天,无药品";
			}
			else if(!examinFlag)
			{
				tip="自入院开始连续2天,没有常规检查";
			}
			else {
				tip="自入院开始连续3天,无治疗";
			}			
		}		
		else if(DateUtils.differentDays(hospitalClaim.getInHospDate(),new Date())>2)
		{
			
			if(totalFee.compareTo(new BigDecimal(2000))<1)
			{
				tip="住院费用低于2000元";
			}
			//有手术不需要审核下面这些条件
			else if(!operationFlag)
			{
				if(drugFee.divide(totalFee,2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(0.85))>0)
				{
					tip="药费占比大于85%";
				}
				else if(treatmentFee.divide(totalFee,2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(0.7))>0)
				{
					tip="治疗费占比大于70%";
				}
				else if(examinFee.divide(totalFee,2,BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(0.7))>0)
				{
					tip="检查费占比大于70%";
				}
			}			
		}
		
		if(!StringUtils.isEmpty(tip))
		{
//			for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
//			{
//				violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, tip);
			    violationDetail=ToolUtils.getViolationDetail2(rule, hospitalClaim, rule.getId(),rule.getMenuName(), tip);
			    if(list==null)
				{
					list= new ArrayList<ViolationDetail>();
				}
				list.add(violationDetail);
//			}
		}
		
		return list;
	}	
	
}
