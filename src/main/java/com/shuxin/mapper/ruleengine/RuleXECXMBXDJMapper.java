package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

/**
 * 限二次项目报销单价
 * @author shuxin
 *
 */
public interface RuleXECXMBXDJMapper extends BaseMapper<CommonModel> {

	public List<Map<String, String>> selectIndicationCode(List<HospitalClaimDetail> hospitalClaimDetails);
}
