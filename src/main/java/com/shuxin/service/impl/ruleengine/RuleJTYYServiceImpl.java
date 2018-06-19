package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.result.Constants;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.MIDiagnosisMapper;
import com.shuxin.mapper.TreatsubjectTransMapper;
import com.shuxin.mapper.ruleengine.HospitalClaimDetailMapper;
import com.shuxin.mapper.ruleengine.RuleJTYYMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

import net.sf.ehcache.util.ProductInfo;
/**
 * 
 *阶梯用药
 */
@Service
public class RuleJTYYServiceImpl implements IAnalysisRuleService{

	@Autowired
	private RuleJTYYMapper ruleJTYYMapper;
	
	@Autowired
	private MIDiagnosisMapper miDiagnosisMapper;
	
	@Autowired
	private TreatsubjectTransMapper treatsubjectTransMapper;
	
	@Autowired
	private HospitalClaimDetailMapper hospitalClaimDetailMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<HospitalClaimDetail> drugCodeList = new ArrayList<HospitalClaimDetail>();
		List<HospitalClaimDetail> drugCodeListTemp = new ArrayList<HospitalClaimDetail>();
		List<String> productCodeTemp =new ArrayList<String>();
		
//		int ruleType = Integer.parseInt(rule.getRuleType());
		List<ViolationDetail> list= null;
		ViolationDetail violationDetail =null;
		List<String> codeList = new ArrayList<String>();
		for(HospitalClaimDetail hospitalClaimDetail:hospitalClaimDetails)
		{
			codeList.add(hospitalClaimDetail.getProductCode());
			//只有药品才需要审核
			if(!"1".equals(hospitalClaimDetail.getThrCatType()))
			{
				continue;
			}			
			
			if(!productCodeTemp.contains(hospitalClaimDetail.getProductCode())){
				drugCodeListTemp.add(hospitalClaimDetail);
				productCodeTemp.add(hospitalClaimDetail.getProductCode());
			}
			//如果患者的医保金额为0，就不用审核
//			if(ruleType <4 &&
//					hospitalClaimDetail.getMedInsCost().compareTo(BigDecimal.ZERO)<1)
//			{
//				continue;
//			}
			
			drugCodeList.add(hospitalClaimDetail);			
			
		}
		
		if(drugCodeList.size() == 0)
		{
			return null;
		}	
		
		List<Map<String,Object>> secondLineDrugList = ruleJTYYMapper.selectSecondLineDrugInfo(drugCodeListTemp);
		
		if(secondLineDrugList.size()==0)
		{
			return null;
		}
		
		List<String> diagnosisCodeList = ToolUtils.getAllAiagnosisCode(hospitalClaim);
		List<String> adaptionPackageCodeList = new ArrayList<String>();
		
		if(diagnosisCodeList.size()>0)
		{
			adaptionPackageCodeList= miDiagnosisMapper.selectAdaptionPackageCode(diagnosisCodeList);
		}
		
		
		List<String> treatsubjectTransList = treatsubjectTransMapper.selectDefineSubjectCode(productCodeTemp);
		Map<String, Object> paramMap = null;
		for(HospitalClaimDetail hospitalClaimDetail:drugCodeList)
		{
			//当审核通过或不需要审核，就直接跳出这个循环
			secondLoop:
			for(Map<String,Object> secondLineDrugMap:secondLineDrugList)
			{
				if(!hospitalClaimDetail.getProductCode().
						equals((String)secondLineDrugMap.get("DRUG_CODE")))
				{
					continue;
				}
				
				if(!Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("SYZXD"))
						&& adaptionPackageCodeList.size()>0)
				{
					String[] limitIndications = ((String)secondLineDrugMap.get("SYZXD")).split(",");
					for(String limitIndication:limitIndications)
					{
						if(adaptionPackageCodeList.contains(limitIndication))
						{
							break secondLoop;
						}
					}
				}
				
				if(!Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("XDDZLXM"))
						&& treatsubjectTransList.size()>0)
				{
					String[] treatsubjects = ((String)secondLineDrugMap.get("XDDZLXM")).split(",");
					for(String treatsubject:treatsubjects)
					{
						if(treatsubjectTransList.contains(treatsubject))
						{
							break secondLoop;
						}
					}
				}
				
				if(!Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("CBLXBM")))
				{
					if(hospitalClaim.getPatInsuredType().equals((String)secondLineDrugMap.get("CBLXBM")))
					{
						break secondLoop;
					}
				}
				
				paramMap = new HashMap<String, Object>();
				String  isLastMonth = "false";
				if(!Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("CSPJLSFCSGYZHYTKSC")))
				{
					isLastMonth = "true";
					SimpleDateFormat formatter =  new SimpleDateFormat ("yyyy-MM");
					Date now = new Date();
					Calendar calendar = Calendar.getInstance();//日历对象
					calendar.setTime(now);//设置当前日期
					calendar.add(Calendar.MONTH, -1);//月份减一
					paramMap.put("serviceDate",formatter.format(calendar.getTime()));
				}
				else if(Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("SFCSPJL")))
				{
					paramMap.put("days", ((BigDecimal)secondLineDrugMap.get("CXLSTS")).intValue());
				}
				else
				{
					paramMap.put("days", 30);
				}
				
				if(Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("CYXYYLSJLSFBHDT")))
				{
					paramMap.put("list", hospitalClaimDetails);
					paramMap.put("isCurrent", "true");
				}
				
				paramMap.put("isLastMonth", isLastMonth);
				paramMap.put("groupCode", (String)secondLineDrugMap.get("YXYPBMZ"));
				paramMap.put("patCode", hospitalClaim.getPatCode());
				
				int firstDrugCount = hospitalClaimDetailMapper.selectFirstDrugCount(paramMap);
				if(firstDrugCount == 0)
				{
					String prompt = (String)secondLineDrugMap.get("WFXDYPJLZTS");
					if(!Constants.N_FLAG.equalsIgnoreCase
						((String)secondLineDrugMap.get("YFXDYPJLZTS")))
					{
						prompt = (String)secondLineDrugMap.get("YFXDYPJLZTS");
					}
					violationDetail=ToolUtils.getViolationDetail(rule, hospitalClaim, hospitalClaimDetail, prompt);
					if(list==null)
					{
						list= new ArrayList<ViolationDetail>();
					}
					list.add(violationDetail);
				}
			}
		}
		
		return list;
	}

}
