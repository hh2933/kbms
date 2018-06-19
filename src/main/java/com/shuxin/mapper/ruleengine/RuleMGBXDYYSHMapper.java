package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 
 * 门规病限定项目
 *
 */
public interface RuleMGBXDYYSHMapper extends BaseMapper<CommonModel>{
	
	public List<Map<String, String>> selectSpecialDiseasesProjectInfo(List<HospitalClaimDetail> hospitalClaimDetails);

}
