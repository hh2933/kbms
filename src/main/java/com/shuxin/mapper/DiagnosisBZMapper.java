package com.shuxin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.shuxin.model.DiagnosticmaBZMpping;
import com.shuxin.model.Dictionary;

public interface DiagnosisBZMapper extends BaseMapper<DiagnosticmaBZMpping> {

	List<DiagnosticmaBZMpping> findbzDiagnosisDataGrid(
			Page<DiagnosticmaBZMpping> page, Map<String, Object> condition);

	List<String> selectColumnOfDiagnosisBZ(String tableName);

	void addDiagnosisBZHistory(Map<String, Object> params);

	void daoruData(Map<String, Object> map);

	void addKnowledgeBaseOpt(List<Map<String, Object>> list);

	List<Map<String, Object>> selectDetailDiagnosisBZ();

	List<Map<String, Object>> selectDiagnosisBZHistory();

}
