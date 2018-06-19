package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.DiagnosticCatalog;

public interface DiagnosisCatalogMapper extends BaseMapper<DiagnosticCatalog> {

	List<DiagnosticCatalog> findDiagnosisCatalogDataGrid(
			Page<DiagnosticCatalog> page, Map<String, Object> condition);

	void addDiagnosticCatalogHistory(Map<String, Object> params);

	List<Map<String, Object>> selectDetailDiagnosisCatalog();

	List<Map<String, Object>> selectDiagnosisCatalogHistory();

	void daoruData(Map<String, Object> map);

	public List<Map<String, String>> selectDiagnosticInfo(List<String> list);

}
