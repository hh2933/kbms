package com.shuxin.mapper.ruleengine;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.CommonModel;
import com.shuxin.model.ruleengine.HospitalClaimDetail;
/**
 * 限定就医方式
 *
 */
public interface RuleXDJYFSMapper extends BaseMapper<CommonModel> {
	
	public List<Map<String, String>> selectIndicationCode(@Param("jyfs")String jyfs, @Param("hospitalClaimDetails")List<HospitalClaimDetail> hospitalClaimDetails);

}
