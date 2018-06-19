package com.shuxin.mapper.ruleengine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface HospitalClaimDetailMapper extends BaseMapper<HospitalClaimDetail>  {

	List<String> selectPatTodayProjectCode(Map<String, Object> paramMap);
	
	@Override
	Integer insert(HospitalClaimDetail entity);

	@Override
	Integer deleteById(Serializable id);

	@Override
	Integer updateById(HospitalClaimDetail entity);
	
	public Map<String, Object> selectLastClaimDetail(Map<String, Object> paramMap);

	public int selectFirstDrugCount(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> selectFrequentlyDrugCount(Map<String, Object> paramMap);
	
	public BigDecimal selectFrequentlyDrugNumber(Map<String, String> paramMap);
	
	public void deleteHospitalClaimDetail(String diaSerialCode);
	
	public BigDecimal selectSpecialPatientDrugAmount(Map<String, String> paramMap);
	
	public BigDecimal selectSpecialPatientDrugAmountOne(Map<String, Object> paramMap);
	
	public void insertBathHospitalClaimDetail(List<HospitalClaimDetail> list);
}
