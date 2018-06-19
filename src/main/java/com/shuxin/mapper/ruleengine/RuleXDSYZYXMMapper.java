package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 限定适应症项目
 *
 */
public interface RuleXDSYZYXMMapper  extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectIndicationProjectInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
