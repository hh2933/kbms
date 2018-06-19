package com.shuxin.service.impl.ruleengine;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shuxin.commons.utils.MySortList;
import com.shuxin.commons.utils.ToolUtils;
import com.shuxin.mapper.ruleengine.RuleXDSSJGMapper;
import com.shuxin.model.RuleTableInfo;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
import com.shuxin.model.ruleengine.ViolationDetail;
import com.shuxin.service.ruleengine.IAnalysisRuleService;

/**
 * 限定手术价格
 * @author shuxin
 *
 */
@Service
public class RuleXDSSJGServiceImpl implements IAnalysisRuleService {

	//限定手术价格规则Mapper
	@Autowired
	RuleXDSSJGMapper xDSSJGMapper;
	
	@Override
	public List<ViolationDetail> executeRule(RuleTableInfo rule, HospitalClaim hospitalClaim,
			List<HospitalClaimDetail> hospitalClaimDetails) {
		List<ViolationDetail> result = new ArrayList<ViolationDetail>();
		
		List<HospitalClaimDetail> tempDetails = null;
		//"医保限定规则”,"出院带药","费用超限"三个类型的规则，医保为0的不审核
//		int ruleType = Integer.parseInt(rule.getRuleType());
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, List<HospitalClaimDetail>> sameDayProjectMap = new HashMap<String, List<HospitalClaimDetail>>();
		
		for(HospitalClaimDetail detail : hospitalClaimDetails)
		{
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
			
			//将同一天的项目放在一个List里
			String serviceDate = sf.format(detail.getServiceDate());
			if(sameDayProjectMap.containsKey(serviceDate))
			{
				sameDayProjectMap.get(serviceDate).add(detail);
			}
			else
			{
				List<HospitalClaimDetail> tempList = new ArrayList<HospitalClaimDetail>();
				tempList.add(detail);
				sameDayProjectMap.put(serviceDate, tempList);
			}
		}
		
		if(sameDayProjectMap.isEmpty())
		{
			return null;
		}
		
		Iterator<List<HospitalClaimDetail>> it = sameDayProjectMap.values().iterator();
		
		while(it.hasNext())
		{
		
			tempDetails =  it.next();
			
			if(tempDetails.size()<2)
			{
				continue;
			}
			
			
			//查询手术项目是否在规则表中
			List<Map<String, String>> indicationCode = xDSSJGMapper.selectIndicationCode(tempDetails);
			//如果规则表中没有，则不违规
			if(indicationCode.size() < 1){
				continue;
			}
			//对查询出来的项目分组
			Map<String,List<Map<String, String>>> groupMap = new HashMap<String,List<Map<String, String>>>();
			for(Map<String, String> map : indicationCode){
				String fzxh = map.get("FZXH");
				if(groupMap.get(fzxh)==null){
					List<Map<String, String>> tempList = new ArrayList<Map<String, String>>(); 
					tempList.add(map);
					groupMap.put(fzxh, tempList);
				}else{
					groupMap.get(fzxh).add(map);
				}
			}
			//对分组数据判断违规
			for(String fzxh : groupMap.keySet()){
				//System.out.println("分组："+fzxh);
				//某个分组中的数据
				List<Map<String, String>> tempList = groupMap.get(fzxh);
				MySortList<HospitalClaimDetail> mysortList = new MySortList<HospitalClaimDetail>();
				//将明细按服务日期排序
				mysortList.sortByMethod(tempDetails, "getServiceDate", false);
				int tempnum = 1;
				//遍历明细中的项目判断是否违规
				for(HospitalClaimDetail detail : tempDetails){
					String productCode = detail.getProductCode();
					BigDecimal unitPrice = detail.getUnitPrice();
					for(Map<String, String> tempMap : tempList){
						String xmbm = tempMap.get("XMBM");
						if(productCode.equals(xmbm)){
							//如果是第一项则不判断
							if(1 == tempnum){
								tempnum += 1;
							}else{
								String zkjg = tempMap.get("D2XSSYBJ");
								BigDecimal zkPrice = new BigDecimal(zkjg);
	//								System.out.println("限定编码："+xmbm+" 折扣："+zkjg);
	//								System.out.println("项目编码："+productCode+" 价格："+unitPrice+" 服务日期："+detail.getServiceDate());
	//								System.out.println(unitPrice.compareTo(zkPrice));
								//判断项目价格是否大于限定价格
								if(unitPrice.compareTo(zkPrice) == 1){
									//System.out.println("项目编码："+productCode+" 违规。。。");
									ViolationDetail violationDetail = ToolUtils.getViolationDetail(rule, hospitalClaim, detail, tempMap.get("TSXX"));
									result.add(violationDetail);
								}
							}
						}
					}
				}
				//System.out.println("----------------------------------------------");
			}
		}
		if(result.size() > 0){
			return result;
		}else{
			return null;
		}
	}

}
