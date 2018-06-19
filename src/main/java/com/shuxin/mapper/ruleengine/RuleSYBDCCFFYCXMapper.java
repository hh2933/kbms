package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 
 *省医保单次处方费用超限
 */
public interface RuleSYBDCCFFYCXMapper extends BaseMapper<CommonModel>{
	
	public Map<String,String> selectProvincialMedicalInsuranceInfo();
	
	public List<String> selectByDrugcode();

}
