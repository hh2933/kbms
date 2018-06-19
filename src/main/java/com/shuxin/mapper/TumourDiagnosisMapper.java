package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.TumourDiagnosis;

public interface TumourDiagnosisMapper extends BaseMapper<TumourDiagnosis> {

public	List<TumourDiagnosis> findTumourDiagnosisDataGrid(
			Page<TumourDiagnosis> page, Map<String, Object> condition);

public	void addTumourDiagnosisHistory(Map<String, Object> params);

public	List<Map<String, Object>> selectDetailTumourDiagnosis();

public	List<Map<String, Object>> selecTumourDiagnosisHistory();

public	void daoruData(Map<String, Object> map);

public int selectTumourDiagnosisCount(List<String> list);

}
