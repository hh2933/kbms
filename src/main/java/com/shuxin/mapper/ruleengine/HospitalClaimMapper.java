package com.shuxin.mapper.ruleengine;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.ruleengine.HospitalClaim;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface HospitalClaimMapper extends BaseMapper<HospitalClaim>  {

	@Override
	Integer insert(HospitalClaim entity);

	@Override
	Integer deleteById(Serializable id);

	@Override
	Integer updateById(HospitalClaim entity);
	
	public List<Map<String, Object>> selectMonthTakeDrugsInfo(Map<String, Object> paramMap);

	public void deleteHospitalClaim(String diaSerialCode);
	
	public String selectLastOutHospitalDate(Map<String, String> paramMap);
}
