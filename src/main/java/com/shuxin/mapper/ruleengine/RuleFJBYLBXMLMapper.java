package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface RuleFJBYLBXMLMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectInsuranceFolder(List<HospitalClaimDetail> list);

}
