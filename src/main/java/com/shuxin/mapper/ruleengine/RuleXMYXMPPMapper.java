package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 
 *项目与项目匹配
 */
public interface RuleXMYXMPPMapper  extends BaseMapper<CommonModel>{

	public List<Map<String, String>> selectProjectMappingInfo();
}
