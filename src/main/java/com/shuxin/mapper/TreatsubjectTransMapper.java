package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.TreatSubjectTrans;
import com.shuxin.model.ruleengine.HospitalClaimDetail;

public interface TreatsubjectTransMapper extends BaseMapper<TreatSubjectTrans> {

	public List<TreatSubjectTrans> findTreatSubjectTransDataGrid(
			Page<TreatSubjectTrans> page, Map<String, Object> condition);

	public	void addTreatSubjectTransHistory(Map<String, Object> params);

	public 	List<Map<String, Object>> selectDetailTreatsubjectTrans();

	public	List<Map<String, Object>> selecTeatsubjectTransHistory();

	public	void daoruData(Map<String, Object> map);

	
	public List<String> selectDefineSubjectCode(List<String> codes);
	
	
	
	
	
	
	
	
	
	
	
}
